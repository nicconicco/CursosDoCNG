package br.com.cursosdocng.cng.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import br.com.cursosdocng.cng.CngApplication;
import br.com.cursosdocng.cng.R;
import br.com.cursosdocng.cng.domain.CngService;
import br.com.cursosdocng.cng.util.BroadcastUtil;
import br.com.cursosdocng.cng.util.Constantes;
import br.com.cursosdocng.cng.util.Pref;
import br.com.cursosdocng.cng.util.Task;
import br.com.cursosdocng.cursosdocng.domain.GpsObjetoDoMeuServico;

/**
 * Created by mestre on 04/08/2015.
 */

// Aqui usamos disso para poder usar do suporte de transição entre os fragment
// Ser sempre android.support.v4.app.Fragment;

public class BaseFragment extends Fragment implements BaseActivity.MessageFromServiceDelegate {

    private static final String TAG = BaseFragment.class.getSimpleName();
    private String msg_aguarde = "Ola";
    private ArrayList tasks;
    private GpsObjetoDoMeuServico gpsObjeto;
    public static final Executor THREAD_POOL_EXECUTOR;
    private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue(10);
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + this.mCount.getAndIncrement());
        }
    };

    protected void dispararTaskProcurarServico() {
        Log.d(TAG, "dispararTaskProcurarServico");
    }


    static {
        THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(5, 128, 1L, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);
    }

    public BaseActivity getBaseActivity() {
        // Método existente no Fragment, sendo que ele esta sendo extendido na BaseActivity
        return (BaseActivity) getActivity();
    }

    public void replaceFragment(int containerViewId, Fragment fragment, String tag) {
        BaseActivity baseActivity = getBaseActivity();
        if (baseActivity != null) {
            FragmentManager fm = baseActivity.getSupportFragmentManager();
            if (fm != null) {
                fm.beginTransaction().replace(containerViewId, fragment, tag).commit();
            }
        }
    }

    protected Context getContext() {
        FragmentActivity activity = this.getActivity();
        return (Context) (activity != null ? activity : CngApplication.getInstance().getApplicationContext());
    }

    protected void startTaskProcuraPorOutroServicoACadaXTempo() {
        startTask(new BaseTask() {
            @Override
            public void execute() throws Exception {
                gpsObjeto = CngService.getNewService();
                Log.d(TAG, "execute() CngService.getNewService: " + gpsObjeto);
                Pref.setInteger(getBaseActivity(), Constantes.MARCOU_PROXIMO_AGENDAMENTO, 0);

            }

            @Override
            public void updateView() {

                chegouObjeto(gpsObjeto);
                Log.d(TAG, "ProcuraServicoService.updateView fazendo schedule 30 seg");
                // schedule = programar para x tempo
                Pref.setInteger(getBaseActivity(), Constantes.MARCOU_PROXIMO_AGENDAMENTO, 1);
                BroadcastUtil.scheduleProcurarServicoDifente(getContext());
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        // Registra delegate
        BaseActivity baseActivity = (BaseActivity) getActivity();
        baseActivity.setMessageFromServiceDelegate(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        BaseActivity baseActivity = (BaseActivity) getActivity();
        baseActivity.removeMessageFromServiceDelegate(this);

    }

    protected void chegouObjeto(GpsObjetoDoMeuServico gpsObjetoDoMeuServico) {
        Log.d(TAG, "chegou o seguinte objeto: GpsObjetoDoMeuServico");
    }

    protected void startTask(Task transacao) {
        this.startTask(transacao, R.string.msg_aguarde, true, true, -1, false);
    }

    protected void startTaskParallel(Task transacao) {
        this.startTask(transacao, R.string.msg_aguarde, true, true, -1, true);
    }

    private boolean startTask(Task transacao, int aguardeString, boolean showAguarde, boolean online, int progressId, boolean parallel) {
        try {
            if (online) {
                boolean e = isNetworkAvailable(this.getContext());
                if (!e) {
                    boolean tratou = transacao.onErrorNetworkUnavailable();
                    if (!tratou) {
                        alertDialog(this.getActivity(), this.getString(R.string.msg_erro_rede_indisponivel), this.getString(R.string.verifique));
                    }

                    return false;
                }
            }

            ThreadAsyncTask e1 = new ThreadAsyncTask(this, showAguarde, aguardeString, (Handler) null, progressId);
            if (this.tasks == null) {
                this.tasks = new ArrayList();
            }

            this.tasks.add(e1);
            transacao.preExecute();
            if (parallel && getAPILevel() >= 11) {
                e1.executeOnExecutor(THREAD_POOL_EXECUTOR, new Task[]{transacao});
            } else {
                e1.execute(new Task[]{transacao});
            }

            return true;
        } catch (Exception var9) {
            return false;
        }
    }

    public static int getAPILevel() {
        int apiLevel = Build.VERSION.SDK_INT;
        return apiLevel;
    }

    public static void alertDialog(final Activity activity, final String title, final String message) {
        try {
            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            AlertDialog e = (new AlertDialog.Builder(activity)).setTitle(title).setMessage(message).create();
                            e.setButton(-1, activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            e.show();
                        } catch (Throwable var2) {
                        }

                    }
                });
            }
        } catch (Throwable var4) {
        }

    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; ++i) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    @Override
    public void messageFromServiceReceived(Intent intent) {
        Log.d(TAG, getClass().getSimpleName() + ": messageFromServiceReceived: " + intent.getAction());
        // Agora eh so sobreescrever no fragment
    }
}
