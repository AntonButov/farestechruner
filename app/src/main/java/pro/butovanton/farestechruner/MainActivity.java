package pro.butovanton.farestechruner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final int MY_PERMISSIONS_LOCATION_REQUEST = 101;
    final int MY_IGNORE_OPTIMIZATION_REQUEST = 102;
    final int RC_SIGN_IN = 100;
    FirebaseAuth firebaseAuth;
    FViewModel viewModel;
    private Button buttonRun;
    private TextView textViewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    textViewUser = findViewById(R.id.textViewUser);
    firebaseAuth = FirebaseAuth.getInstance();
    viewModel = new ViewModelProvider(MainActivity.this).get(FViewModel.class);
    buttonRun = findViewById(R.id.buttonRun);
    buttonRun.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonRunClick();
        }
    });


        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        boolean isIgnoringBatteryOptimizations = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (pm != null) {
                isIgnoringBatteryOptimizations = pm.isIgnoringBatteryOptimizations(getPackageName());
            }
            if(!isIgnoringBatteryOptimizations){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, MY_IGNORE_OPTIMIZATION_REQUEST);
            }else {
                Toast.makeText(getApplicationContext(), "power allowed", Toast.LENGTH_LONG).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null)
            startPhone();
        else {
            textViewUser.setText("Пользователь: " + user.getPhoneNumber());
        }

        if (!checkPermissions()) {
            startLocationPermissionRequest();
            buttonRun.setEnabled(false);
        } else
            buttonRun.setEnabled(true);

        setButtonRun(viewModel.getServiceStatus());
   }

   void buttonRunClick() {
       FirebaseUser user = firebaseAuth.getCurrentUser();
       if (user != null)
           setButtonRun(viewModel.starStopService(user.getPhoneNumber()));
   }

    void setButtonRun(Boolean start) {
    if (start) buttonRun.setText(R.string.run);
    else buttonRun.setText(R.string.stop);
    }

    void startPhone() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build());
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d(viewModel.TAG, "onResultOk user " + user);
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }

        if (requestCode == MY_IGNORE_OPTIMIZATION_REQUEST) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            boolean isIgnoringBatteryOptimizations = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (pm != null) {
                    isIgnoringBatteryOptimizations = pm.isIgnoringBatteryOptimizations(getPackageName());
                }
                if (isIgnoringBatteryOptimizations) {
                    Toast.makeText(getApplicationContext(), "power allowed", Toast.LENGTH_LONG).show();
                } else {
                    // Not ignoring battery optimization
                }
            }

        }
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_LOCATION_REQUEST);
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(viewModel.TAG, "onRequestPermissionResult");
        if (requestCode == MY_PERMISSIONS_LOCATION_REQUEST) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(viewModel.TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
    //            getLastLocation();
            } else {

            }
        }
    }


}

