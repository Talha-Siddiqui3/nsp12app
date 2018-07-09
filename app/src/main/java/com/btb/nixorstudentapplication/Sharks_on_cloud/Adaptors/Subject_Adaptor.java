package com.btb.nixorstudentapplication.Sharks_on_cloud.Adaptors;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.btb.nixorstudentapplication.R;

import java.util.ArrayList;


public class Subject_Adaptor extends RecyclerView.Adapter<Subject_Adaptor.Rv_ViewHolder> {
    ArrayList<String> SubjectNames;


    public Subject_Adaptor( ArrayList<String> SubjectNames){
        this.SubjectNames=SubjectNames;
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
    }


    @Override
    public int getItemCount() {
        return SubjectNames.size();
    }


    class Rv_ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectname;

        public Rv_ViewHolder(View itemView) {
            super(itemView);
            subjectname = itemView.findViewById(R.id.subjectname_soc_textview);


        }

    }

}