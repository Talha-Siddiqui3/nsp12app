package com.btb.nixorstudentapplication.Sharks_on_cloud.Adaptors;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Navigation_Classes.BucketData;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Soc_Main;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class Buckets_Adaptor extends RecyclerView.Adapter<Buckets_Adaptor.Rv_ViewHolder> implements View.OnClickListener {
    ArrayList<String> bucketNames;
    common_util cu=new common_util();





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
    public void onBindViewHolder(@NonNull final Rv_ViewHolder holder, int position) {
       holder.studentName.setOnClickListener(this);
        if(position==0){
            holder.studentName.setText("MyBucket");
            Glide.with(Soc_Main.context).load(R.drawable.redbucket).into(holder.studentPhoto);
        }
        else {
            final String tempName = bucketNames.get(position);
            holder.studentName.setText(tempName);
            Soc_Main.usersRoot.document(tempName).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String photourl=documentSnapshot.get("photourl").toString();
                    Log.i("ASDASDADASDASD",photourl);
                    Glide.with(Soc_Main.context).load(photourl).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).into(holder.studentPhoto);

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            cu.ToasterLong(Soc_Main.context, "Failed to load" + tempName + "photo");
                        }
                    });
        }
    }



    @Override
    public int getItemCount() {
        return bucketNames.size();
    }


    @Override
    public void onClick(View view) {
        TextView tempUserName = (TextView) view;
        Soc_Main.isCurrentlyRunning="BucketData";//To provide functionality for OnBackPressed;
        Soc_Main.ClearData();// cleaing previous data of adadptor
        BucketData bucketData=new BucketData(Soc_Main.context,tempUserName.getText().toString());
    }


    class Rv_ViewHolder extends RecyclerView.ViewHolder {
        TextView studentName;
        ImageView studentPhoto;


        public Rv_ViewHolder(View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.student_name_soc);
studentPhoto=itemView.findViewById(R.id.student_imgae_soc);


        }

    }

}
