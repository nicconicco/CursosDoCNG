package br.com.cursosdocng.cursosdocng.domain;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Carlos Nicolau Galves on 8/6/15.
 */
public abstract class BaseService extends Service {

    public BaseService() {
    }

    public IBinder onBind(Intent i) {
        return null;
    }

    public void onCreate() {
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        BaseService.WorkerThread t = new BaseService.WorkerThread(this.getClass().getSimpleName() + "-" + startId, intent);
        t.start();
        return super.onStartCommand(intent, flags, startId);
    }

    protected abstract void execute(Intent var1) throws Exception;

    public void onDestroy() {
    }

    private class WorkerThread extends Thread {
        private Intent intent;

        public WorkerThread(String threadName, Intent intent) {
            super(threadName);
            this.intent = intent;
        }

        public void run() {
            try {
                BaseService.this.execute(this.intent);
            } catch (Exception var5) {
                Log.e(this.getClass().getSimpleName(), var5.getMessage(), var5);
            } finally {
                BaseService.this.stopSelf();
            }

        }
    }
}
