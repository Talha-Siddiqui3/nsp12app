package com.btb.nixorstudentapplication.PastPapers;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.btb.nixorstudentapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import static android.util.Log.i;

//Rv means Recycle View
public class RvAdaptor extends RecyclerView.Adapter<RvAdaptor.Rv_ViewHolder> {

    private ArrayList data;
    private ArrayList<String> ActualNames;
    Activity activity;
    public RvAdaptor(ArrayList<String> pastpapers, Activity context,ArrayList<String> ActualNames){
        data=pastpapers;
        activity= context;
this.ActualNames=ActualNames;
    }


    @NonNull
    @Override
    public Rv_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.list_item_layout,parent,false);
        return new Rv_ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Rv_ViewHolder holder, final int position) {
        String title=data.get(position).toString();

        holder.txt.setText(title);
        holder.txt.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v)  {
                Toast.makeText(activity,"wait for "+data.get(position).toString(),Toast.LENGTH_LONG).show();

                downloadpapers(ActualNames.get(position).toString());
                Log.i("ERROR",ActualNames.get(position).toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class Rv_ViewHolder extends RecyclerView.ViewHolder{
        TextView txt;
        public Rv_ViewHolder(View itemView) {
            super(itemView);
            txt= itemView.findViewById(R.id.text_item);


        }
    }

    public void downloadpapers(final String Actualname){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://nixorstudentapplication.appspot.com/PastPapers/Subjects/Chem/"+Actualname);
        File file1 = new File(Environment.getExternalStorageDirectory() + "/nixorapp/pastpapers/"+Actualname);
        if(file1.exists()==false){

            storageRef.getFile(file1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    Intent i = new Intent(activity,PdfLoad.class);
                    i.putExtra("FileName",Actualname);
              activity.startActivity(i);
                }



            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
Log.i("ERROR","error");

                }
            });



        }
        else{
            Intent i = new Intent(activity,PdfLoad.class);
            i.putExtra("FileName",Actualname);
            activity.startActivity(i);
        }












    }



}
