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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

import static android.util.Log.e;
import static android.util.Log.i;
public class Pastpaper_adapter extends RecyclerView.Adapter<Pastpaper_adapter.Rv_ViewHolder> implements Filterable {

    public static ArrayList<paperObject> data;
    public static ArrayList<String> ActualNames;
    public static ArrayList<String> stringname;
    common_util common_util = new common_util();
    Activity activity;
    CustomFilter filter;

    public Pastpaper_adapter(ArrayList<paperObject> pastpapers, Activity context, ArrayList<String> ActualNames, ArrayList<String> stringname1){
        data=pastpapers;
        activity= context;
    this.ActualNames=ActualNames;
    stringname=stringname1;
    }


    @NonNull
    @Override
    public Rv_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.itemview_paperload,parent,false);
        return new Rv_ViewHolder(view); }


public void validateData(TextView value, String data,RatingBar ratingBar){
//Atleast one should not be error
        if(data.equals("error")){

    value.setVisibility(View.INVISIBLE);

}else{
    value.setText(data);
    ratingBar.setVisibility(View.VISIBLE);
}
}

public void setRatingValue(float ratingValue, RatingBar ratingBar){
        if(ratingValue!=-1){
            ratingBar.setRating(ratingValue);

        }else{
    ratingBar.setVisibility(View.INVISIBLE);


        }
}
    @Override
    public void onBindViewHolder(@NonNull Rv_ViewHolder holder, final int position) {
    validateData(holder.type,data.get(position).getType(),holder.ratingBar);
    validateData(holder.year,data.get(position).getYear(),holder.ratingBar);
    validateData(holder.variant,data.get(position).getVariant(),holder.ratingBar);
    validateData(holder.month,data.get(position).getMonth(),holder.ratingBar);

    switch (data.get(position).getMonth()){
        case "Winter":holder.linearLayout.setBackgroundColor(activity.getResources().getColor(R.color.winter_color));
        holder.decor_paper.setImageResource(R.drawable.snow_flake);
        break;
        case "Summer":holder.linearLayout.setBackgroundColor(activity.getResources().getColor(R.color.summer_color));
            holder.decor_paper.setImageResource(R.drawable.summer_icon);
        break;
        case "March": holder.linearLayout.setBackgroundColor(activity.getResources().getColor(R.color.spring_color));
            holder.decor_paper.setImageResource(R.drawable.spring_icon);
        break;

    }


        setRatingValue(data.get(position).getRating(),holder.ratingBar);
        holder.linearLayout.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v)  {

                String loadingtext = activity.getString(R.string.pastpaper_loadingtext);
                common_util.progressDialogShow(activity,loadingtext);
                downloadpapers(ActualNames.get(position).toString(),data.get(position));
                Log.i("ERROR",ActualNames.get(position).toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new CustomFilter(stringname,ActualNames,data,this);
        }

        return filter;

    }

    class Rv_ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout linearLayout;
        TextView year;
        TextView month;
        TextView variant;
        TextView type;
        RatingBar ratingBar;
        ImageView decor_paper;




        public Rv_ViewHolder(View itemView) {
            super(itemView);
            year= itemView.findViewById(R.id.year_textView);
            month= itemView.findViewById(R.id.month_textView);
            variant= itemView.findViewById(R.id.variant_textView);
            type= itemView.findViewById(R.id.type_textView);
            decor_paper = itemView.findViewById(R.id.decor_paper);
            ratingBar= itemView.findViewById(R.id.ratingBar);



            linearLayout= itemView.findViewById(R.id.paperlayout);

        }
    }

    public void downloadpapers(final String Actualname, final paperObject paperObject){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://nixorstudentapplication.appspot.com/PastPapers/Subjects/Chem/"+Actualname);
        File file1 = new File(Environment.getExternalStorageDirectory() + "/nixorapp/pastpapers/"+Actualname);
        if(file1.exists()==false){

            storageRef.getFile(file1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    common_util.progressDialogHide();
                //    ((Load_papers)activity).paperOpened =paperObject;
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
           // ((Load_papers)activity).paperOpened =paperObject;
            Intent i = new Intent(activity,PdfLoad.class);
            i.putExtra("FileName",Actualname);
            activity.startActivity(i);
        }














    }



    /*
    * for (int i=0;i<filterList.size();i++)
                {
                    //CHECK
                    if(filterList.get(i).toUpperCase().contains(constraint)) { int values = getIndexNumber(filterList.get(i),filterList);
                        if(1==1){
                            filteredActualNames.add(actualnames.get(values));
                            filteeddata.add(data.get(values));
                            filteredcombinedname.add(filterList.get(i)); }
                            }else{


                            if (itContains(constraint.toString().toUpperCase(),data.get(i))){
                                int values = getIndexNumber(data.get(i),data);
                                if(values!=-1){
                                filteredActualNames.add(actualnames.get(values));
                                filteeddata.add(data.get(i));
                                filteredcombinedname.add(filterList.get(values));
                            }}

                    }
                }

                returnData.add(filteeddata);
                returnData.add(filteredActualNames);
                returnData.add(filteredcombinedname);

                results.count=returnData.size();
                results.values=returnData;
                return results;*/
   private class CustomFilter extends Filter{

        Pastpaper_adapter adapter;
        ArrayList<String> filterList;
        ArrayList<String> actualnames;
        ArrayList<paperObject> data;
        String TAG= "CustomFilter";

        public CustomFilter(ArrayList<String> filterList,ArrayList<String> actualnames1, ArrayList<paperObject> data1 ,Pastpaper_adapter adapter)
        {
                actualnames=actualnames1;
                data=data1;
            this.adapter=adapter;
            this.filterList=filterList;

        }

        //FILTERING OCURS
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();

            //CHECK CONSTRAINT VALIDITY
            if(constraint != null && constraint.length() > 0)
            {
                //CHANGE TO UPPER
              String  text =constraint.toString().toUpperCase().trim();
                //STORE OUR FILTERED PLAYERS
                ArrayList<String> filteredcombinedname=new ArrayList<>();
                ArrayList<paperObject> filteeddata=new ArrayList<>();
                ArrayList<String> filteredActualNames=new ArrayList<>();
                ArrayList<ArrayList> returnData = new ArrayList<>();


                String[] firstsplit= text.split("\\s+");;
                 Log.i(TAG,Integer.toString(firstsplit.length));
                for(int x=0;x<data.size();x++){
                int count =0;
                int found =0;
                for(String value: firstsplit) {
                    if (!value.trim().isEmpty()) {
                        if(!value.trim().equals("PAPER")||!value.trim().equals("SCHEME")){
                        count++;
                        }

                       Boolean contains = itContains(value, data.get(x));
                        if(contains){
                            found++;
                        }

                    }

                }
                    if(found==count){
                    filteeddata.add(data.get(x));
                        filteredActualNames.add(actualnames.get(x));
                        filteredcombinedname.add(filterList.get(x));
                    }

                }
                returnData.add(filteeddata);
                returnData.add(filteredActualNames);
                returnData.add(filteredcombinedname);
                results.count=returnData.size();
                results.values=returnData;
                return results;


            }else
            {

                ArrayList<ArrayList> returnData = new ArrayList<>();
                returnData.add(data);
                returnData.add(actualnames);
                returnData.add(filterList);

                results.count=returnData.size();
                results.values=returnData;
                return results;
            }



        }


        public Boolean itContains(String value, paperObject paperObject){
            try{
          if(paperObject.getVariant().toUpperCase().contains(value)
                  || paperObject.getYear().toUpperCase().contains(value)
                  || paperObject.getType().toUpperCase().contains(value)
                  || paperObject.getMonth().toUpperCase().contains(value)
                  ){
              return true;
          }else{
              return false;
          }}catch (Exception e){

                e.printStackTrace();
                return false;
            }
        }
        public int getIndexNumber(Object name, ArrayList main){

            if(main.contains(name)) {
                int index = main.indexOf(name);
                return  index;
            }else{
                return -1;}


        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            ArrayList<ArrayList> resultFinal;
            resultFinal =(ArrayList<ArrayList>) results.values;

            if(results.values==null){
                Log.i("TAG","Results is null");

            }else{

                adapter.stringname= resultFinal.get(2) ;
                adapter.data= resultFinal.get(0) ;
                adapter.ActualNames= resultFinal.get(1) ;

                //REFRESH
                adapter.notifyDataSetChanged();}
        }
    }

}
