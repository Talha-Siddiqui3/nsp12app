package com.btb.nixorstudentapplication.Sharks_on_cloud;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.btb.nixorstudentapplication.GeneralLayout.activity_header;
import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
//import com.btb.nixorstudentapplication.Sharks_on_cloud.Navigation_Classes.BucketData;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Navigation_Classes.Buckets;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Navigation_Classes.Subjects_homescreen;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    //private static GridView gv;

    public static String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = getLayoutInflater().inflate(R.layout.activity_soc__main, null);
        setContentView(v);
        activity_header=findViewById(R.id.toolbar_top_soc);
        activity_header.setActivityname("Sharks On Cloud");
        loading = findViewById(R.id.loading_soc);
        context = this;
        //gv=findViewById(R.id.gridview_bucketData);
        rv = (RecyclerView) findViewById(R.id.rectcler_view_soc);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        rv.setItemViewCacheSize(20);
        rv.setDrawingCacheEnabled(true);
        rv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        String year=cu.getUserDataLocally(this,"year");
        SetUserName();
        new Subjects_homescreen(this, v,getYearWithSubjects());

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
      /*  ClearDataGv();*/
    }
   /* public static void setAdaptor_Generic_GridView(BaseAdapter adaptor){
        gv.setAdapter(adaptor);
        ClearDataRv();
    }*/



    public static void ClearDataRv() {
        rv.setAdapter(null);
    }
   /* public static void ClearDataGv() {
        gv.setAdapter(null);
    }
*/
    @Override
    public void onBackPressed() {

        switch (isCurrentlyRunning) {
            case "Subjects_homescreen":
                super.onBackPressed();
           break;
            case "Buckets":
                HideLoading();
                Buckets.OnBackPressed(v);
                break;
           /* case "BucketData":
                HideLoading();
                BucketData.OnBackPressed();
                break;*/

        }


    }
    private HashMap<String,String> getYearWithSubjects() {
       HashMap<String,String> map=new HashMap<>();
        List<String> subjects=new ArrayList<String>(cu.getUserDataLocallyHashSet(this,"student_subjects"));
        List<String> classes=new ArrayList<String>(cu.getUserDataLocallyHashSet(this,"student_classes"));
        for(int i=0;i<subjects.size();i++){
            String year="";
            if (classes.get(i).substring(3, 4).equals("1")) {
               year="AS";
            }
            else{
                year="A2";
            }
            map.put(subjects.get(i),year);
        }
return map;
    }
}
