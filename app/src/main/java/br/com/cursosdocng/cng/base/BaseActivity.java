package br.com.cursosdocng.cng.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.cursosdocng.cng.util.BroadcastUtil;

/**
 * Created by mestre on 04/08/2015.
 */

// Utilizamos do FragmentActivity para a transição de telas como o setContentView, por isso não usamos fragment, senão ele acusa desse erro
// Além do onCreate de um ser protected e outro public

public class BaseActivity extends FragmentActivity {


    private static final String TAG = BaseActivity.class.getSimpleName();

    // transição de telas da activiy pro fragment sempre é bom jogarmos na base para não repitir código
    public void showActivity(Context context, Class c) {
        Intent intent = new Intent(context, c);
        startActivity(intent);
    }

    // BroadcastReceir local.. apenas o app ira escutar este broadcast

    private BroadcastReceiver receiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            Log.d(TAG, "BaseActivity.BroadcastReceiver.onReceive > receiver1: " + action);
            messageFromServiceReceived(intent);
            Log.d("STARTSERVICO", "SERVICO FOI ESTARTADO AQUI");
        }
    };

    private BroadcastReceiver receiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            Log.i(TAG, "BaseActivity.BroadcastReceiver.onReceive > receiver2: " + action);
            messageFromServiceReceived(intent);
        }
    };

    private BroadcastReceiver receiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            Log.i(TAG, "BaseActivity.BroadcastReceiver.onReceive > receiver3: " + action);
            messageFromServiceReceived(intent);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();


        // Sempre registrar e no onPause desregistrar o BroadCast
        registerReceiver(receiver1, new IntentFilter(BroadcastUtil.ACTION_WAKEUP_PROCURAR_SERVICO));
        registerReceiver(receiver2, new IntentFilter(BroadcastUtil.ACTION_MESSAGE_SERVICO_TO_ACTIVITY));
        registerReceiver(receiver3, new IntentFilter(BroadcastUtil.ACTION_MESSAGE_GPS_TO_ACTIVITY));
    }

    @Override
    protected void onPause() {
        super.onPause();
        
        unregisterReceiver(receiver1);
        unregisterReceiver(receiver2);
        unregisterReceiver(receiver3);
    }


    protected synchronized void messageFromServiceReceived(Intent intent) {
        if (delegates != null) {
            Log.d(TAG, "messageFromServiceReceived para " + delegates.size() + " delegates");
            for (MessageFromServiceDelegate delegate : delegates) {
                Log.d(TAG, "messageFromServiceReceived delegate > " + delegate);
                delegate.messageFromServiceReceived(intent);
            }
        }
    }


    // Cria uma lista de delegates pois todos os fragments tem que pegar,
    // Apenas aqueles registrados que farão uma acao

    List<MessageFromServiceDelegate> delegates;

    public interface MessageFromServiceDelegate {
        void messageFromServiceReceived(Intent intent);
    }

    public void setMessageFromServiceDelegate(MessageFromServiceDelegate delegate) {
        if (delegates == null) {
            delegates = new ArrayList<MessageFromServiceDelegate>();
        }
        this.delegates.add(delegate);
        Log.d(TAG, "setMessageFromServiceDelegate registrou [" + delegate + "], tem " + delegates.size() + " delegates");
    }

    public void removeMessageFromServiceDelegate(MessageFromServiceDelegate delegate) {
        if (delegates != null) {
            this.delegates.remove(delegate);
            Log.d(TAG, "setMessageFromServiceDelegate removeu [" + delegate + "], tem " + delegates.size() + " delegates");
        }
    }


    // Otimo metodo para pegar o contexto

    protected Context getContext() {
        return this.getActivity();
    }

    protected Activity getActivity() {
        return this;
    }

}
