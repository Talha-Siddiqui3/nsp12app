package com.btb.nixorstudentapplication.Nsp_Portal;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.Misc.permission_util;
import com.btb.nixorstudentapplication.Nsp_Portal.Adaptors.Nsp_Adaptor;
import com.btb.nixorstudentapplication.Past_papers.MainPPActivity;
import com.btb.nixorstudentapplication.Past_papers.PdfLoad;
import com.btb.nixorstudentapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Nsp_Portal_MainActivity extends AppCompatActivity {

    private common_util common_util;
    private String Filename;
    public static String GUID = "";
    private String TAG;
    private Map<String, Object> map = new HashMap<>();
    private GridView gridView;
    private Nsp_Adaptor nsp_adaptor;
    private List<String> icons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nsp__portal);
        icons = new ArrayList<>();
        common_util = new common_util();
        GetGUIDLocally();
        GetNspIcons();
        TAG = "Nsp_Portal_MainActivity";
        gridView = findViewById(R.id.GridView_NspPortal);
        nsp_adaptor = new Nsp_Adaptor(icons, this);
        gridView.setAdapter(nsp_adaptor);
        GetExternalStoragePermission();
        MakePath();


    }


    public void MakePath() {
        File path = new File(Environment.getExternalStorageDirectory() + "/nixorapp/NspDocuments/");
        if (!path.exists())
            path.mkdirs();

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


    public void GetGUIDLocally() {
        GUID = common_util.getUserDataLocally(this, "GUID");

    }


    private void GetExternalStoragePermission() {
        permission_util permission_util = new permission_util();
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        permission_util.getPermissions(this, permissions);
    }


    public void GetNspIcons() {
        String username = common_util.getUserDataLocally(this, "username");
        DocumentReference dr = FirebaseFirestore.getInstance().collection("users").document(username).collection("icons").document("myicons");
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())

                {
                    map = task.getResult().getData();
                    for (int i = 0; i < map.size(); i++) {

                        Log.i(TAG, map.get(Integer.toString(i)).toString());
                        icons.add(i, map.get(Integer.toString(i)).toString());
                    }

                    nsp_adaptor.notifyDataSetChanged();
                } else {
                    Log.i(TAG, "cant get data");
                }
            }
        });
    }



}


