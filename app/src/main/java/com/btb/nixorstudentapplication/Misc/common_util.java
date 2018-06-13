package com.btb.nixorstudentapplication.Misc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class common_util {
    static ProgressDialog pd;
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
    public static void errorReporting(String TAG, String errorcode, Exception e){
        encode_text(TAG+" "+errorcode);
        encode_text(TAG+" "+e.getMessage());

    }
    public static String encode_text(String text){
        text = "------"+text+"------";
        System.out.println(text);
        Log.i("ERROR:",text);
        return text;
    }
    public  void ToasterLong(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
    public void ToasterShort(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
    public void progressDialogShow(Context context, String message){
        pd = new ProgressDialog(context);
        pd.setMessage(message);
        pd.setCancelable(false);
        pd.show();
    }

    public void progressDialogHide(){
      if(pd!=null)
      {if(pd.isShowing())pd.dismiss();}
    }



}
