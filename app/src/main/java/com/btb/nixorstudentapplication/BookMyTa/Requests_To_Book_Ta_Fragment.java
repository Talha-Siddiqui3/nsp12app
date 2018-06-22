package com.btb.nixorstudentapplication.BookMyTa;

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
import android.widget.EditText;
import android.widget.TextView;

import com.btb.nixorstudentapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Requests_To_Book_Ta_Fragment extends Fragment {

    View view;

    String TAG = "Requests_To_Book_Ta_Fragment";
    List<Map<String, Object>> maps = new ArrayList<>();
    CollectionReference cr = FirebaseFirestore.getInstance().collection("BookMyTa/BookMyTaDocument/Requests");
    boolean initial = true;
    RV_Adaptor_2 rvAdaptor = new RV_Adaptor_2(DisplayRequest());


    public Requests_To_Book_Ta_Fragment() {
    }

    public List<Map<String, Object>> DisplayRequest() {


        cr.addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {

                int x = 0;
                if (e != null) {
                    Log.i(TAG, "Listen failed.", e);
                    return;
                }
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    if (initial) {
                        maps.add(dc.getDocument().getData());
                    } else {
                        switch (dc.getType()) {
                            case ADDED:
                                maps.add(dc.getDocument().getData());
                                AddData();
                                break;
                            case REMOVED:
                                break;
                            case MODIFIED:
                                maps.set(Integer.parseInt(dc.getDocument().getString("StatusId")), dc.getDocument().getData());
                              UpdateStatus(Integer.parseInt(dc.getDocument().getString("StatusId")));
                              Log.i(TAG,dc.getDocument().getString("StatusId"));
                                AddData();
                              break;


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

    public void AddData() {
        rvAdaptor.notifyDataSetChanged();
    }


}








