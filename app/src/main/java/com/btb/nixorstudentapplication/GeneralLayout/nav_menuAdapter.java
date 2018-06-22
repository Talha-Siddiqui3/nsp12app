package com.btb.nixorstudentapplication.GeneralLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.Past_papers.PdfLoad;
import com.btb.nixorstudentapplication.Past_papers.paperObject;
import com.btb.nixorstudentapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class nav_menuAdapter extends RecyclerView.Adapter<nav_menuAdapter.Rv_ViewHolder> {
    common_util common_util = new common_util();
    Context activity;

    ArrayList<String> allitems;

    public nav_menuAdapter(ArrayList<String> items, Context context){

        activity=context;
        allitems =items;
    }


    @NonNull
    @Override
    public Rv_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.nav_item,parent,false);
        return new Rv_ViewHolder(view); }




    @Override
    public void onBindViewHolder(@NonNull Rv_ViewHolder holder, final int position) {
        Log.i("yo","Got here");
        switch (allitems.get(position)){
            case "Home":holder.item_icon.setImageResource(R.drawable.home); holder.item_name.setText(activity.getString(R.string.Home));Log.i("yo","Home");break;
            case "Pastpapers":holder.item_icon.setImageResource(R.drawable.pastpapers); holder.item_name.setText(activity.getString(R.string.Pastpapers));break;
            case "Carpool":holder.item_icon.setImageResource(R.drawable.carpool); holder.item_name.setText(activity.getString(R.string.Carpool));break;
            case "Portal":holder.item_icon.setImageResource(R.drawable.portal); holder.item_name.setText(activity.getString(R.string.Portal));break;
            case "Soc":holder.item_icon.setImageResource(R.drawable.soc); holder.item_name.setText(activity.getString(R.string.Soc));break;
            case "Bookmyta":holder.item_icon.setImageResource(R.drawable.bookmyta); holder.item_name.setText(activity.getString(R.string.Bookmyta));break;


        }

    }

    @Override
    public int getItemCount() {
        return allitems.size();
    }

    class Rv_ViewHolder extends RecyclerView.ViewHolder{
        ImageView item_icon;
        TextView item_name;





        public Rv_ViewHolder(View itemView) {
            super(itemView);

        item_icon = itemView.findViewById(R.id.item_icon);
        item_name = itemView.findViewById(R.id.item_name);
        }
    }



}
