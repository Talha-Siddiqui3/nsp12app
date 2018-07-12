package com.btb.nixorstudentapplication.Sharks_on_cloud.Adaptors;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Soc_Main;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class Names_Adaptor extends RecyclerView.Adapter<Names_Adaptor.Rv_ViewHolder> implements View.OnClickListener {
    private ArrayList<String> studentNames;
    private common_util cu = new common_util();

    public Names_Adaptor( ArrayList<String> studentNames) {

        this.studentNames = studentNames;
    }


    @NonNull
    @Override
    public Rv_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_studentname_soc, parent, false);
        return new Rv_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Rv_ViewHolder holder, int position) {
      if(position==0){
            holder.studentName.setText("MyBucket");
           Glide.with(Soc_Main.context).load(R.drawable.redbucket).into(holder.studentPhoto);
        }
       else {
           final String tempName = studentNames.get(position);
            holder.studentName.setText(tempName);
            Soc_Main.usersRoot.document(tempName).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
               @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String photourl=documentSnapshot.get("photourl").toString();
               Glide.with(Soc_Main.context).load(photourl).into(holder.studentPhoto);
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
        return studentNames.size();
    }


    @Override
    public void onClick(View view) {
        // Soc_Main.isCurrentlyRunning="Names";//To provide functionality for OnBackPressed;
        //Soc_Main.ClearData();// cleaing previous data of adadptor
        //Names names = new Names(Soc_Main.context,Soc_Main.v);//naviagating to new page through a new class
    }


    class Rv_ViewHolder extends RecyclerView.ViewHolder {
        TextView studentName;
        ImageView studentPhoto;


        public Rv_ViewHolder(View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.student_name_soc);
            studentPhoto = itemView.findViewById(R.id.student_imgae_soc);

        }

    }

}


