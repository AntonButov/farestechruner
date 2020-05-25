package pro.butovanton.farestechruner;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.LOCATION_SERVICE;

public class LocationFinder {

    private final int TIMER_DELLAY = 60 * 1000 * 20;
    private LocationManager locationManager;
    private Timer timer;
    private Report report;


    public LocationFinder(Context context, String user) {
        report = new Report(user);
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        addLocationListener();
    }

    @SuppressLint("MissingPermission")
    private void addLocationListener() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                60 * 1000 * 5, 10, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                60 * 1000 * 5, 10, locationListener);

        if (timer != null)
            timer = createTimer();
    }

    @SuppressLint("MissingPermission")
    private Timer createTimer() {
        Timer timer =  new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                report.outLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
                report.outLocation(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
            }
        }, 10000, TIMER_DELLAY);
        return timer;
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null)
                report.outLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("DEBUG", "onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String provider) {
            @SuppressLint("MissingPermission")
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null)
                report.outLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
