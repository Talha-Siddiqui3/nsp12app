package com.btb.nixorstudentapplication.testFunc;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Test_Activity extends AppCompatActivity {
    private FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_);
        common_util cu=new common_util();
        String guid= cu.getUserDataLocally(this,"GUID");
        String username=cu.getUserDataLocally(this,"username");

        UpdateRequest(guid,username);
    }
    //CALLING CLOUD FUNCTION
    public void UpdateRequest(String guid, String username) {
        // Create the arguments to the callable function.

        Map<String, Object> data = new HashMap<>();
        Log.i("123", "method executed");
        data.put("GUID",guid);
        data.put("username",username);


        mFunctions
                .getHttpsCallable("pdfExtract")
                .call(data)
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                        if(httpsCallableResult.getData()!=null) {
                            Log.i("123456", httpsCallableResult.getData().toString());
                        }
                        else{
                            Log.i("123456","null");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("123", "FAIlED");

                    }
                });


    }


}