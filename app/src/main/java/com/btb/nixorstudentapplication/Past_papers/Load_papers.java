package com.btb.nixorstudentapplication.Past_papers;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;


import com.btb.nixorstudentapplication.Misc.permission_util;
import com.btb.nixorstudentapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Load_papers extends AppCompatActivity {
    public static Context context;
    Button byYear;
    Button byVariant;
    String TAG = "Load_papers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_papers);
        byVariant = findViewById(R.id.byVariant);
        byYear = findViewById(R.id.byYear);

        GetExternalStoragePermission();
        GetDataFireBase();
    }

    private void GetExternalStoragePermission() {
        permission_util permission_util = new permission_util();
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        permission_util.getPermissions(Load_papers.this, permissions);
    }


    ArrayList<paperObject> FbQueryData = new ArrayList<>();
    ArrayList<String> Actualname = new ArrayList<>();

    public Boolean checkIfAllErrors(paperObject paperObject) {
        if (!paperObject.getMonth().equals("error")) {
            return false;
        }
        if (!paperObject.getType().equals("error")) {
            return false;
        }
        if (!paperObject.getVariant().equals("error")) {
            return false;
        }
        if (!paperObject.getYear().equals("error")) {
            return false;
        }
        return true;
    }

    public void GetDataFireBase() {
        CollectionReference cr = FirebaseFirestore.getInstance().collection("Past Papers/Subjects/Chem");
        Query ppQuery = cr.orderBy("year");
        ppQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Map<String, Object> map;
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        map = document.getData();
                        if (!Actualname.contains(map.get("name").toString())) {
                            paperObject paperObject = new paperObject();
                            addDataToObject(paperObject, map.get("month"), map.get("year"), map.get("variant"), map.get("type"));
                            if (!checkIfAllErrors(paperObject)) {
                                FbQueryData.add(paperObject);
                                Actualname.add(map.get("name").toString());
                                Log.i(TAG, document.getId() + " => " + document.getData());
                            }
                        }
                    }
                } else {
                    Log.i(TAG, "Error getting documents: ", task.getException());
                }
                loadpapers(FbQueryData, Actualname);
            }
        });


    }

    public void addDataToObject(paperObject paperObject, Object month, Object year, Object variant, Object type) {
        if (month != null) {
            paperObject.setMonth(month.toString());
        } else {
            paperObject.setMonth("error");
        }
        if (variant != null) {
            if (variant.toString().equals("1+2+3+4+5+6")) {
                paperObject.setVariant("All");
            } else {
                paperObject.setVariant(variant.toString());
            }

        } else {
            paperObject.setVariant("error");
        }
        if (year != null) {
            paperObject.setYear(year.toString());
        } else {
            paperObject.setYear("error");
        }
        if (type != null) {
            paperObject.setType(type.toString());
        } else {
            paperObject.setType("error");
        }
    }

    private void loadpapers(ArrayList<paperObject> mydata, ArrayList<String> Actualnames) {
        RecyclerView rv = findViewById(R.id.rv_list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        RvAdaptor rvAdaptor = new RvAdaptor(mydata, Load_papers.this, Actualnames);
        rv.setAdapter(rvAdaptor);
    }
}