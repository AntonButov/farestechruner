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

    private String info;
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

    user = intent.getStringExtra("user");
    new LocationFinder(getApplicationContext(), user);

    startForeground(101, updateNotification());
    return START_NOT_STICKY;
    }

    private Notification updateNotification() {

        Context context = getApplicationContext();
        info = context.getResources().getString(R.string.notification);

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
            builder = new NotificationCompat.Builder(context);

        return builder.setContentIntent(action)
                .setContentTitle(info)
                .setTicker(info)
                .setSmallIcon(R.drawable.fui_ic_check_circle_black_128dp)
                .setContentIntent(action)
                .setOngoing(true).build();
    }
}
