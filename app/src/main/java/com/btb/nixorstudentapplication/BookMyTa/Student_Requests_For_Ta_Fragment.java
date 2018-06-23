package com.btb.nixorstudentapplication.BookMyTa;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.Past_papers.Load_papers;
import com.btb.nixorstudentapplication.Past_papers.RvAdaptor;
import com.btb.nixorstudentapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Student_Requests_For_Ta_Fragment extends Fragment {
    static String TAG = "Student_Requests_For_Ta_Fragment";
    View view;
    static CollectionReference cr = FirebaseFirestore.getInstance().collection("BookMyTa/BookMyTaDocument/Requests");
    List<String> DocIds = new ArrayList<>();
    RV_Adaptor_1 rvAdaptor = new RV_Adaptor_1(GetRequest(), DocIds);
    common_util cu = new common_util();
    boolean initial = true;

    public Student_Requests_For_Ta_Fragment() {
    }

    public List<String> GetRequest() {
        final List<String> requests = new ArrayList<>();


        cr.orderBy("latestUpdateTimestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {


                for (DocumentChange dc : snapshots.getDocumentChanges()) {

                    if (initial) {

                        if (dc.getDocument().get("TaName").toString().equals(cu.getUserDataLocally(getContext(), "name"))) {
                            requests.add(dc.getDocument().get("StudentName").toString());
                            DocIds.add(dc.getDocument().getId());

                        }

                    } else {
                        Log.i("ABC", "IF NOT EXECUTED");
                        switch (dc.getType()) {
                            case ADDED:

                                if (dc.getDocument().get("TaName").toString().equals(cu.getUserDataLocally(getContext(), "name"))) {
                                    requests.add(dc.getDocument().get("StudentName").toString());
                                    AddData();
                                    DocIds.add(dc.getDocument().getId());
                                    break;
                                }
                            case REMOVED:
                                break;
                            case MODIFIED:
                                break;


                        }
                    }

                }
                initial = false;
            }
        });


        return requests;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.student_requests_for_ta, container, false);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.student_requests_for_ta_rv);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        rv.setAdapter(rvAdaptor);


        return view;
    }


    public static void UpdateRequest(final String requestStatus, String DocId) {

        DocumentReference dr1 = cr.document(DocId);
        dr1.update("Status", requestStatus);


    }

    public void AddData() {
        rvAdaptor.notifyDataSetChanged();
    }


}