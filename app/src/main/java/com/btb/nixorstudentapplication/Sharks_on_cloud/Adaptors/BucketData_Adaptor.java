package com.btb.nixorstudentapplication.Sharks_on_cloud.Adaptors;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.btb.nixorstudentapplication.Sharks_on_cloud.MyBucket;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Objects.BucketDataObject;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Soc_Main;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BucketData_Adaptor extends RecyclerView.Adapter<BucketData_Adaptor.Rv_ViewHolder> {
    private ArrayList<BucketDataObject> bucketDataObjects;
    private ArrayList<String> photoUrlsForImageViewver;
    private Activity context;


    public BucketData_Adaptor(ArrayList<BucketDataObject> bucketDataObjects, ArrayList<String> photoUrlsForImageViewver, Activity context) {
        this.bucketDataObjects = bucketDataObjects;
        this.photoUrlsForImageViewver = photoUrlsForImageViewver;
        this.context = context;
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
                    .error(R.drawable.ic_error_outline)
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

               Picasso.get().setLoggingEnabled(true);
                Picasso.get()
                        .load(bucketDataObjects.get(position).getPhotoUrlThumbnail())
                        .error(R.drawable.ic_error_outline)
                        .into(holder.contentImage, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                holder.loading.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onError(Exception e) {
                                holder.loading.setVisibility(View.INVISIBLE);
                                Log.i("123", e.toString());
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
            loading = itemView.findViewById(R.id.progressBar_bucketdata_eachitem);
            relativeLayout = itemView.findViewById(R.id.BucketDataLayout);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (photoUrlsForImageViewver.get(getAdapterPosition()) != null) {
                        ImageViewer imageViewer = new ImageViewer(photoUrlsForImageViewver, getAdapterPosition());
                        imageViewer.startActivity(context);
                    }
                }
            });
        }

    }

}

