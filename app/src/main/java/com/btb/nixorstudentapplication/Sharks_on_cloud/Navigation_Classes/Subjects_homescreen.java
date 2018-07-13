package com.btb.nixorstudentapplication.Sharks_on_cloud.Navigation_Classes;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Adaptors.Subject_Adaptor_SOC;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Soc_Main;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import info.hoang8f.android.segmented.SegmentedGroup;

public class Subjects_homescreen implements View.OnClickListener {
    private static ArrayList<String> AllSubjectNames;
    private static ArrayList<String> MySubjectNames;
    private static Subject_Adaptor_SOC subject_adaptor;
    private Button mySubjects;
    private Button AS;
    private Button A2;
    public static SegmentedGroup subjectButtons;//so that Classes class can turn on//off buttons
    public static String button_Selected="A2";
    common_util cu = new common_util();

    public Subjects_homescreen(Activity context, View v) {
        GetUserSubjects(v, context);
    }


    private void initializeButtons(final View v) {
        mySubjects = v.findViewById(R.id.MySubjects_Button);
        AS = v.findViewById(R.id.AS_Button);
        A2 = v.findViewById(R.id.A2_Button);
        subjectButtons = v.findViewById(R.id.segmented_soc_subjects);
        subjectButtons.setVisibility(View.VISIBLE);
        mySubjects.setOnClickListener(this);
        AS.setOnClickListener(this);
        A2.setOnClickListener(this);
    }


    private void GetUserSubjects(final View v, final Context context) {
        MySubjectNames = new ArrayList<>();
        Soc_Main.usersRoot.document(Soc_Main.username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    GetAllSubjects(v, context);
                    DocumentSnapshot doc = task.getResult();
                    MySubjectNames = (ArrayList<String>) doc.get("student_subjects");
                    GetAllSubjects(v, context);
                } else {
                    Soc_Main.HideLoading();
                    cu.ToasterLong(context, "Error retrieving data from Server");

                }
            }
        });
    }


    private void GetAllSubjects(final View v, final Context context) {
        AllSubjectNames = new ArrayList<>();
        Soc_Main.socRoot.document("A2").collection("Subjects").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                AllSubjectNames = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                        DocumentSnapshot doc = task.getResult().getDocuments().get(i);
                        AllSubjectNames.add(doc.getId());
                    }
                    initializeAdaptorMySubjects();
                    initializeButtons(v);
                    Soc_Main.HideLoading();
                } else {
                    Soc_Main.HideLoading();
                    cu.ToasterLong(context, "Error retrieving data from Server");
                }


            }
        });

    }



//Made this public as it is called onBackPressed too from Buckets class.

    public static void initializeAdaptorMySubjects() {
        subject_adaptor = new Subject_Adaptor_SOC(MySubjectNames);
        Soc_Main.setAdaptor_Generic(subject_adaptor);
    }

    private static void initializeAdaptorAllSubjects() {
        subject_adaptor = new Subject_Adaptor_SOC(AllSubjectNames);
        Soc_Main.setAdaptor_Generic(subject_adaptor);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.MySubjects_Button:
                initializeAdaptorMySubjects();
                button_Selected="A2";//TODO:CHANGE THIS.REMOVED HARDCODED
                break;
            case R.id.AS_Button:
                button_Selected = "AS";
                initializeAdaptorAllSubjects();
                break;
            case R.id.A2_Button:
                button_Selected = "A2";
                initializeAdaptorAllSubjects();
                break;
        }

    }
}
