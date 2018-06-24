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


public class Requests_To_Book_Ta_Fragment extends Fragment {

    View view;

    String TAG = "Requests_To_Book_Ta_Fragment";

    CollectionReference cr = FirebaseFirestore.getInstance().collection("BookMyTa/BookMyTaDocument/Requests");
    boolean initial = true;
    boolean removed = false;
    boolean added = false;
    List<Integer> localIndexList = new ArrayList<>();
    RV_Adaptor_2 rvAdaptor = new RV_Adaptor_2(DisplayRequest());
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
                    if (initial) {
//NOT WORKING IDK WHY              if (dc.getDocument().get("StudentName").toString().equals(cu.getUserDataLocally(getContext(),"name"))) {
                       if (dc.getDocument().get("StudentName").toString().equals("Muhammad Talha Siddiqui")) {
                            maps.add(dc.getDocument().getData());
                            localIndexList.add(dc.getNewIndex());
                       }
                    } else {

                        switch (dc.getType()) {
                            case ADDED:
                                //NOT WORKING IDK WHY              if (dc.getDocument().get("StudentName").toString().equals(cu.getUserDataLocally(getContext(),"name")))
                                if (dc.getDocument().get("StudentName").toString().equals("Muhammad Talha Siddiqui")) {
                                    maps.add(dc.getDocument().getData());
                                    localIndexList.add(dc.getNewIndex());
                                    DataAddORRemove();
                                    added = true;
                                    break;
                                }
                            case REMOVED:
//NOT WORKING IDK WHY              if (dc.getDocument().get("StudentName").toString().equals(cu.getUserDataLocally(getContext(),"name")))
                                if (dc.getDocument().get("StudentName").toString().equals("Muhammad Talha Siddiqui")) {

                                    for (int i = 0; i < localIndexList.size(); i++) {
                                        if (localIndexList.get(i) == dc.getOldIndex()) {
                                            maps.remove(i);
                                            localIndexList.remove(i);
                                        }
                                    }
                                    DataAddORRemove();
                                    removed = true;
                                    break;
                                }
                            case MODIFIED:
                               if (dc.getDocument().get("StudentName").toString().equals(cu.getUserDataLocally(getContext(), "name")) && added == false && removed == false) {
                                    for (int i = 0; i < localIndexList.size(); i++) {
                                        if (localIndexList.get(i) == dc.getOldIndex()) {
                                            maps.set(i, dc.getDocument().getData());
                                            UpdateStatus(i);
                                        }
                                    }
                                    added = false;
                                    removed = false;
                                    break;
                               }
                        }
                    }

                }
                initial = false;


            }

        });

        return maps;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.requests_to_book_ta, container, false);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.request_to_book_ta_rv);

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(rvAdaptor);


        return view;

    }

    public void UpdateStatus(int pos) {
        rvAdaptor.notifyItemChanged(pos);

    }

    public void DataAddORRemove() {
        rvAdaptor.notifyDataSetChanged();

    }


}








