package com.btb.nixorstudentapplication.Sharks_on_cloud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Adapter.AllSubjectsAdapter;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Adapter.buckets_Adapter;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Adapter.uploadedImages_Adapter;
import com.btb.nixorstudentapplication.Sharks_on_cloud.ImageViewer.ImageViewer;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class MainCloudActivity extends Activity {

    private String TAG = "MainCloudActivity";
    public static String username;
    private RecyclerView data_recyler;
    private GridView images_View;
    private common_util common_util = new common_util();
   private  RelativeLayout myBucketButtons;

    public static String classRoomSelected = null;
    public static String bucketSelected = null;

    private ArrayList<Object> Allsubjects = new ArrayList<>();

    private ArrayList<String> Allbuckets = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cloud);
        //XML
        data_recyler = findViewById(R.id.data_recycler);
        images_View = findViewById(R.id.images_View);
        myBucketButtons = findViewById(R.id.mybucketHeader);
        username = common_util.getUserDataLocally(this,"username");


        getAllSubjects(this);
    }
    //Getting list of subjects
    public void getAllSubjects(final Context context){
        Allsubjects = new ArrayList<>();
        data_recyler.setVisibility(View.VISIBLE);
        images_View.setVisibility(View.GONE);
        myBucketButtons.setVisibility(View.GONE);
        data_recyler.setLayoutManager(new LinearLayoutManager(context));
        data_recyler.setAdapter(null);
        String getNodeLocation = context.getString(R.string.node_soc_all_subjects);
        final DocumentReference subjectRootCollection = FirebaseFirestore.getInstance().document(getNodeLocation);

        subjectRootCollection.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot= task.getResult();
                Map<String,Object> map = documentSnapshot.getData();
                Log.i(TAG,Integer.toString(map.size()));
                Allsubjects  =new ArrayList<Object>(map.values());
                Log.i(TAG,Integer.toString(Allsubjects.size()));
                setAllSubjectsAdapter(context,Allsubjects,data_recyler);



            }
        });



    }
    public void setAllSubjectsAdapter(Context context, ArrayList<Object> data, RecyclerView rv){
        AllSubjectsAdapter allSubjectsAdapter = new AllSubjectsAdapter(data,context,rv);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(allSubjectsAdapter);
    }

    //Getting buckets for a particular subject
    public void getAllBuckets(final Context context, String subjectSelected){
        classRoomSelected = subjectSelected;
        myBucketButtons.setVisibility(View.GONE);
        Allbuckets = new ArrayList<>();
        Allbuckets.add("MyBucket");
        String getNodeLocation = context.getString(R.string.node_soc_all_subjects);
        final CollectionReference subjectRootCollection = FirebaseFirestore.getInstance().collection(getNodeLocation + subjectSelected);
        subjectRootCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                    Allbuckets.add(document.getId());

                    }

                    setBucketAdapter(context,Allbuckets);
                } else {
                    Log.i(TAG, "Error getting buckts: ", task.getException());
                }
            }
        });



    }
    public void setBucketAdapter(Context context, ArrayList<String> imageUrls){
        buckets_Adapter adapter = new buckets_Adapter(imageUrls,context);
        images_View.setAdapter(adapter);
        images_View.setVisibility(View.VISIBLE);
        data_recyler.setVisibility(View.INVISIBLE);

    }



    //Getting all images for that particular user and the subject
    public void getAllImages(final Context context, String username){
        bucketSelected = username;
        images_View.setAdapter(null);
        myBucketButtons.setVisibility(View.VISIBLE);
        Button upload_button = findViewById(R.id.upload_button);

        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, upload_activity.class));
            }
        });

    }



    public void openImageViewer(Context context){
        ArrayList<String> urls = new ArrayList<>();
        urls.add("https://firebasestorage.googleapis.com/v0/b/nixor-9afe0.appspot.com/o/BIO102%2Fhayaa-aryan%2F-L52zi1BykR0guI_m7lDlol?alt=media&token=3e32a422-4bfd-43ec-be49-5f1b93b6714b");
        urls.add("https://firebasestorage.googleapis.com/v0/b/nixor-9afe0.appspot.com/o/CHM105%2Fmohib-tariq%2F-L50IyajRbUI6dOMcn1Mlol?alt=media&token=90b013a3-8d92-4e77-8402-1aa29742f864");
        urls.add("https://firebasestorage.googleapis.com/v0/b/nixor-9afe0.appspot.com/o/PHY103%2Fmubin-khan%2F-L5J-rn-p9LL97Z53f_Plol?alt=media&token=676edb86-57f1-4bf3-ae18-5dde28409046");
        urls.add("https://firebasestorage.googleapis.com/v0/b/nixor-9afe0.appspot.com/o/the_killing_joke_by_fifinho5-d6tpq36.jpg?alt=media&token=9a6e273a-f79f-4289-a5e6-b88cf621eaea");
        urls.add("https://firebasestorage.googleapis.com/v0/b/nixor-9afe0.appspot.com/o/PHY103%2Fmubin-khan%2F-L5OaR6BbpDZtYz5_8z9lol?alt=media&token=78be3b1c-c9da-4ab8-91cf-5c18db09aed8");
       ImageViewer imageViewer = new ImageViewer(urls);
        imageViewer.startActivity(context);
    }


    @Override
    public void onBackPressed() {
        if(bucketSelected!=null){
            bucketSelected=null;
            getAllBuckets(this,classRoomSelected);
        }else if (classRoomSelected!=null){
            classRoomSelected=null;
            getAllSubjects(this);
        }else {
            super.onBackPressed();
        }

    }
}
