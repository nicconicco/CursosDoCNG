package br.com.cursosdocng.cng;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.cursosdocng.cng.domain.Gps;
import br.com.cursosdocng.cng.domain.GpsService;

/**
 * Created by mestre on 04/08/2015.
 */
public class CngApplication extends CApp {

    private Gps gps;
    private Intent gpsIntent;
    private int estadoDosFragments = 1;

    public static CngApplication getInstance() {
        return (CngApplication) CApp.getInstance();

    }



    @Override
    public int getProgressId() {
        return 0;
    }


    public void setGps(Gps gps) {
        this.gps = gps;
    }

    public Intent getGpsIntent() {
        return gpsIntent;
    }



    public void setGpsIntent(Intent gpsIntent) {
        this.gpsIntent = gpsIntent;
    }

    public int getEstadoDosFragments() {
        return estadoDosFragments;
    }

    public void setEstadoDosFragments(int estadoDosFragments) {
        this.estadoDosFragments = estadoDosFragments;
    }
}
