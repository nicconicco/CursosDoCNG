package br.com.cursosdocng.cng.domain;

import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.google.android.gms.location.LocationListener;

import br.com.cursosdocng.cng.util.BroadcastUtil;
import br.com.cursosdocng.cng.util.GooglePlayServicesHelper;
import br.com.cursosdocng.cng.CngApplication;

/**
 * Created by mestre on 05/08/2015.
 */

public class GpsService extends BaseService implements LocationListener {

    private static final String TAG = GpsService.class.getSimpleName();
    private static final String GPS_SERVICE = "GPS_SERVICE";
//    private static final long TEMPO_DE_ESPERA = 5000;
    private static final long TEMPO_DE_ESPERA = 50000;

    private GooglePlayServicesHelper googlePlayServicesHelper;

    private boolean FAKE_MODE = false;
    private float distance;

    private String latitude;
    private String longitude;
    Object mLastLocation;
    private long mLastLocationMillis;
    private boolean ativo;
    private Gps gps;

    Location locationA = new Location("point A");
    Location locationB = new Location("point B");

    private final IBinder mBinder = new LocalBinder();


    public class LocalBinder extends Binder {
        public GpsService getService() {
            // Retorna a instância do GpsService para que os clients possam acessar seus métodos
            return GpsService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // retorna a classe ConexaoGpsInterface para a activity utilizar
        return mBinder;

    }

    @Override
    protected void execute(Intent var1) throws Exception {
        googlePlayServicesHelper = new GooglePlayServicesHelper(getApplicationContext());
        googlePlayServicesHelper.setLocationListeners(this);
        googlePlayServicesHelper.connect();
        mLastLocationMillis = SystemClock.elapsedRealtime();
        ativo = true;

        Log.d(TAG, "estartou servico");

        while (ativo) {
            if (true) {
                if (getGpsLatitude() != null && getGpsLongitude() != null) {
                    latitude = getGpsLatitude().replace(",", ".");
                    longitude = getGpsLongitude().replace(",", ".");

                    if (CngService.mandarPosicaoAtualParaWebServices(latitude, longitude)) {
                        Log.d(TAG, "chamou: mandarPosicaoAtualParaWebServices");
                    } else {
                        // faz alguma coisa avisando
                    }

                    Log.d(TAG, "latitude: " + latitude + " / " + " longitude: " + longitude);
                }
            }
            try {
                Thread.sleep(TEMPO_DE_ESPERA);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stopSelf();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ativo = false;
        googlePlayServicesHelper.disconnect();
    }

    public GooglePlayServicesHelper getGooglePlayServicesHelper() {
        return googlePlayServicesHelper;
    }

    public String getGpsLatitude() {
        return latitude;
    }

    public String getGpsLongitude() {
        return longitude;
    }

    public Object getmLastLocation() {
        return mLastLocation;
    }

    public long getmLastLocationMillis() {
        return mLastLocationMillis;
    }

    public String getmLastLocationString() {
        return "Latitude: " + latitude + " / Longitude: " + longitude;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        mLastLocationMillis = SystemClock.elapsedRealtime();

        //       latitude = "-23,6895484";
        //       longitude = "-46,3897377";

        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());

        sendBroadcast();
    }



    private void sendBroadcast() {

        gps = new Gps();
        gps.setLatitude(latitude);
        gps.setLongitude(longitude);

        CngApplication.getInstance().setGps(gps);

        // Broadcast
        Bundle b = new Bundle();
        b.putSerializable(Gps.KEY, gps);

        BroadcastUtil.sendMessageToActivity(this, BroadcastUtil.ACTION_MESSAGE_GPS_TO_ACTIVITY, b);

    }
}
