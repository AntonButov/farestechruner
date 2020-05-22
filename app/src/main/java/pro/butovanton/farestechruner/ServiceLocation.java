package pro.butovanton.farestechruner;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class ServiceLocation extends Service {

    private Report report;

    private final int TIMER_DELLAY = 60 * 1000 * 20;
    private String info = "Запись включена.";

    private LocationManager locationManager;
    private String user;

    public ServiceLocation() {
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {

        super.onCreate();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                report.outLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
                report.outLocation(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
            }
        },10000,TIMER_DELLAY);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        user = intent.getStringExtra("user");
        if (report == null) report = new Report(user);
        startForeground(101, updateNotification());

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                60 * 1000 * 5, 10, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                60 * 1000 * 5, 10, locationListener);
        return START_NOT_STICKY;
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
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

    private Notification updateNotification() {

        Context context = getApplicationContext();

        PendingIntent action = PendingIntent.getActivity(context,
                0, new Intent(context, MainActivity.class),
                PendingIntent.FLAG_CANCEL_CURRENT); // Flag indicating that if the described PendingIntent already exists, the current one should be canceled before generating a new one.

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            String CHANNEL_ID = "Farestech_channel";

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Farestech",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Farestech channel description");
            manager.createNotificationChannel(channel);

            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        }
        else
        {
            builder = new NotificationCompat.Builder(context);
        }

        return builder.setContentIntent(action)
                .setContentTitle(info)
                .setTicker(info)
                .setContentText(info)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(action)
                .setOngoing(true).build();
    }
}
