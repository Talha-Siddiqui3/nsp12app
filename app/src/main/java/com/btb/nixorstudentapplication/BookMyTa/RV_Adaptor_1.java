package com.btb.nixorstudentapplication.BookMyTa;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import com.btb.nixorstudentapplication.R;

import java.util.ArrayList;
import java.util.List;

public class RV_Adaptor_1 extends RecyclerView.Adapter<RV_Adaptor_1.Rv_ViewHolder> {
    List<String> StudentName=new ArrayList<>();

    RV_Adaptor_1(List<String> StudentName){
        this.StudentName=StudentName;
    }

    @NonNull
    @Override
    public Rv_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.student_request_for_ta_layout, parent, false);
        return new Rv_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RV_Adaptor_1.Rv_ViewHolder holder, int position) {
holder.txt.setText(StudentName.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class Rv_ViewHolder extends RecyclerView.ViewHolder {
        EditText txt;

        public Rv_ViewHolder(View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.Student_Name);


        }
    }

}