package pro.butovanton.farestechruner;


import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class FViewModel extends AndroidViewModel {

    final public static String TAG = "DEBUG";

 //   private Application application;
 //   private Boolean requestLocationUpdates = false;
    private MutableLiveData<String> locationMutableLiveData = new MutableLiveData<>();
    private String user;

    public FViewModel(@NonNull Application application) {
        super(application);

    }

    public LiveData<String> startListener(String user) {
        this.user = user;
        Intent intent = new Intent(getApplication(), ServiceLocation.class);
        intent.putExtra("user", user);
        getApplication().startService(intent);
        return locationMutableLiveData;
    }

}
