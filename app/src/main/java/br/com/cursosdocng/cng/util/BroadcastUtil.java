package br.com.cursosdocng.cng.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;
import java.util.Set;


/**
 * Created by Carlos Nicolau Galves on 7/10/15.
 */
public class BroadcastUtil {
    public static final String ACTION_WAKEUP_PROCURAR_SERVICO = "ACTION_WAKEUP_PROCURAR_SERVICO";
    public static final String ACTION_MESSAGE_GPS_TO_ACTIVITY = "ACTION_MESSAGE_GPS_TO_ACTIVITY";
    public static final String ACTION_MESSAGE_SERVICO_TO_ACTIVITY = "ACTION_MESSAGE_SERVICO_TO_ACTIVITY";

    private static final String TAG = BroadcastUtil.class.getSimpleName();

    public static void scheduleProcurarServicoDifente(Context context) {

        Intent it = new Intent(ACTION_WAKEUP_PROCURAR_SERVICO);
        PendingIntent p = PendingIntent.getBroadcast(context, 0, it, 0);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());

        int segundos = Constantes.TIME_SCHEDULE_PROCURA_SERVICO;
        c.add(Calendar.SECOND, segundos);
        AlarmManager alarme = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarme.cancel(p);

        long time = c.getTimeInMillis();

        alarme.set(AlarmManager.RTC_WAKEUP, time, p);

    }

    public static void cancelaProcuraServicoDiferente(Context context) {
        Intent it = new Intent(ACTION_WAKEUP_PROCURAR_SERVICO);
        PendingIntent p = PendingIntent.getBroadcast(context, 0, it, 0);
        AlarmManager alarme = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarme.cancel(p);
    }

    public static void sendMessageToActivity(Context context, String action, Bundle args) {
        Log.d("mondial", "BroadcastUtil.sendMessageToActivity: " + args);
        if(args != null) {
            Set<String> keys = args.keySet();
            for (String key: keys) {
                Object value = args.get(key);
            }
        }
        Intent it = new Intent(action);
        it.putExtras(args);
        context.sendBroadcast(it);
    }
}
