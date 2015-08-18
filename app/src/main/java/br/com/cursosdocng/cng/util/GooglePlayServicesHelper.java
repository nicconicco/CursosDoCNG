package br.com.cursosdocng.cng.util;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mestre on 05/08/2015.
 */

public class GooglePlayServicesHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener {
    public static final String TAG = GooglePlayServicesHelper.class.getSimpleName();
    private final GoogleApiClient mGoogleApiClient;
    private List<LocationListener> locationListeners;

    public GooglePlayServicesHelper(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    // Conecta no Google Play Services
    public void connect() {
        Log.d(TAG, "connect()");
        mGoogleApiClient.connect();
    }

    // Desconecta do Google Play Services
    public void disconnect() {
        Log.d(TAG, "disconnect()");
        if(mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("mondial","onConnected()");

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000); // 10 segundos
        mLocationRequest.setFastestInterval(5000); // 5 segundos
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int status) {
        Log.d(TAG,"onConnectionSuspended(): " + status);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG,"onConnectionFailed(): " + connectionResult);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG,"onLocationChanged(): " + location);
        if(locationListeners != null) {
            for (LocationListener listener : locationListeners) {
                listener.onLocationChanged(location);
            }
        }
    }

    public void setLocationListeners(LocationListener locationListeners) {
        if(this.locationListeners == null) {
            this.locationListeners = new ArrayList<LocationListener>();
        }
        this.locationListeners.add(locationListeners);
    }

    public String getLastLocationString() {
        Location l = getLastLocation();
        double latitude = l.getLatitude();
        double longitude = l.getLongitude();
        return "lat/lng: " + latitude+"/"+longitude;
    }

    public Location getLastLocation() {
        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        return  l;
    }
}
