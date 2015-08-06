package br.com.cursosdocng.cursosdocng;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mestre on 04/08/2015.
 */
public abstract class CngApplication extends Application {
    private static CngApplication instance;
    private boolean startTick = false;
    private Handler handler = new Handler();
    private Runnable timerRunnable;
    private List<CngApplication.TickListener> listeners;
    private long timeTick = -1L;

    public CngApplication() {
    }

    public static CngApplication getInstance() {
        if(instance == null) {
            throw new IllegalStateException("Faltou a classe [project]Application estendendo LivroAndroidApplication e android:name no Application do AndroidManifest.xml!");
        } else {
            return instance;
        }
    }


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
                if(CngApplication.this.listeners != null) {
                    Iterator var1 = CngApplication.this.listeners.iterator();

                    while(var1.hasNext()) {
                        CngApplication.TickListener tl = (CngApplication.TickListener)var1.next();
                        tl.tick();
                    }

                    if(CngApplication.this.startTick & CngApplication.this.timeTick >= 0L) {
                        CngApplication.this.scheduleTimer(CngApplication.this.timeTick);
                    }
                }

            }
        };
    }

    public void addListener(CngApplication.TickListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(CngApplication.TickListener listener) {
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
        void addTickListener(CngApplication.TickListener var1);

        void removeTickListener(CngApplication.TickListener var1);
    }

    public interface TickListener {
        void tick();
    }
}
