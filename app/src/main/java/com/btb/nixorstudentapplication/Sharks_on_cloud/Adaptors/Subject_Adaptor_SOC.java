package com.btb.nixorstudentapplication.Sharks_on_cloud.Adaptors;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.btb.nixorstudentapplication.R;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Navigation_Classes.Buckets;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Soc_Main;

import java.util.ArrayList;


public class Subject_Adaptor_SOC extends RecyclerView.Adapter<Subject_Adaptor_SOC.Rv_ViewHolder> implements View.OnClickListener {
    ArrayList<String> SubjectNames;


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
        holder.subjectname.setOnClickListener(this);
    }


    @Override
    public int getItemCount() {
        return SubjectNames.size();
    }


    @Override
    public void onClick(View view) {
        Soc_Main.isCurrentlyRunning="Classes";//To provide functionality for OnBackPressed;
        Soc_Main.ClearData();// cleaing previous data of adadptor
        TextView tempSubjectName = (TextView) view;
        Buckets buckets = new Buckets(Soc_Main.context, Soc_Main.v, tempSubjectName.getText().toString());//naviagating to new page through a new class and passing clicked subject name

    }


    class Rv_ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectname;


        public Rv_ViewHolder(View itemView) {
            super(itemView);
            subjectname = itemView.findViewById(R.id.subjectname_soc_textview);


        }

    }

}