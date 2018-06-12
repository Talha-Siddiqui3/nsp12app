package com.btb.nixorstudentapplication.Autentication;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.btb.nixorstudentapplication.Misc.permission_util;
import com.btb.nixorstudentapplication.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login_screen extends AppCompatActivity {
    private FirebaseAuth FbAuth;
    FirebaseUser FbUser;
    permission_util permission_util;
    String TAG = "login_screen";


    //XML res
    EditText code_entry;
    EditText phone_entry;

    //Permission
    private static String[] PERMISSIONS = {
            android.Manifest.permission.RECEIVE_SMS,
            android.Manifest.permission.READ_SMS,

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        //Intialize var
        permission_util = new permission_util();
            //XMl
        code_entry = findViewById(R.id.code_entry);
        phone_entry = findViewById(R.id.phone_entry);
initialize();
    }
    public void initialize(){

        FbAuth = FirebaseAuth.getInstance();
        permission_util.getPermissions(login_screen.this,PERMISSIONS);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FbUser = FbAuth.getCurrentUser();
        if(FbUser!=null){
            Log.i(TAG,"User Logged in");
            userLoggedIn();
        }else{
            FbUser=null;
        }
    }
    public void userLoggedIn(){}













}
