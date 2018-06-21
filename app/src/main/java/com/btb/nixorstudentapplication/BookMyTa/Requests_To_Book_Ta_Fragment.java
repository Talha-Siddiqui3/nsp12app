package com.btb.nixorstudentapplication.BookMyTa;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Requests_To_Book_Ta_Fragment extends Fragment {

    View view;
    TextView statusText;
    TextView TaNameText;
    String TAG = "Requests_To_Book_Ta_Fragment";
    String TaName;
    String Status;
    CollectionReference cr = FirebaseFirestore.getInstance().collection("BookMyTa/BookMyTaDocument/Requests");

    public Requests_To_Book_Ta_Fragment() {
    }

    public void DisplayRequest() {

        Query myQuery = cr.whereEqualTo("TaName", "Hassan");
        myQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            Map<String, Object> map = new HashMap<>();
            @Override
            public void onEvent(QuerySnapshot value, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                for (DocumentSnapshot document : value) {
                    if (document.getData() != null) {
                        map = document.getData();
                        Status = map.get("Status").toString();
                        TaName = map.get("TaName").toString();
                        statusText.setText(Status);
                        TaNameText.setText(TaName);

                    }
                }






            }
        }) ;


    }








    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.requests_to_book_ta, container, false);
        statusText = view.findViewById(R.id.Request_Status);
        TaNameText = view.findViewById(R.id.Ta_Name);

        DisplayRequest();


        return view;

    }
}








