package br.com.cursosdocng.cng;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;

import com.google.android.gms.ads.doubleclick.AppEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.cursosdocng.cng.domain.Gps;

/**
 * Created by Carlos Nicolau Galves on 8/17/15.
 */
public abstract class CApp extends Application {

    private static CApp instance;
    private boolean startTick = false;
    private Handler handler = new Handler();
    private Runnable timerRunnable;
    private List<CApp.TickListener> listeners;
    private long timeTick = -1L;


    public CApp() {
        super();
    }

    public static CApp getInstance() {
        if(instance == null) {
            throw new IllegalStateException("Faltou a classe [project]Application estendendo LivroAndroidApplication e android:name no Application do AndroidManifest.xml!");
        } else {
            return instance;
        }
    }

    public abstract int getProgressId();

    public void onCreate() {
        super.onCreate();
        instance = this;
        this.listeners = new ArrayList();
        this.timerRunnable = this.getRunnable();
    }

    public void onTerminate() {
        super.onTerminate();
        this.clearListenersTick();
    }

    public void startTick(long time) {
        this.timeTick = time;
        this.startTick = true;
        this.scheduleTimer(time);
    }

    public void stopTick() {
        this.timeTick = -1L;
        this.startTick = false;
    }

    public void scheduleTimer(long time) {
        this.handler.postDelayed(this.timerRunnable, time);
    }

    private Runnable getRunnable() {
        return new Runnable() {
            public void run() {
                if(CApp.this.listeners != null) {
                    Iterator var1 = CApp.this.listeners.iterator();

                    while(var1.hasNext()) {
                        CngApplication.TickListener tl = (CngApplication.TickListener)var1.next();
                        tl.tick();
                    }

                    if(CApp.this.startTick & CApp.this.timeTick >= 0L) {
                        CApp.this.scheduleTimer(CApp.this.timeTick);
                    }
                }

            }
        };
    }

    public void addListener(CApp.TickListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(CApp.TickListener listener) {
        if(this.listeners != null) {
            this.listeners.remove(listener);
        }

    }

    public void clearListenersTick() {
        if(this.listeners != null) {
            this.listeners.clear();
        }

    }


    public interface AddTickListener {
        void addTickListener(CApp.TickListener var1);

        void removeTickListener(CApp.TickListener var1);
    }

    public interface TickListener {
        void tick();
    }


}
