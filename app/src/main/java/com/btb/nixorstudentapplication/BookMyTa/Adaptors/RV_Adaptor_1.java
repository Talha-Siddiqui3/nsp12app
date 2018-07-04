package com.btb.nixorstudentapplication.BookMyTa.Adaptors;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.btb.nixorstudentapplication.BookMyTa.Fragments_for_tabs.Student_Requests_For_Ta_Fragment;
import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//ADAPTOR FOR Student_Requests_For_Ta_Fragment
public class RV_Adaptor_1 extends RecyclerView.Adapter<RV_Adaptor_1.Rv_ViewHolder> {
    List<String> StudentName = new ArrayList<>();
    List<String> DocIds;
    Button AcceptRequest;
    Button RejectRequest;
    TextView RequestStatus;
    LinearLayout buttonLayout;
    LinearLayout statusLayout;
    String TAG = "RV_Adaptor_1";
    String RequestStatusText;
    common_util cu = new common_util();
    private FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
    Activity context;


    public RV_Adaptor_1(List<String> StudentName, List<String> DocIds, Activity context) {
        this.StudentName = StudentName;
        this.DocIds = DocIds;
        this.context = context;
    }

    @NonNull
    @Override
    public Rv_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.student_request_for_ta_layout, parent, false);
        return new Rv_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Rv_ViewHolder holder, int position) {
        holder.txt.setText(StudentName.get(position));

    }

    @Override
    public int getItemCount() {
        return StudentName.size();
    }

    //NOT WORKING EVEN AFTER IMPLEMENTING OnClick Listener
  /*  @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Accept_Request:
                Student_Requests_For_Ta_Fragment.UpdateRequest("Accepted");
                Log.i(TAG,"CHALING");
                break;
            case R.id.Reject_Request:
                Student_Requests_For_Ta_Fragment.UpdateRequest("Rejected");
        }
    }
    */
    class Rv_ViewHolder extends RecyclerView.ViewHolder {
        TextView txt;

        public Rv_ViewHolder(View itemView) {
            super(itemView);
            RequestStatusText = "Pending";
            txt = itemView.findViewById(R.id.Student_Name);
            AcceptRequest = itemView.findViewById(R.id.Accept_Request);
            RejectRequest = itemView.findViewById(R.id.Reject_Request);
            RequestStatus = itemView.findViewById(R.id.Request_Status_student_request_for_ta);
            RequestStatus.setText(RequestStatusText);
            buttonLayout = itemView.findViewById(R.id.linearlayout_for_buttons_bookmyta);
            statusLayout = itemView.findViewById(R.id.linearlayout_for_status_bookmyta);
            //Both buttons return status and corresponding DocId for selected request

            AcceptRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UpdateRequest("Accepted", DocIds.get(getAdapterPosition()));
                    AcceptRequest.setEnabled(false);
                    RejectRequest.setEnabled(false);
                    buttonLayout.setVisibility(View.GONE);
                    statusLayout.setVisibility(View.VISIBLE);
                }
            });
            RejectRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UpdateRequest("Rejected", DocIds.get(getAdapterPosition()));
                    AcceptRequest.setEnabled(false);
                    RejectRequest.setEnabled(false);
                    buttonLayout.setVisibility(View.GONE);
                    statusLayout.setVisibility(View.VISIBLE);
                }
            });


        }


    }

    public void UpdateRequest(final String requestStatus, String DocId) {
        // Create the arguments to the callable function.

        Map<String, Object> data = new HashMap<>();

        data.put("newdocid", DocId);
        data.put("status", requestStatus);
        Log.i(TAG, "method executed");

        mFunctions
                .getHttpsCallable("callableFunctionandroid")
                .call(data)
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                        Map<String, Object> recieveddata = new HashMap<>();
                        recieveddata = ((HashMap<String, Object>) httpsCallableResult.getData());
                        Log.i(TAG, recieveddata.get("RequestStatus").toString());
                        cu.ToasterLong(context, recieveddata.get("RequestStatus").toString());
                        RequestStatus.setText (recieveddata.get("RequestStatus").toString());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "FAIlED");
                        cu.ToasterLong(context, "Failed to connect to server");
                        RequestStatus.setText ("Failed to connect to server");

                    }
                });


    }
}