package com.btb.nixorstudentapplication.Sharks_on_cloud;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Adapter.AllSubjectsAdapter;
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
    private common_util common_util = new common_util();

    public static String classRoomSelected = null;

    private ArrayList<Object> Allsubjects = new ArrayList<>();
    private ArrayList<String> Allbuckets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cloud);
        //XML
        data_recyler = findViewById(R.id.data_recycler);
        username = common_util.getUserDataLocally(this,"username");


        getAllSubjects(this);
    }

    public void getAllSubjects(final Context context){

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

    public void getAllBuckets(Context context, String subjectSelected){
        Allbuckets.add("MyBucket");

        String getNodeLocation = context.getString(R.string.node_soc_all_subjects);
        final CollectionReference subjectRootCollection = FirebaseFirestore.getInstance().collection(getNodeLocation + subjectSelected);
        subjectRootCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {

                    }
                } else {
                    Log.i(TAG, "Error getting buckts: ", task.getException());
                }
            }
        });



    }
}
