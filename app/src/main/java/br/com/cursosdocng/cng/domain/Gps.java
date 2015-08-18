package br.com.cursosdocng.cng.domain;

import java.io.Serializable;

/**
 * Created by Carlos Nicolau Galves on 7/13/15.
 */
public class Gps implements Serializable {
    public static final String KEY = "GPS";

    private String longitude;
    private String latitude;


    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Gps{" +
                "longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}
