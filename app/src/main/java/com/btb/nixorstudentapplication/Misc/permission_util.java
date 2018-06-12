package com.btb.nixorstudentapplication.Misc;

import android.app.Activity;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

//Need to write the Callback function for permission.
// Handling the cases where the user refuses to allow access
public class permission_util{
    int RequestPermissionCode =1;
public void getPermissions(Activity act, String[] list_permissions){
    activity=act;
    if(checkBuildVer()){
        ActivityCompat.requestPermissions(activity, list_permissions
                , RequestPermissionCode); }

}


public boolean checkBuildVer(){
    if(Build.VERSION.SDK_INT >= 23){
return true;
    }
return false;
}
    Activity activity;


}
