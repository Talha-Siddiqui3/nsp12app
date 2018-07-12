package com.btb.nixorstudentapplication.Sharks_on_cloud.Adaptors;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.btb.nixorstudentapplication.R;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Navigation_Classes.Names;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Soc_Main;

import java.util.ArrayList;

public class Buckets_Adaptor extends RecyclerView.Adapter<Buckets_Adaptor.Rv_ViewHolder> implements View.OnClickListener {
    ArrayList<String> ClassesNames;





    public Buckets_Adaptor(ArrayList<String> ClassesNames) {
        this.ClassesNames = ClassesNames;
    }


    @NonNull
    @Override
    public Rv_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_classes_soc, parent, false);
        return new Rv_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Rv_ViewHolder holder, int position) {
        holder.className.setText(ClassesNames.get(position));
        holder.className.setOnClickListener(this);
    }


    @Override
    public int getItemCount() {
        return ClassesNames.size();
    }


    @Override
    public void onClick(View view) {
        TextView tempClassesName = (TextView) view;
        Soc_Main.isCurrentlyRunning="Names";//To provide functionality for OnBackPressed;
        Soc_Main.ClearData();// cleaing previous data of adadptor
        Names names = new Names(Soc_Main.context,Soc_Main.v,tempClassesName.getText().toString());//naviagating to new page through a new class and passing clicked subject name
    }


    class Rv_ViewHolder extends RecyclerView.ViewHolder {
        TextView className;


        public Rv_ViewHolder(View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.classname_soc_textview);



        }

    }

}
