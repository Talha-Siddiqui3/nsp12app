package com.btb.nixorstudentapplication.BookMyTa;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.Past_papers.paperObject;
import com.btb.nixorstudentapplication.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Search_Ta_Fragment extends Fragment {
    View view;
    static CollectionReference cr = FirebaseFirestore.getInstance().collection("BookMyTa/BookMyTaDocument/TA Details");
    RV_Adaptor_3_For_Search_Ta rvAdaptor = new RV_Adaptor_3_For_Search_Ta(DisplayTa());
    boolean initial = true;
    Map<String, Object> map = new HashMap();
    String TAG = "Search_Ta_Fragment";
    boolean initialAddition;

    public Search_Ta_Fragment() {
    }


    public List<TA_Object> DisplayTa() {
        final List<TA_Object> ta_objects = new ArrayList<>();

        cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {
                initialAddition=true;
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    TA_Object ta_object = new TA_Object();
                    if (initial) {
                        map = dc.getDocument().getData();
                        Log.i(TAG, map.toString());
                        ta_objects.add(addDataToObject(ta_object, map.get("Name"), map.get("Days"), map.get("TaID"), map.get("Timings"), map.get("Subject")));
                    }
                 else {
                        switch (dc.getType()) {
                            case ADDED:

                                map = dc.getDocument().getData();
                                addDataToObject(ta_object, map.get("Name"), map.get("Days"), map.get("TaID"), map.get("Timings"), map.get("Subject"));
                                AddDataToAdaptor();
                                break;

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

        return ta_objects;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.search_ta, container, false);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.search_ta_rv);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        rv.setAdapter(rvAdaptor);
        return view;
    }


    public void AddDataToAdaptor() {

        rvAdaptor.notifyDataSetChanged();


    }

    public TA_Object addDataToObject(TA_Object ta_object, Object Ta_Name, Object Days, Object TaId, Object Timings, Object Subject) {
        ta_object.setDays(Days.toString());
        ta_object.setSubject(Subject.toString());
        ta_object.setTaID(TaId.toString());
        ta_object.setTimings(Timings.toString());
        ta_object.setTaName(Ta_Name.toString());
        return ta_object;
    }
}
