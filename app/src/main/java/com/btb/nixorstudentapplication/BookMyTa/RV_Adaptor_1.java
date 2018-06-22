package com.btb.nixorstudentapplication.BookMyTa;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.btb.nixorstudentapplication.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RV_Adaptor_1 extends RecyclerView.Adapter<RV_Adaptor_1.Rv_ViewHolder> {
    List<String> StudentName = new ArrayList<>();
    List<String> DocIds;
    Button AcceptRequest;
    Button RejectRequest;
    String TAG = "RV_Adaptor_1";

    RV_Adaptor_1(List<String> StudentName, List<String> DocIds) {
        this.StudentName = StudentName;
        this.DocIds = DocIds;
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

    //NOT WORKING EVEN AFTER IMPLEMENTING
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
            txt = itemView.findViewById(R.id.Student_Name);
            AcceptRequest = itemView.findViewById(R.id.Accept_Request);
            RejectRequest = itemView.findViewById(R.id.Reject_Request);
            AcceptRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Student_Requests_For_Ta_Fragment.UpdateRequest("Accepted", DocIds.get(getAdapterPosition()));
                    Log.i(TAG, "CHALING 1");
                }
            });
            RejectRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Student_Requests_For_Ta_Fragment.UpdateRequest("Rejected", DocIds.get(getAdapterPosition()));
                    Log.i(TAG, "CHALING 2");

                }
            });


        }
    }

}