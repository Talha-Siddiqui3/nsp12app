package com.btb.nixorstudentapplication.GeneralLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.btb.nixorstudentapplication.Application_Home.home_screen;
import com.btb.nixorstudentapplication.Past_papers.MainPPActivity;
import com.btb.nixorstudentapplication.R;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Soc_Main;


import java.util.ArrayList;

public class nav_adapter extends RecyclerView.Adapter<nav_adapter.Rv_ViewHolder> {

    private ArrayList<String> allitems;


    Context activity;
    public nav_adapter(ArrayList items, Context context){
        allitems=items;
        activity= context;

    }


    @NonNull
    @Override
    public Rv_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.nav_item,parent,false);
        return new Rv_ViewHolder(view); }




    @Override
    public void onBindViewHolder(@NonNull Rv_ViewHolder holder, final int position) {


        switch (allitems.get(position)){
            case "Home":holder.item_icon.setImageResource(R.drawable.home);break;
            case "Pastpapers":holder.item_icon.setImageResource(R.drawable.pastpapers);break;
            case "Carpool":holder.item_icon.setImageResource(R.drawable.carpool);break;
            case "Chat":holder.item_icon.setImageResource(R.drawable.chat);break;
            case "Soc":holder.item_icon.setImageResource(R.drawable.soc); break;
            case "Bookmyta":holder.item_icon.setImageResource(R.drawable.bookmyta);break;


        }
        holder.item_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(activity instanceof home_screen){
                switch(allitems.get(position)){
                    case "Home":activity.startActivity(new Intent(activity,home_screen.class));break;
                    case "Pastpapers":activity.startActivity(new Intent(activity,MainPPActivity.class));break;
                    //case "Carpool":holder.item_icon.setImageResource(R.drawable.carpool);break;
                   // case "Chat":holder.item_icon.setImageResource(R.drawable.chat);break;
                    case "Soc":activity.startActivity(new Intent(activity,Soc_Main.class)); break;
                  //  case "Bookmyta":holder.item_icon.setImageResource(R.drawable.bookmyta);break;

                }

            }else{
                    switch(allitems.get(position)){
                        case "Home":activity.startActivity(new Intent(activity,home_screen.class));((Activity)activity).finish();break;
                        case "Pastpapers":activity.startActivity(new Intent(activity,MainPPActivity.class));((Activity)activity).finish();break;
                        //case "Carpool":holder.item_icon.setImageResource(R.drawable.carpool);break;
                        // case "Chat":holder.item_icon.setImageResource(R.drawable.chat);break;
                        case "Soc":activity.startActivity(new Intent(activity,Soc_Main.class)); ((Activity)activity).finish();break;
                        //  case "Bookmyta":holder.item_icon.setImageResource(R.drawable.bookmyta);break;

                    }

                }
            }
        });




    }

    @Override
    public int getItemCount() {
        return allitems.size();
    }

    class Rv_ViewHolder extends RecyclerView.ViewHolder{

        ImageView item_icon;






        public Rv_ViewHolder(View itemView) {
            super(itemView);

            item_icon = itemView.findViewById(R.id.item_icon);


        }
    }





}
