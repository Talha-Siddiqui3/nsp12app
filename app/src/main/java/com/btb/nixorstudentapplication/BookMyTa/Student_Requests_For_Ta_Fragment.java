package com.btb.nixorstudentapplication.BookMyTa;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.btb.nixorstudentapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
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

public class Student_Requests_For_Ta_Fragment extends Fragment  {
    String TAG = "Student_Requests_For_Ta_Fragment";
    View view;
    TextView Student_Name;
    Button AcceptRequest;
    Button RejectRequest;
    Boolean datarecieved = false;
CollectionReference cr = FirebaseFirestore.getInstance().collection("BookMyTa/BookMyTaDocument/Requests");

    public Student_Requests_For_Ta_Fragment() {
    }

    public void GetRequest() {

        cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot value, FirebaseFirestoreException e) {
                List<String> requests = new ArrayList<>();
                int x = 0;
                for (DocumentSnapshot doc : value) {
                    if (doc.get("StudentName") != null) {
                        requests.add(doc.get("StudentName").toString());
                        Student_Name.setText(requests.get(x));
                        Log.i(TAG, requests.get(x) + " Booked you");
                        x = x + 1;
                    }
                }

            }

        });
    }

/*onlick not working even after implementing View.onClickListener
    @Override
    public void onClick(View v) {
        if (datarecieved) {
            if (v == AcceptRequest) {
UpdateRequest("Accepted");
            Log.i(TAG,"ACCEPT");
            }
            if (v == RejectRequest) {
                UpdateRequest("Rejected");
            }

            Log.i(TAG,v.toString());
        }
    }
*/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.student_requests_for_ta, container, false);
        Student_Name = view.findViewById(R.id.Student_Name);
        AcceptRequest = view.findViewById(R.id.Accept_Request);
        AcceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"ACCEPT");
                UpdateRequest("Accepted");
            }
        });
        RejectRequest = view.findViewById(R.id.Reject_Request);
RejectRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"REJECT");
                UpdateRequest("Rejected");
            }
        });
      GetRequest();

        return view;
    }




public void UpdateRequest(final String requestStatus){
    Query myQuery = cr.whereEqualTo("TaName", "Hassan");
    myQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {


        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    if (document.getData() != null) {
String id=document.getId();
                        DocumentReference dr =cr.document(id);
                        dr.update("Status",requestStatus);


                    }
                }
            } else {
                Log.i(TAG, "ERROR");
            }

        }
    });

}
}