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

public class ServiceLocation extends Service {

    private String info = "Запись включена.";

    private DatabaseReference myRef;
    private LocationManager locationManager;
    private String user;

    public ServiceLocation() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(101, updateNotification());

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.myRef = database.getReference("farestech");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000 * 1, 1, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                1000 * 1, 1, locationListener);
        user = intent.getStringExtra("user");
        return START_NOT_STICKY;
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            outLocation(location);
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
                outLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    void outLocation(Location location) {
 //       locationMutableLiveData.setValue(locationToString(location));
        Log.d("DEBUG", locationToString(location));
        myRef.child(user)
                .child(timeToString(location.getTime()))
                .child("lat")
                .setValue(location.getLatitude());
        myRef.child(user)
                .child(timeToString(location.getTime()))
                .child("lon")
                .setValue(location.getLatitude());
 /*       myRef.child(user)
                .child("lat")
                .setValue(location.getLatitude());
        myRef.child(user)
                .child("lon")
                .setValue(location.getLatitude());
        myRef.child(user)
                .child("time")
                .child(timeToString(location.getTime()));
*/
    }

    String locationToString(Location location) {
        return String.format("Coordinates: lat = %1$.4f, lon = %2$.4f", location.getLatitude(), location.getLongitude()) + " "
                + timeToString(location.getTime());

    }

    String timeToString(Long time) {
        DateFormat formatter = new SimpleDateFormat("hh:mm dd:MM:yy");
        String timeFormat = formatter.format(time);
        return timeFormat;
    }

    private Notification updateNotification() {

        Context context = getApplicationContext();

        PendingIntent action = PendingIntent.getActivity(context,
                0, new Intent(context, MainActivity.class),
                PendingIntent.FLAG_CANCEL_CURRENT); // Flag indicating that if the described PendingIntent already exists, the current one should be canceled before generating a new one.

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            String CHANNEL_ID = "alex_channel";

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "AlexChannel",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Alex channel description");
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
