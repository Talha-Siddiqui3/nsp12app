package com.btb.nixorstudentapplication.BookMyTa;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.btb.nixorstudentapplication.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Student_Requests_For_Ta_Fragment extends Fragment {
    String TAG="Student_Requests_For_Ta_Fragment";
    View view;
    public Student_Requests_For_Ta_Fragment() {
    }

    public void GetRequest(){
        final CollectionReference cr = FirebaseFirestore.getInstance().collection("BookMyTa");
        ListenerRegistration listenerRegistration = cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot value, FirebaseFirestoreException e) {
                List<String> cities = new ArrayList<>();
                for (DocumentSnapshot doc : value) {
                    if (doc.get("StudentName") != null) {
                        cities.add(doc.get("StudentName").toString());
                    }
                }
                Log.i(TAG, cities+" Booked you");
            }

        });
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.student_requests_for_ta,container,false);
GetRequest();
        return view;
    }






}

