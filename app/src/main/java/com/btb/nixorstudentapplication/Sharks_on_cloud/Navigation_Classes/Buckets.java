package com.btb.nixorstudentapplication.Sharks_on_cloud.Navigation_Classes;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Adaptors.Buckets_Adaptor;
import com.btb.nixorstudentapplication.Sharks_on_cloud.MyBucket;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Soc_Main;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;

import javax.annotation.Nullable;

import info.hoang8f.android.segmented.SegmentedGroup;

public class Buckets implements View.OnClickListener {
    private static ArrayList<String> myClassBuckets;
    private static ArrayList<String> allBuckets;
    private static Buckets_Adaptor bucketsAdaptor;
    public static SegmentedGroup bucketsButtons;// so that BucketData class can turn on/off these buttons.
    private Button myClasses;
    private Button mostRecent;
    private Button topRated;
    public static RelativeLayout my_Bucket;//// so that BucketData class can turn on/off this.
    public static String subjectName;// need to access it in BucketDclass
    Activity context;
    private Boolean isInitialData;

    common_util cu = new common_util();

    public Buckets(Activity context, View v, String subjectName) {
        this.context = context;
        RemovePreviousButtons(v);
        this.subjectName = subjectName;
        GetAllBuckets(v);
        isInitialData = true;

    }


    private static void AddPreviousButtons(View v) {
        Subjects_homescreen.subjectButtons.setVisibility(View.VISIBLE);
        bucketsButtons.setVisibility(View.GONE);//TODO:NULL KA KHAYAL RAKHO
        my_Bucket.setVisibility(View.GONE);
    }


    private void RemovePreviousButtons(View v) {
        Subjects_homescreen.subjectButtons = v.findViewById(R.id.segmented_soc_subjects);
        Subjects_homescreen.subjectButtons.setVisibility(View.GONE);
    }

    private void initializeButtons(View v) {
        myClasses = v.findViewById(R.id.my_classes_soc);
        mostRecent = v.findViewById(R.id.most_recent_soc);
        topRated = v.findViewById(R.id.top_rated_soc);
        bucketsButtons = v.findViewById(R.id.segmented_soc_classes);
        my_Bucket = v.findViewById(R.id.my_bucket_layout);
        bucketsButtons.setVisibility(View.VISIBLE);
        my_Bucket.setVisibility(View.VISIBLE);
        my_Bucket.setOnClickListener(this);
        myClasses.setOnClickListener(this);
        mostRecent.setOnClickListener(this);
        topRated.setOnClickListener(this);
    }

    //You would have had a null pointer exception at some point
    //ohk thanks
//What if we save the user's subjects in shared prefs. and add a refresh button to get subjects from server.
    //haan good point
    //MARK: It gets current Students's classes only
    private void GetAllBuckets(final View v) {
        allBuckets = new ArrayList<>();
        Soc_Main.socRoot.document(Subjects_homescreen.button_Selected).collection("Subjects").document(subjectName)
                .collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots.isEmpty()) {
                    allBuckets.add(0, "empty");
                }
                if (e == null) {
                    for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (isInitialData) {
                            allBuckets.add(dc.getDocument().getId());
                        } else {
                            if (allBuckets.contains("empty")) {
                                allBuckets.clear();
                            }
                            Log.i("TEST123", dc.getType().toString());
                            switch (dc.getType()) {
                                case ADDED:
                                    if(!allBuckets.contains(dc.getDocument().getId())) {
                                        allBuckets.add(dc.getDocument().getId());
                                    }
                                    break;
                                case REMOVED:
                                    if(!allBuckets.contains(dc.getDocument().getId())) {
                                        allBuckets.remove(dc.getDocument().getId());
                                    }
                                    break;
                            }
                        }
                    }
                    initializeAdaptorAllBuckets(isInitialData);
                    initializeButtons(v);
                    isInitialData = false;


                } else {
                    cu.ToasterLong(context, "Error retrieving data form Server");
                }

            }
        });

    }

    //Orrr you could use remote config for this, Because kaafi rare hoga ke update hoon all subjects and if you ever want to tou you can use remote config. Check docs for
    //remote config. Alright best
    //MARK: It gets AS all classes.


    //BucketData calls this methoid to implement onBackPressed feature
    public static void initializeAdaptorAllBuckets(Boolean isInitialData) {
        if (isInitialData) {
            bucketsAdaptor = new Buckets_Adaptor(allBuckets);
            Soc_Main.setAdaptor_Generic(bucketsAdaptor);
        } else {
            bucketsAdaptor.notifyDataSetChanged();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // case R.id.my_classes_soc:
            //   initializeAdaptorAllBuckets();//TODO: Change this
            // break;
            case R.id.my_bucket_layout:
                Intent i = new Intent(Soc_Main.context, MyBucket.class);
                i.putExtra("subject", subjectName);
                Soc_Main.context.startActivity(i);
        }
    }

    //MARK: Main acitivty calls this
    public static void OnBackPressed(View v) {
        Soc_Main.isCurrentlyRunning = "Subjects_homescreen";//Providing back functionality for OnBackPressed in Soc_Main class(mainactvity).
        AddPreviousButtons(v);
        Subjects_homescreen.initializeAdaptorMySubjects();
    }


}








