package com.btb.nixorstudentapplication.Sharks_on_cloud.Adaptors;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.btb.nixorstudentapplication.R;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Soc_Main;

import java.util.ArrayList;

public class Buckets_Adaptor extends RecyclerView.Adapter<Buckets_Adaptor.Rv_ViewHolder> implements View.OnClickListener {
    ArrayList<String> bucketNames;





    public Buckets_Adaptor(ArrayList<String> bucketNames) {
        this.bucketNames = bucketNames;
    }


    @NonNull
    @Override
    public Rv_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_buckets, parent, false);
        return new Rv_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Rv_ViewHolder holder, int position) {
        holder.studentName.setText(bucketNames.get(position));
        holder.studentName.setOnClickListener(this);
    }


    @Override
    public int getItemCount() {
        return bucketNames.size();
    }


    @Override
    public void onClick(View view) {
        TextView tempClassesName = (TextView) view;
        Soc_Main.isCurrentlyRunning="Names";//To provide functionality for OnBackPressed;
        Soc_Main.ClearData();// cleaing previous data of adadptor

    }


    class Rv_ViewHolder extends RecyclerView.ViewHolder {
        TextView studentName;


        public Rv_ViewHolder(View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.student_name_soc);



        }

    }

}
