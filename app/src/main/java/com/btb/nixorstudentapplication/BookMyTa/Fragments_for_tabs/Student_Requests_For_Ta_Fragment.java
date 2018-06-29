package com.btb.nixorstudentapplication.BookMyTa.Fragments_for_tabs;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.btb.nixorstudentapplication.BookMyTa.Adaptors.RV_Adaptor_1;
import com.btb.nixorstudentapplication.BookMyTa.Main_Activity_Ta_Tab;
import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

//CLASS specific to TAs ONLY
//It receives all the requests send by students to logged in TA.
//Allows to accept/reject request

public class Student_Requests_For_Ta_Fragment extends Fragment {
    static String TAG = "Student_Requests_For_Ta_Fragment";
    View view;
    static CollectionReference cr = FirebaseFirestore.getInstance().collection("BookMyTa/BookMyTaDocument/Requests");
    List<String> DocIds = new ArrayList<>();
    RV_Adaptor_1 rvAdaptor;
    static common_util cu = new common_util();
    boolean isInitialData = true;
    List<Integer> localIndexList = new ArrayList<>();
    static Context context;

    public Student_Requests_For_Ta_Fragment() {
    }

    public List<String> GetRequest() {
        final List<String> requests = new ArrayList<>();


        cr.orderBy("latestUpdateTimestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {


                for (DocumentChange dc : snapshots.getDocumentChanges()) {
//Check whther it is the first time data is recivied from Firebase.
                    if (isInitialData) {
//checks if the request is for the logged in user who is a TA.
                        if (dc.getDocument().get("TaName").toString().equals(cu.getUserDataLocally(getContext(), "name"))) {
                            requests.add(dc.getDocument().get("StudentName").toString());
                            localIndexList.add(dc.getNewIndex());
                            DocIds.add(dc.getDocument().getId());

                        }

                    } else {
                        switch (dc.getType()) {
                            case ADDED:
                                if (dc.getDocument().get("TaName").toString().equals(cu.getUserDataLocally(getContext(), "name"))) {
                                   Log.i(TAG,"WORKING");
                                    requests.add(dc.getDocument().get("StudentName").toString());
                                    localIndexList.add(dc.getNewIndex());
                                    DocIds.add(dc.getDocument().getId());
                                    DataAddORRemove();
                                    break;
                                }
                            case REMOVED:
                                if (dc.getDocument().get("TaName").toString().equals(cu.getUserDataLocally(getContext(), "name"))) {
                                    //Common loop for both REMOVED and MODIFIED cases
                                    //It gets the corresponding localIndex of Firebase's Index
                                    //For exmaple 5th index of firebase's data is stored locally on 2nd index of maps
                                    //so it returns 2
                                    for (int i = 0; i < localIndexList.size(); i++) {
                                        if (localIndexList.get(i) == dc.getOldIndex()) {
                                            requests.remove(i);
                                            localIndexList.remove(i);
                                        }
                                    }
                                    DataAddORRemove();
                                    break;
                                }
                            case MODIFIED:
                                break;


                        }
                    }

                }
                isInitialData = false;
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
         rvAdaptor = new RV_Adaptor_1(GetRequest(), DocIds);
        rv.setAdapter(rvAdaptor);
        context=getActivity();
        return view;
    }

//It Updates the Request Status on Firebase when a TA clicks on accept/reject button.
    public static void UpdateRequest(final String requestStatus, String DocId) {

        DocumentReference dr = cr.document(DocId);
        dr.update("Status", requestStatus);
        dr.delete();
        cu.ToasterLong(context,"Request "+requestStatus);
    }

    //Executes when new requests arrives or old requests removed.
    public void DataAddORRemove() {
        rvAdaptor.notifyDataSetChanged();
    }


}