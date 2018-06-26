package com.btb.nixorstudentapplication.Past_papers.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.Past_papers.Objects.paperObject;
import com.btb.nixorstudentapplication.Past_papers.PdfLoad;
import com.btb.nixorstudentapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class multiView_adapter extends RecyclerView.Adapter<multiView_adapter.Rv_ViewHolder>  {

    public static ArrayList<paperObject> data;
    public static ArrayList<String> ActualNames;
    Context activity;

    //Constructor nothing more
    public multiView_adapter(ArrayList<paperObject> pastpapers, Context context, ArrayList<String> ActualNames) {
        data = pastpapers;
        activity = context;
        this.ActualNames = ActualNames;

    }

    //Here you play with the views
    @Override
    public void onBindViewHolder(@NonNull Rv_ViewHolder holder, final int position) {
        validateData(holder.type, data.get(position).getType(), holder.ratingBar);
        validateData(holder.year, data.get(position).getYear(), holder.ratingBar);
        validateData(holder.variant, data.get(position).getVariant(), holder.ratingBar);
        validateData(holder.month, data.get(position).getMonth(), holder.ratingBar);

        switch (data.get(position).getMonth()) {
            case "Winter":

                holder.decor_paper.setImageResource(R.drawable.snow_flake);
                break;
            case "Summer":

                holder.decor_paper.setImageResource(R.drawable.summer_icon);
                break;
            case "March":

                holder.decor_paper.setImageResource(R.drawable.spring_icon);
                break;

        }


        setRatingValue(data.get(position).getRating(), holder.ratingBar);

    }
    class Rv_ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout linearLayout;
        TextView year;
        TextView month;
        TextView variant;
        TextView type;
        RatingBar ratingBar;
        ImageView decor_paper;


        public Rv_ViewHolder(View itemView) {
            super(itemView);
            year = itemView.findViewById(R.id.year_textView);
            month = itemView.findViewById(R.id.month_textView);
            variant = itemView.findViewById(R.id.variant_textView);
            type = itemView.findViewById(R.id.type_textView);
            decor_paper = itemView.findViewById(R.id.decor_paper);
            ratingBar = itemView.findViewById(R.id.ratingBar);


            linearLayout = itemView.findViewById(R.id.paperlayout);

        }
    }



    //Makes all those error fields disappear so that your bitchy user likes the UI
    public void validateData(TextView value, String data, RatingBar ratingBar) {
        //Atleast one should not be error
        if (data.equals("error")) {

            value.setVisibility(View.INVISIBLE);

        } else {
            value.setText(data);
            ratingBar.setVisibility(View.VISIBLE);
        }
    }

    //Yeah this needs to be changed. Will write a library for rating. So don't you worry about that
    public void setRatingValue(float ratingValue, RatingBar ratingBar) {
        if (ratingValue != -1) {
            ratingBar.setRating(ratingValue);

        } else {
            ratingBar.setVisibility(View.INVISIBLE);


        }
    }

    //Moving on..





    //All the methods that I have never bothered about
    @NonNull
    @Override
    public Rv_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_paper, parent, false);
        return new Rv_ViewHolder(view);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

}