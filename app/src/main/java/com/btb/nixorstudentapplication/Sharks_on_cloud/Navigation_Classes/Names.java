package com.btb.nixorstudentapplication.Sharks_on_cloud.Navigation_Classes;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;


import com.btb.nixorstudentapplication.Sharks_on_cloud.Adaptors.Names_Adaptor;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Soc_Main;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class Names {
    private String className;
    private ArrayList<String> studentNames;
    private String classType;
    private Names_Adaptor namesAdadptor;


    public Names(Activity context, View v, String className) {
        this.className = className;
        RemovePreviousButtons(v);
        GetAllNames(v, context);
    }

    private void GetAllNames(final View v, final Activity context) {
        studentNames = new ArrayList<>();
        //CHECKS WHeTHER AS OR A2
        if (className.substring(0, 1) == "1") {
            classType = "Classes AS";
        } else {
            classType = "Classes A2";
        }
        Soc_Main.socRoot.document("AllSubjects").collection("SubjectNames").document(Buckets.subjectName).collection(classType)
                .document(className).collection("Names").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                    DocumentSnapshot doc = task.getResult().getDocuments().get(i);
                    studentNames.add(doc.getId());
                }
                initializeAdaptorNames();
            }
        });

    }


    private static void AddPreviousButtons(View v) {
        Buckets.bucketsButtons.setVisibility(View.VISIBLE);
    }


    private void RemovePreviousButtons(View v) {
        Buckets.bucketsButtons.setVisibility(View.GONE);
    }

    private void initializeAdaptorNames() {
        Names_Adaptor namesAdadptor = new Names_Adaptor(studentNames);
        Soc_Main.setAdaptor_Generic(namesAdadptor);
    }


    //MARK: Main acitivty calls this
    public static void OnBackPressed(View v) {
        Soc_Main.isCurrentlyRunning = "Classes";//Providing back functionality for OnBackPressed in Soc_Main class(mainactvity).
        AddPreviousButtons(v);
        Buckets.initializeAdaptorMyClasses();
    }


}
