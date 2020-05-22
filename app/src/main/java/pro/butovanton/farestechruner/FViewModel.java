package pro.butovanton.farestechruner;


import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Timer;
import java.util.TimerTask;

public class FViewModel extends AndroidViewModel {

    private Application application;

    final public static String TAG = "DEBUG";

   private LocationFinder locationFinder;

    public FViewModel(@NonNull Application application) {
        super(application);
    //locationFinder = new LocationFinder(application);
    this.application = application;
    }

    public Boolean starStopService(final String user) {
        Intent intent = new Intent(getApplication(), ServiceLocation.class);
        if (!isMyServiceRunning(ServiceLocation.class)) {

            intent.putExtra("user", user);
            application.startService(intent);
            return true;
        }
        else {
            application.stopService(intent);
        }

        return false;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) application.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
