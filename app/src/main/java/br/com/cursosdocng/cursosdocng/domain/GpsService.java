package br.com.cursosdocng.cursosdocng.domain;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;

import br.com.cursosdocng.cursosdocng.util.GooglePlayServicesHelper;

/**
 * Created by mestre on 05/08/2015.
 */

public class GpsService extends Service implements LocationListener {

    private static final String TAG = GpsService.class.getSimpleName();
    private static final String GPS_SERVICE = "GPS_SERVICE";

    private GooglePlayServicesHelper googlePlayServicesHelper;

    private boolean FAKE_MODE = false;
    private float distance;
    private Gps gps;

    private String latitude;
    private String longitude;
    Object mLastLocation;
    private long mLastLocationMillis;
    private boolean ativo;

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
    protected void execute(Intent intent) throws Exception {
        googlePlayServicesHelper = new GooglePlayServicesHelper(getApplicationContext());
        googlePlayServicesHelper.setLocationListeners(this);
        googlePlayServicesHelper.connect();
        mLastLocationMillis = SystemClock.elapsedRealtime();
        ativo = true;
        String codViatura = "";

        while (ativo) {

            if (true) {
                try {
                    if (getGpsLatitude() != null && getGpsLongitude() != null) {
                        latitude = getGpsLatitude().replace(",", ".");
                        longitude = getGpsLongitude().replace(",", ".");
                        if (MondialService.SendVehiclePosition(login.login, codViatura, status, latitude, longitude)) {
                            Log.v(TAG, "ok enviou posicao LAT:" + latitude + " - LONG:" + longitude);
                        } else {
                            Log.v(TAG, "ok enviou NAO posicao LAT:" + latitude + " - LONG:" + longitude);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(5000);
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
//
        //       latitude = "-23,6895484";
        //       longitude = "-46,3897377";

        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());

//        Log.d("mondial","GpsService.onLocationChanged: " + String.format("lat/lng: %s/%s",latitude,longitude));

        sendBroadcast();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void sendBroadcast() {
        gps = new Gps();
        gps.setLatitude(latitude);
        gps.setLongitude(longitude);

        MondialApplication.getInstance().setGps(gps);

        // Broadcast
        Bundle b = new Bundle();
        b.putSerializable(Gps.KEY, gps);

        BroadcastUtil.sendMessageToActivity(this, BroadcastUtil.ACTION_MESSAGE_GPS_TO_ACTIVITY, b);

        Log.d("mondial", "GpsService.sendBroadcast bundle: " + gps);
    }
}
