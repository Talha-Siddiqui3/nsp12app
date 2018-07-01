package com.btb.nixorstudentapplication.Past_papers;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.Past_papers.Objects.paperObject;
import com.btb.nixorstudentapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.util.Log.i;
public class RvAdaptor extends RecyclerView.Adapter<RvAdaptor.Rv_ViewHolder> {

    private ArrayList<paperObject> data;
    private ArrayList<String> ActualNames;
    common_util common_util = new common_util();
    Activity activity;
    public RvAdaptor(ArrayList<paperObject> pastpapers, Activity context,ArrayList<String> ActualNames){
        data=pastpapers;
        activity= context;
    this.ActualNames=ActualNames;
    }


    @NonNull
    @Override
    public Rv_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.paper_item,parent,false);
        return new Rv_ViewHolder(view); }


public void validateData(TextView title,TextView value, String data,RatingBar ratingBar){
if(data.equals("error")){
    title.setVisibility(View.INVISIBLE);
    value.setVisibility(View.INVISIBLE);

}else{
    value.setText(data);
    ratingBar.setVisibility(View.VISIBLE);
}

}


    @Override
    public void onBindViewHolder(@NonNull Rv_ViewHolder holder, final int position) {
    validateData(holder.typeTitle,holder.type,data.get(position).getType(),holder.ratingBar);
    validateData(holder.yearTitle,holder.year,data.get(position).getYear(),holder.ratingBar);
    validateData(holder.variantTitle,holder.variant,data.get(position).getVariant(),holder.ratingBar);
    validateData(holder.monthTitle,holder.month,data.get(position).getMonth(),holder.ratingBar);


        holder.linearLayout.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v)  {

                String loadingtext = activity.getString(R.string.pastpaper_loadingtext);
                common_util.progressDialogShow(activity,loadingtext);
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
        RelativeLayout linearLayout;
        TextView year;
        TextView month;
        TextView variant;
        TextView type;
        RatingBar ratingBar;

        TextView typeTitle;
        TextView monthTitle;
        TextView yearTitle;
        TextView variantTitle;

        public Rv_ViewHolder(View itemView) {
            super(itemView);
            year= itemView.findViewById(R.id.year_textView);
            month= itemView.findViewById(R.id.month_textView);
            variant= itemView.findViewById(R.id.variant_textView);
            type= itemView.findViewById(R.id.type_textView);
            ratingBar= itemView.findViewById(R.id.ratingBar_FoldingView);

            typeTitle= itemView.findViewById(R.id.typeTitle_textView);
            yearTitle= itemView.findViewById(R.id.yearTitle_textView);
            monthTitle= itemView.findViewById(R.id.monthTitle_textView);
            variantTitle= itemView.findViewById(R.id.variantTitle_textView);


            linearLayout= itemView.findViewById(R.id.paperlayout);

        }
    }

    public void downloadpapers(final String Actualname){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://nixorstudentapplication.appspot.com/PastPapers/Subjects/"+((MainPPActivity)activity).subjectSelected+"/"+Actualname);
        File file1 = new File(Environment.getExternalStorageDirectory() + "/nixorapp/pastpapers/"+Actualname);
        if(file1.exists()==false){

            storageRef.getFile(file1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    common_util.progressDialogHide();
                    Intent i = new Intent(activity,PdfLoad.class);
                    i.putExtra("FileName",Actualname);
              activity.startActivity(i);
                }



            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                    common_util.progressDialogHide();
                    Log.i("ERROR","error");

                }
            });



        }
        else{
            common_util.progressDialogHide();
            Intent i = new Intent(activity,PdfLoad.class);
            i.putExtra("FileName",Actualname);
            activity.startActivity(i);
        }












    }



}
