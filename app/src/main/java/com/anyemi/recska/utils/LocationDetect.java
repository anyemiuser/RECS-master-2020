package com.anyemi.recska.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by ADMIN on 26-09-2017.
 */

public class LocationDetect implements LocationListener  {

//    mLocManager.removeUpdates(locationListenerObject);
//    mLocManager = null;

    private Context mContext;
    private LocationManager locationManager;
    int Interval = 0;
    LocationListener gpsListener;


    private static LocationDetect INSTANCE;

    public static LocationDetect getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LocationDetect();
        }
        return INSTANCE;
    }


    public void init(Context context) {
        this.mContext = context.getApplicationContext();
    }


    public Location getBestLocation() {
        Location gpslocation = null;
        Location networkLocation = null;

        gpsListener= new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Location l=location;
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        try {
            Settings.Secure.getInt(mContext.getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        if (locationManager == null) {
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        }
        try {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    return null;
                } else {
                     locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Interval, 0, gpsListener);// here you can set the 2nd argument time interval also that after how much time it will get the gps location
                    gpslocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Interval, 0, gpsListener);
                networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        } catch (IllegalArgumentException e) {
            Log.e("error", e.toString());
        }


        if (gpslocation == null && networkLocation == null) {
            Log.e("error", "no Gps or network found");
            return null;
        } else if (gpslocation != null && networkLocation != null) {
            if (gpslocation.getTime() < networkLocation.getTime()) {
                gpslocation = null;
                return networkLocation;
            } else {
                networkLocation = null;
                return gpslocation;
            }
        } else if (gpslocation == null) {
            return networkLocation;
        } else if (networkLocation == null) {
            return gpslocation;
        }


        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
