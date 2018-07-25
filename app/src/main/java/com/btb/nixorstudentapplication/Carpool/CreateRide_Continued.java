package com.btb.nixorstudentapplication.Carpool;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateRide_Continued extends AppCompatActivity {
    private FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
    private String TAG = "CreateRide_Continued";
    private common_util cu = new common_util();
    private HashMap<String,String> routes=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride__continued);
        getMapDataCloudFunction();
    }

    public void getMapDataCloudFunction() {
        // Create the arguments to the callable function.

        Map<String, Object> data = new HashMap<>();
        data.put("destination", "24.8114006657596,67.0217330098016 ");
        data.put("origin", "24.8114006657596,67.021733009801");
        Log.i(TAG, "method executed123");

        mFunctions
                .getHttpsCallable("map_function")
                .call(data)
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                        ArrayList routeList = (ArrayList) httpsCallableResult.getData();
                        JSONArray jRoutes = new JSONArray(routeList);

                        try {
                            for(int i=0;i<jRoutes.length();i++) {
                                JSONArray jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                                String distance = String.valueOf(jLegs.getJSONObject(0).getJSONObject("distance").get("text"));
                                Log.i("123456", distance);
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "FAIlED");
                        cu.ToasterLong(CreateRide_Continued.this, "Failed to connect to server");


                    }
                });


    }


}
