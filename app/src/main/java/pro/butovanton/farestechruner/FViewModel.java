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

    private final int TIMER_INTERVAL = 60 * 1 * 1000;

    final public static String TAG = "DEBUG";
    Timer timer = new Timer();

   private LocationFinder locationFinder;
   private MutableLiveData<String> locationMutableLiveData = new MutableLiveData<>();
    private String user;

    public FViewModel(@NonNull Application application) {
        super(application);
    locationFinder = new LocationFinder(application);

    }

    public LiveData<String> startListener(final String user) {

        this.user = user;
        /*
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplication(), ServiceLocation.class);
                intent.putExtra("user", user);
                getApplication().startService(intent);
            }
        },0, TIMER_INTERVAL);

         */
        if (!isMyServiceRunning(ServiceLocation.class)) {
            Intent intent = new Intent(getApplication(), ServiceLocation.class);
            intent.putExtra("user", user);
            getApplication().startService(intent);
        }
        return locationMutableLiveData;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getApplication().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
