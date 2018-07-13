package com.btb.nixorstudentapplication.Sharks_on_cloud.Adaptors;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.btb.nixorstudentapplication.R;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Objects.BucketDataObject;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Soc_Main;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

public class BucketData_Adaptor extends RecyclerView.Adapter<BucketData_Adaptor.Rv_ViewHolder>  {
    private ArrayList<BucketDataObject> bucketDataObjects;




    public BucketData_Adaptor(ArrayList<BucketDataObject> bucketDataObjects) {
        this.bucketDataObjects=bucketDataObjects;
    }


    @NonNull
    @Override
    public BucketData_Adaptor.Rv_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
   View view = inflater.inflate(R.layout.item_bucketdata, parent, false);
        return new Rv_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Rv_ViewHolder holder, int position) {
        holder.contentName.setText(bucketDataObjects.get(position).getName());
   if(bucketDataObjects.get(position).isFolder()) {
       Glide.with(Soc_Main.context).load(R.drawable.foldericon).into(holder.contentImage);
   }
   else{
       Glide.with(Soc_Main.context).load(bucketDataObjects.get(position).getPhotoUrl()).into(holder.contentImage);

   }

    }

    @Override
    public int getItemCount() {
        return bucketDataObjects.size();
    }





    class Rv_ViewHolder extends RecyclerView.ViewHolder {
       TextView contentName;
       ImageView contentImage;
        public Rv_ViewHolder(View itemView) {
            super(itemView);
contentName =itemView.findViewById(R.id.content_name_soc);
contentImage=itemView.findViewById(R.id.content_image_soc);
        }

    }

}

