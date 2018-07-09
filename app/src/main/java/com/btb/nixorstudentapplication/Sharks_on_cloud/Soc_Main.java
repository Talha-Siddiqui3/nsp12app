package com.btb.nixorstudentapplication.Sharks_on_cloud;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.btb.nixorstudentapplication.BookMyTa.Adaptors.RV_Adaptor_3_For_Search_Ta;
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

import io.fabric.sdk.android.services.common.CommonUtils;

public class Soc_Main extends AppCompatActivity {
    private Subject_Adaptor subject_adaptor;
    private RecyclerView rv;
    public static CollectionReference cr = FirebaseFirestore.getInstance().collection("/SharksOnCloud/AllSubjects/SubjectNames");
    private common_util cu = new common_util();
    private ArrayList<String> SubjectNames;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soc__main);
        pb = findViewById(R.id.loading_soc);
        rv = (RecyclerView) findViewById(R.id.rectcler_view_soc);
        rv.setLayoutManager(new LinearLayoutManager(this));
        GetAllSubjects();
    }

    private void GetAllSubjects() {
        SubjectNames = new ArrayList<>();
        cr.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                SubjectNames = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                        DocumentSnapshot doc = task.getResult().getDocuments().get(i);
                        SubjectNames.add(doc.getId());
                    }
                    initializeadaptor(SubjectNames);
                    loading.setVisibility(View.INVISIBLE);
                } else {
                    cu.ToasterLong(Soc_Main.this, "Error retrieving data from Server");
                }


            }
        });

    }

    private void initializeadaptor(ArrayList<String> SubjectNames) {
        subject_adaptor = new Subject_Adaptor(SubjectNames);
        rv.setAdapter(subject_adaptor);
    }


}
