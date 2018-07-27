package com.btb.nixorstudentapplication.testFunc;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

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
        MakePath();
        UpdateRequest();
    }
    //CALLING CLOUD FUNCTION
    public void UpdateRequest() {
        // Create the arguments to the callable function.

        Map<String, Object> data = new HashMap<>();
        Log.i("123", "method executed");

        mFunctions
                .getHttpsCallable("DocumentAccessFunc")
                .call()
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                        Map<String, Object> recieveddata = new HashMap<>();
                        String s = (String) httpsCallableResult.getData();
                        File file1 = new File(Environment.getExternalStorageDirectory() + "/nixorapp/NspDocuments/" + "123" +".pdf");
                        Log.i("Encoded String", s+"123");
                        byte[] pdfAsBytes = Base64.decode(s, 0);
                        FileOutputStream os;

                        try {
                            os = new FileOutputStream(file1, false);
                            os.write(pdfAsBytes);
                            os.flush();
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
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

    public void MakePath() {
        File path = new File(Environment.getExternalStorageDirectory() + "/nixorapp/NspDocuments/");
        if (!path.exists())
            path.mkdirs();

    }

}