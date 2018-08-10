package com.btb.nixorstudentapplication.Sharks_on_cloud.Adaptors;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btb.nixorstudentapplication.R;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Navigation_Classes.Buckets;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Navigation_Classes.Subjects_homescreen;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Soc_Main;

import java.util.ArrayList;


public class Subject_Adaptor_SOC extends RecyclerView.Adapter<Subject_Adaptor_SOC.Rv_ViewHolder> implements View.OnClickListener {
    private ArrayList<String> SubjectNames;


    public Subject_Adaptor_SOC(ArrayList<String> SubjectNames) {
        this.SubjectNames = SubjectNames;
    }


    @NonNull
    @Override
    public Rv_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_subjects_soc, parent, false);
        return new Rv_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Rv_ViewHolder holder, int position) {
        holder.subjectname.setText(SubjectNames.get(position));
        holder.subjectRL.setOnClickListener(this);
    }


    @Override
    public int getItemCount() {
        return SubjectNames.size();
    }


    @Override
    public void onClick(View view) {
        Soc_Main.isCurrentlyRunning = "Buckets";//To provide functionality for OnBackPressed;
        Soc_Main.ClearDataRv();// cleaing previous data of adadptor
        RelativeLayout tempRL = (RelativeLayout) view;
        String yearSelected;
        TextView tempSubjectName = (TextView) tempRL.getChildAt(1);
        if( !Subjects_homescreen.button_Selected.equals("AS") && !Subjects_homescreen.button_Selected.equals("A2")) {
           yearSelected= Subjects_homescreen.mySubjectsWithYears.get(tempSubjectName.getText().toString());
        }
        else{
            yearSelected=Subjects_homescreen.button_Selected;
        }
        Buckets buckets = new Buckets(Soc_Main.context, Soc_Main.v, tempSubjectName.getText().toString(),yearSelected);//naviagating to new page through a new class and passing clicked subject name

    }


    class Rv_ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectname;
        RelativeLayout subjectRL;

        public Rv_ViewHolder(View itemView) {
            super(itemView);
            subjectname = itemView.findViewById(R.id.subjectname_soc_textview);
            subjectRL = itemView.findViewById(R.id.rl_subject_soc);

        }

    }

}