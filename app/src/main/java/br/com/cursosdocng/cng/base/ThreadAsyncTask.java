package br.com.cursosdocng.cng.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;

import java.io.IOException;

import br.com.cursosdocng.cng.CApp;
import br.com.cursosdocng.cng.R;
import br.com.cursosdocng.cng.util.Task;

import static br.com.cursosdocng.cng.base.BaseFragment.alertDialog;

/**
 * Created by Carlos Nicolau Galves on 8/17/15.
 */

public class ThreadAsyncTask extends AsyncTask<Task, Void, Task> implements DialogInterface.OnCancelListener {
    private static final String TAG = "Transacao";
    public ProgressDialog progress;
    private final Activity activity;
    private boolean running;
    private Handler handlerRefresh;
    private Fragment fragment;
    private int progressId;

    public ThreadAsyncTask(Activity activity, boolean showAguarde, int aguardeString, Handler handlerRefresh) {
        this((Activity)activity, showAguarde, aguardeString, handlerRefresh, -1);
    }

    public ThreadAsyncTask(Activity activity, boolean showAguarde, int aguardeString, Handler handlerRefresh, int progressId) {
        this.activity = activity;
        this.handlerRefresh = handlerRefresh;
        this.progressId = progressId;
        if(showAguarde) {
            this.showProgress(aguardeString);
        }

        this.running = true;
    }

    public ThreadAsyncTask(Fragment fragment, boolean showAguarde, int aguardeString, Handler handlerRefresh) {
        this((Fragment)fragment, showAguarde, aguardeString, handlerRefresh, -1);
    }

    public ThreadAsyncTask(Fragment fragment, boolean showAguarde, int aguardeString, Handler handlerRefresh, int progressId) {
        this.fragment = fragment;
        this.progressId = progressId;
        this.activity = fragment.getActivity();
        this.handlerRefresh = handlerRefresh;
        if(showAguarde) {
            this.showProgress(aguardeString);
        }

        this.running = true;
    }

    private int getProgressId() {
        if(this.progressId > 0) {
            return this.progressId;
        } else {
            CApp app = CApp.getInstance();
            int progress = app.getProgressId();
            return progress;
        }
    }

    public void onCancel(DialogInterface dialog) {
        this.running = false;
        boolean cancel = this.cancel(true);
    }

    protected void onCancelled() {
        super.onCancelled();
    }

    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected Task doInBackground(Task... params) {
        Task t = params[0];
        boolean ok = this.run(t);
        return ok?t:null;
    }

    public boolean run(Task task) {
        boolean e;
        try {
            boolean tratou;
            try {
                if(!this.running) {
                    return false;
                }

                task.execute();
                if(this.handlerRefresh != null) {
                    this.handlerRefresh.removeMessages(0);
                }

                e = true;
            } catch (IOException var91) {
                tratou = this.callOnError(task, var91);
                if(!tratou && this.running) {
                }

                return false;
            } catch (Throwable var92) {
                tratou = this.callOnError(task, var92);
                if(!tratou && this.running) {
                    if(var92 instanceof DomainException) {
                    } else {
                        alertDialog(this.activity, this.activity.getString(R.string.msg_erro), this.activity.getString(R.string.msg_erro_sub));
                    }

                    return false;
                }

                return false;
            }
        } finally {
            try {
                this.hideProgress();
            } catch (Exception var89) {
            } finally {
                this.progress = null;
            }

        }

        return e;
    }

    protected boolean callOnError(Task t, Throwable error) {
        try {
            if(t != null && this.activity != null && !this.activity.isFinishing()) {
                return t.onError(error);
            }
        } catch (Throwable var4) {
        }

        return false;
    }

    protected void onPostExecute(Task t) {
        try {
            if(t != null && this.activity != null && !this.activity.isFinishing()) {
                t.updateView();
            }
        } catch (Throwable var3) {
        }

    }

    public void showProgress(int aguardeString) {
        try {
            View e = null;
            if(this.fragment != null) {
                View fragView = this.fragment.getView();
                if(fragView != null) {
                    e = fragView.findViewById(this.getProgressId());
                }
            }

            if(e == null) {
                e = this.activity.findViewById(this.getProgressId());
            }

            if(e == null) {
                this.progress = ProgressDialog.show(this.activity, (CharSequence)null, this.activity.getString(aguardeString), false, true, this);
                this.progress.setCancelable(true);
            } else {
                e.setVisibility(View.GONE);
            }
        } catch (Throwable var4) {
        }

    }

    public void hideProgress() {
        try {
            boolean e = this.activity != null && !this.activity.isFinishing();
            if(e) {
                if(this.progress != null && this.progress.isShowing()) {
                    this.progress.dismiss();
                }

                View view = null;
                if(this.fragment != null) {
                    View viewProgress = this.fragment.getView();
                    if(viewProgress != null) {
                        view = viewProgress.findViewById(this.getProgressId());
                    }
                } else {
                    view = this.activity.findViewById(this.getProgressId());
                }

                if(view != null) {
                    final View finalView = view;
                    view.post(new Runnable() {
                        public void run() {
                            try {
                                finalView.setVisibility(View.VISIBLE);
                            } catch (Throwable var2) {
                            }

                        }
                    });
                }
            }
        } catch (Throwable var4) {
        }

    }
}
