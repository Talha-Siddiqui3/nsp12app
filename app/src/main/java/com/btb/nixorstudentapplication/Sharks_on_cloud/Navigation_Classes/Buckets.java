package com.btb.nixorstudentapplication.Sharks_on_cloud.Navigation_Classes;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Adaptors.Buckets_Adaptor;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Soc_Main;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;


import java.util.ArrayList;

import info.hoang8f.android.segmented.SegmentedGroup;

public class Buckets implements View.OnClickListener {
    private static ArrayList<String> myClassBuckets;
    private static ArrayList<String> allBuckets;
    private static Buckets_Adaptor bucketsAdaptor;
    public static SegmentedGroup bucketsButtons;// so that Names class can turn on/off these buttons.
    private Button myBucket;
    private Button mostRecent;
    private Button topRated;
    public static String subjectName;// need to access it in names class
    Activity context;

    common_util cu = new common_util();

    public Buckets(Activity context, View v, String subjectName) {
        this.context = context;
        RemovePreviousButtons(v);
        GetUserClasses(v);
        this.subjectName = subjectName;
    }


    private static void AddPreviousButtons(View v) {
        Subjects_homescreen.subjectButtons.setVisibility(View.VISIBLE);
        bucketsButtons.setVisibility(View.GONE);
    }


    private void RemovePreviousButtons(View v) {
        Subjects_homescreen.subjectButtons = v.findViewById(R.id.segmented_soc_subjects);
        Subjects_homescreen.subjectButtons.setVisibility(View.GONE);
    }

    private void initializeButtons(View v) {
        myBucket = v.findViewById(R.id.my_bucket_button);
        mostRecent = v.findViewById(R.id.most_recent_soc);
        topRated = v.findViewById(R.id.top_rated_soc);
        bucketsButtons = v.findViewById(R.id.segmented_soc_classes);
        bucketsButtons.setVisibility(View.VISIBLE);
        myBucket.setOnClickListener(this);
        mostRecent.setOnClickListener(this);
        topRated.setOnClickListener(this);
    }

    //You would have had a null pointer exception at some point
    //ohk thanks
//What if we save the user's subjects in shared prefs. and add a refresh button to get subjects from server.
    //haan good point
    //MARK: It gets current Students's classes only
    private void GetUserClasses(final View v) {
        MyClassesNames = new ArrayList<>();
        Soc_Main.usersRoot.document(Soc_Main.username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    MyClassesNames = (ArrayList<String>) doc.get("student_classes");
                    GetASClasses(v);
                } else {
                    cu.ToasterLong(context, "Error retrieving data from Server");

                }
            }
        });
    }

    //Orrr you could use remote config for this, Because kaafi rare hoga ke update hoon all subjects and if you ever want to tou you can use remote config. Check docs for
    //remote config. Alright best
    //MARK: It gets AS all classes.



    //Names calls this methoid to implement onBackPressed feature
    public static void initializeAdaptorMyClasses() {
        bucketsAdaptor = new Buckets_Adaptor(MyClassesNames);
        Soc_Main.setAdaptor_Generic(bucketsAdaptor);
    }

    private void initializeAdaptorASClasses() {
        bucketsAdaptor = new Buckets_Adaptor(myClassBuckets);
        Soc_Main.setAdaptor_Generic(bucketsAdaptor);

    }

    private void initializeAdaptorA2Classes() {
        bucketsAdaptor = new Buckets_Adaptor(A2ClassesNames);
        //here use context and like for example
        Soc_Main.setAdaptor_Generic(bucketsAdaptor);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_bucket_button:
                initializeAdaptorMyClasses();
                break;
            case R.id.AS_Classes:
                initializeAdaptorASClasses();
                break;
            case R.id.A2_Classes:
                initializeAdaptorA2Classes();
        }
    }

    //MARK: Main acitivty calls this
    public static void OnBackPressed(View v) {
        Soc_Main.isCurrentlyRunning = "Subjects_homescreen";//Providing back functionality for OnBackPressed in Soc_Main class(mainactvity).
        AddPreviousButtons(v);
        Subjects_homescreen.initializeAdaptorMySubjects();
    }


}








