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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btb.nixorstudentapplication.R;
import com.btb.nixorstudentapplication.Sharks_on_cloud.ImageViewver.ImageViewer;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Objects.BucketDataObject;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Soc_Main;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BucketData_Adaptor extends RecyclerView.Adapter<BucketData_Adaptor.Rv_ViewHolder> {
    private ArrayList<BucketDataObject> bucketDataObjects;
    private ArrayList<String> photoUrls;


    public BucketData_Adaptor(ArrayList<BucketDataObject> bucketDataObjects, ArrayList<String> photoUrls) {
        this.bucketDataObjects = bucketDataObjects;
        this.photoUrls = photoUrls;
    }


    @NonNull
    @Override
    public BucketData_Adaptor.Rv_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_bucketdata, parent, false);
        return new Rv_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Rv_ViewHolder holder, int position) {
        holder.contentName.setText(bucketDataObjects.get(position).getName());
        if (bucketDataObjects.get(position).isFolder()) {
            Picasso.get()
                    .load(R.drawable.foldericon)
                    .error(R.drawable.cie_grades)
                    .into(holder.contentImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            holder.loading.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.loading.setVisibility(View.INVISIBLE);
                        }
                    });
        } else {

            Picasso.get()
                    .load(bucketDataObjects.get(position).getPhotoUrl())
                    .error(R.drawable.cie_grades)
                    .into(holder.contentImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            holder.loading.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.loading.setVisibility(View.INVISIBLE);
                        }
                    });

        }

    }

    @Override
    public int getItemCount() {
        return bucketDataObjects.size();
    }


    class Rv_ViewHolder extends RecyclerView.ViewHolder {
        TextView contentName;
        ImageView contentImage;
        RelativeLayout relativeLayout;
        ProgressBar loading;

        public Rv_ViewHolder(View itemView) {
            super(itemView);
            contentName = itemView.findViewById(R.id.content_name_soc);
            contentImage = itemView.findViewById(R.id.content_image_soc);
            loading=itemView.findViewById(R.id.progressBar_bucketdata_eachitem);
            relativeLayout = itemView.findViewById(R.id.BucketDataLayout);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if(photoUrls.get(getAdapterPosition())!=null){
                        ImageViewer imageViewer = new ImageViewer(photoUrls, getAdapterPosition());
                        imageViewer.startActivity(Soc_Main.context);
                    }
                   }
            });
        }

    }

}

