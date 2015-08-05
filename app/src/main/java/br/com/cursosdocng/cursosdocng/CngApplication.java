package br.com.cursosdocng.cursosdocng;

import android.app.Application;
import android.content.Context;

/**
 * Created by mestre on 04/08/2015.
 */
public class CngApplication extends Application {

    private static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }
}
