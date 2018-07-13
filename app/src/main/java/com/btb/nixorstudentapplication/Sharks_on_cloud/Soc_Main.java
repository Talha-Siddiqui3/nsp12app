package com.btb.nixorstudentapplication.Sharks_on_cloud;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.btb.nixorstudentapplication.GeneralLayout.activity_header;
import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Navigation_Classes.BucketData;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Navigation_Classes.Buckets;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Navigation_Classes.Subjects_homescreen;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Soc_Main extends AppCompatActivity {

    private static RecyclerView rv;
    public static CollectionReference socRoot = FirebaseFirestore.getInstance().collection("/SharksOnCloud");
    public static CollectionReference usersRoot = FirebaseFirestore.getInstance().collection("/users");
    private static common_util cu = new common_util();
    public static View v;
    private static ProgressBar loading;
    public static Activity context;
    public static String isCurrentlyRunning = "Subjects_homescreen";//the class which is currently running in screen
    private activity_header activity_header;

    public static String username;
//Bro wessay this isn't wrong but when using object oriented (Is the spelling right? :p) use objects rather than imports. so

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = getLayoutInflater().inflate(R.layout.activity_soc__main, null);
        setContentView(v);
        activity_header=findViewById(R.id.toolbar_top_soc);
        activity_header.setActivityname("Sharks On Cloud");
        loading = findViewById(R.id.loading_soc);
        context = this;
        rv = (RecyclerView) findViewById(R.id.rectcler_view_soc);
        rv.setLayoutManager(new LinearLayoutManager(this));
        SetUserName();
        new Subjects_homescreen(this, v);
    }


    private void SetUserName() {
        username = cu.getUserDataLocally(this, "username");
    }


    public static void ShowLoading() {
        loading.setVisibility(View.VISIBLE);
    }

    public static void HideLoading() {
        loading.setVisibility(View.INVISIBLE);
    }

    public static void setAdaptor_Generic(RecyclerView.Adapter adaptor) {
        rv.setAdapter(adaptor);
    }

    public static void ClearData() {
        rv.setAdapter(null);
    }

    @Override
    public void onBackPressed() {

        switch (isCurrentlyRunning) {
            case "Subjects_homescreen":
                super.onBackPressed();
           break;
            case "Buckets":
                Buckets.OnBackPressed(v);
                break;
            case "BucketData":
                BucketData.OnBackPressed();
                break;

        }


    }
}
