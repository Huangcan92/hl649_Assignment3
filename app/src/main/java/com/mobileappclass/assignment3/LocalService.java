package com.mobileappclass.assignment3;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class LocalService extends Service {
    private LocationManager manager;

    public LocalService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //LocationManager.GPS_PROVIDER|
            // it can be GPS_PROVIDER or NETWORK_PROVIDER
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, listener);

        }
    };
    Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (timer == null) {
            Log.e("log","timer.schedule");
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            }, 0, 10000);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    public static LocationListener mListener;
    LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                Log.e("log", location.getLongitude() + " " + location.getLatitude());
                if (mListener != null) {
                    mListener.onLocationChanged(location);
                }
                manager.removeUpdates(listener);
            }
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

}
