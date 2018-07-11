package com.btb.nixorstudentapplication.Sharks_on_cloud;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Adaptors.Subject_Adaptor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Soc_Main extends AppCompatActivity implements View.OnClickListener {
    private Subject_Adaptor subject_adaptor;
    private RecyclerView rv;
    public static CollectionReference crSubjects = FirebaseFirestore.getInstance().collection("/SharksOnCloud/AllSubjects/SubjectNames");
    public static CollectionReference crUsers = FirebaseFirestore.getInstance().collection("/users");
    private common_util cu = new common_util();
    private ArrayList<String> AllSubjectNames;
    private ArrayList<String> MySubjectNames;
    private ArrayList<String> OtherSubjectNames;
    private ProgressBar loading;
    private Button mySubjects;
    private Button others;
    private Button all;
    private String username;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soc__main);
        loading = findViewById(R.id.loading_soc);
        rv = (RecyclerView) findViewById(R.id.rectcler_view_soc);
        rv.setLayoutManager(new LinearLayoutManager(this));
        SetUserName();
        GetUserSubjects();
        }


    private void initializeButtons() {
        mySubjects = findViewById(R.id.MySubjects_Button);
        others = findViewById(R.id.Others_Button);
        all = findViewById(R.id.All_Button);
        mySubjects.setVisibility(View.VISIBLE);
        others.setVisibility(View.VISIBLE);
        all.setVisibility(View.VISIBLE);
        mySubjects.setOnClickListener(this);
        others.setOnClickListener(this);
        all.setOnClickListener(this);
    }

    private void SetUserName() {
        username = cu.getUserDataLocally(this, "username");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.MySubjects_Button:
                initializeAdaptorMySubjects();
                break;
            case R.id.All_Button:
                initializeAdaptorAllSubjects();
                break;
            case R.id.Others_Button:
                initializeAdaptorOtherSubjects();
        }


    }


    private void GetUserSubjects() {
        MySubjectNames = new ArrayList<>();
        crUsers.document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    GetAllSubjects();
                    DocumentSnapshot doc = task.getResult();
                    MySubjectNames = (ArrayList<String>) doc.get("student_subjects");
                } else {
                    HideLoading();
                    cu.ToasterLong(Soc_Main.this, "Error retrieving data from Server");

                }
            }
        });
    }


    private void GetAllSubjects() {
        AllSubjectNames = new ArrayList<>();
        crSubjects.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                AllSubjectNames = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                        DocumentSnapshot doc = task.getResult().getDocuments().get(i);
                        AllSubjectNames.add(doc.getId());
                    }
                    GetOtherSubjects();
                    initializeAdaptorMySubjects();
                    initializeButtons();
                    HideLoading();
                } else {
                    HideLoading();
                    cu.ToasterLong(Soc_Main.this, "Error retrieving data from Server");
                }


            }
        });

    }


    private void GetOtherSubjects() {

        OtherSubjectNames = new ArrayList<>();
        for (int i = 0; i < AllSubjectNames.size(); i++) {

            if (!MySubjectNames.contains(AllSubjectNames.get(i))) {
                OtherSubjectNames.add(AllSubjectNames.get(i));
            }
        }


    }

    private void initializeAdaptorMySubjects() {
        subject_adaptor = new Subject_Adaptor(MySubjectNames);
        rv.setAdapter(subject_adaptor);

    }

    private void initializeAdaptorAllSubjects() {
        subject_adaptor = new Subject_Adaptor(AllSubjectNames);
        rv.setAdapter(subject_adaptor);

    }

    private void initializeAdaptorOtherSubjects() {
        subject_adaptor = new Subject_Adaptor(OtherSubjectNames);
        rv.setAdapter(subject_adaptor);

    }

    public void ShowLoading() {
        loading.setVisibility(View.VISIBLE);
    }

    public void HideLoading() {
        loading.setVisibility(View.INVISIBLE);
    }
}