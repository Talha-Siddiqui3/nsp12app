package com.btb.nixorstudentapplication.Misc;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class common_util {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public boolean checkPlayServices(Activity context, String error_message ) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(context, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Toast.makeText(context,error_message,Toast.LENGTH_SHORT).show();
                Log.i("Log", "This device is not supported.");
                context.finish();
            }
            return false;
        }
        return true;


    }
}
