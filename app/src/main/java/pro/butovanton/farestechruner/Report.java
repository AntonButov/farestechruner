package pro.butovanton.farestechruner;

import android.location.Location;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Report {

    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private String user;

    public Report(String user) {
        this.user = user;
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("farestech");
    }

    void outLocation(Location location) {
        Log.d("DEBUG", locationToString(location));
        myRef.child(user)
                .child(timeToString(location.getTime()))
                .child("lat")
                .setValue(location.getLatitude());
        myRef.child(user)
                .child(timeToString(location.getTime()))
                .child("lon")
                .setValue(location.getLongitude());
        myRef.child(user)
                .child(timeToString(location.getTime()))
                .child("provider")
                .setValue(location.getProvider());
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
                + timeToString(location.getTime()) + " provider:" + location.getProvider();

    }

    String timeToString(Long time) {
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss dd:MM:yy");
        String timeFormat = formatter.format(time);
        return timeFormat;
    }
}
