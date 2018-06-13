package com.btb.nixorstudentapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.btb.nixorstudentapplication.Autentication.login_screen;
import com.btb.nixorstudentapplication.Misc.common_util;

public class welcome_screen extends AppCompatActivity {
common_util common_util;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        common_util=new common_util();
        String message= getString(R.string.notsupported);
        if(common_util.checkPlayServices(welcome_screen.this,message)) {
            startActivity(new Intent(welcome_screen.this, login_screen.class));
            finish();

        }
    }
}
