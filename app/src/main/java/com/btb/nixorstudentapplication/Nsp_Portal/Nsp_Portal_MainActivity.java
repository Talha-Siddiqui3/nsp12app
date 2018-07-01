package com.btb.nixorstudentapplication.Nsp_Portal;

import android.Manifest;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.Misc.permission_util;
import com.btb.nixorstudentapplication.Nsp_Portal.Adaptors.Nsp_Adaptor;
import com.btb.nixorstudentapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Nsp_Portal_MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nsp__portal);

    }




//In case we need GUID from FIrebase
    /*public void GetGUIDFireStore() {
        String userName = common_util.extractUsername(this, "talha.siddiqui@nixorcollege.edu.pk");
        DocumentReference dr = FirebaseFirestore.getInstance().collection("users").document(userName);
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())

                {

                    GUID = task.getResult().get("student_guid").toString();
                } else {
                    Log.i(TAG, "Couldn't get GUID");

                }
            }
        });
    }
*/




}


