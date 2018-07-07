package com.btb.nixorstudentapplication.BookMyTa.Fragments_for_tabs;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.btb.nixorstudentapplication.BookMyTa.Adaptors.RV_Adaptor_2;
import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Class for Students who requested to book a Ta
//Displays Request status(Accepted,Rejected,Pending)
public class Requests_To_Book_Ta_Fragment extends Fragment {

    View view;
    String TAG = "Requests_To_Book_Ta_Fragment";
    CollectionReference cr = FirebaseFirestore.getInstance().collection("BookMyTa/BookMyTaDocument/Requests");
    boolean isInitialData = true;
    boolean isDataRemoved = false;
    boolean isDataAdded = false;
    List<Integer> localIndexList = new ArrayList<>();
    RV_Adaptor_2 rvAdaptor;
    common_util cu = new common_util();

    public Requests_To_Book_Ta_Fragment() {
    }

    List<Map<String, Object>> DisplayRequest() {
        final List<Map<String, Object>> maps = new ArrayList<>();

        cr.orderBy("latestUpdateTimestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {

                if (e != null) {
                    Log.i(TAG, "Listen failed.", e);
                    return;
                }
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                  //Check whther it is the first time data is recivied from Firebase.
                    if (isInitialData)
                    {
                        //checks if the request is for the logged in user(may or may not be a TA but is a Student)
                        if (dc.getDocument().get("StudentName").toString().equals(cu.getUserDataLocally(getContext(), "name"))) {
                            maps.add(dc.getDocument().getData());
                            localIndexList.add(dc.getNewIndex());
                            DataAddORRemove();
                        }
                    } else {

                        switch (dc.getType()) {
                            case ADDED:
                                if (dc.getDocument().get("StudentName").toString().equals(cu.getUserDataLocally(getContext(), "name"))) {
                                    maps.add(dc.getDocument().getData());
                                    localIndexList.add(dc.getNewIndex());//adds Firebase's recieved data's index number to a localIndexList array.
                                    DataAddORRemove();
                                    isDataAdded = true;

                                }
                                break;
                            case REMOVED:
                                if (dc.getDocument().get("StudentName").toString().equals(cu.getUserDataLocally(getContext(), "name"))) {
                                    //Common loop for both REMOVED and MODIFIED cases
                                    //It gets the corresponding localIndex of Firebase's Index
                                    //For exmaple 5th index of firebase's data is stored locally on 2nd index of maps
                                    //so it returns 2
                                    for (int i = 0; i < localIndexList.size(); i++) {
                                        if (localIndexList.get(i) == dc.getOldIndex()) {
                                            maps.remove(i);
                                            localIndexList.remove(i);
                                        }
                                    }
                                    DataAddORRemove();
                                   isDataRemoved = true;

                                }
                                break;
                            case MODIFIED:
                                //Additional check that modified only executes when neither ADD or REMOVES executes
                                if (dc.getDocument().get("StudentName").toString().equals(cu.getUserDataLocally(getContext(), "name")) &&  isDataAdded == false &&  isDataRemoved == false) {
                                    for (int i = 0; i < localIndexList.size(); i++) {
                                        if (localIndexList.get(i) == dc.getOldIndex()) {
                                            maps.set(i, dc.getDocument().getData());
                                            UpdateStatus(i);
                                        }
                                    }
                                    isDataAdded = false;
                                    isDataRemoved = false;

                                }
                                break;
                        }
                    }

                }
                isInitialData = false;


            }

        });

        return maps;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//Simple Recycler View Set up.
        view = inflater.inflate(R.layout.requests_to_book_ta, container, false);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.request_to_book_ta_rv);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvAdaptor = new RV_Adaptor_2(DisplayRequest());

        rv.setAdapter(rvAdaptor);


        return view;

    }
//executes when Request Status is changed
    public void UpdateStatus(int pos) {
        rvAdaptor.notifyItemChanged(pos);

    }
    //executes when data is added or removed from FireBase
    public void DataAddORRemove() {
        rvAdaptor.notifyDataSetChanged();

    }


}








