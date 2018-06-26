package com.btb.nixorstudentapplication.Past_papers.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.Past_papers.MainPPActivity;
import com.btb.nixorstudentapplication.Past_papers.PdfLoad;
import com.btb.nixorstudentapplication.Past_papers.Objects.paperObject;
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
    public static Boolean multi;


    common_util common_util = new common_util();
    Context activity;
    searchFilter filter;

    //Constructor nothing more
    public Pastpaper_adapter(ArrayList<paperObject> pastpapers, Context context, ArrayList<String> ActualNames, Boolean multiview) {
        data = pastpapers;
        activity = context;
        this.ActualNames = ActualNames;
        multi = multiview;
    }

    //Here you play with the views
    @Override
    public void onBindViewHolder(@NonNull Rv_ViewHolder holder, final int position) {
        validateData(holder.type, data.get(position).getType(), holder.ratingBar);
        validateData(holder.year, data.get(position).getYear(), holder.ratingBar);
        validateData(holder.variant, data.get(position).getVariant(), holder.ratingBar);
        validateData(holder.month, data.get(position).getMonth(), holder.ratingBar);

       if(((MainPPActivity)activity).multiviewactiv!=null){
           holder.multiViewPaper.setText("Remove");
           holder.multiViewPaper.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
           holder.multiViewPaper.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   //Remove multiview


               }
           });
       }else{
     holder.multiViewPaper.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             ((MainPPActivity)activity).multiviewactiv=ActualNames.get(position);
             ((MainPPActivity)activity).addPapertoMultiView(data.get(position),activity,ActualNames.get(position));

         }
     });}


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
        if(!multi){
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                String loadingtext = activity.getString(R.string.pastpaper_loadingtext);
                common_util.progressDialogShow(activity, loadingtext);
                if( ((MainPPActivity)activity).multiviewactiv!=null){
                downloadpapers(ActualNames.get(position).toString(), true,((MainPPActivity)activity).multiviewactiv);}
                else{
                    downloadpapers(ActualNames.get(position).toString(), false,null);
                }
                Log.i("ERROR", ActualNames.get(position).toString());
            }
        });
    }}
    class Rv_ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout linearLayout;
        TextView year;
        TextView month;
        TextView variant;
        TextView type;
        RatingBar ratingBar;
        ImageView decor_paper;
        Button multiViewPaper;

        public Rv_ViewHolder(View itemView) {
            super(itemView);
            year = itemView.findViewById(R.id.year_textView);
            month = itemView.findViewById(R.id.month_textView);
            variant = itemView.findViewById(R.id.variant_textView);
            type = itemView.findViewById(R.id.type_textView);
            decor_paper = itemView.findViewById(R.id.decor_paper);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            multiViewPaper = itemView.findViewById(R.id.activateMultiView);


            linearLayout = itemView.findViewById(R.id.paperlayout);

        }
    }



    //Pretty self explanatory. Downloads the papers.
    public static String firspaper;


    public void downloadpapers(final String Actualname, final Boolean multipapers, final String secondpaper) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        //Remove Chem from here

        StorageReference storageRef = storage.getReferenceFromUrl("gs://nixorstudentapplication.appspot.com/PastPapers/Subjects/Chem/" + Actualname);
        File file1 = new File(Environment.getExternalStorageDirectory() + "/nixorapp/pastpapers/" + Actualname);
        if (file1.exists() == false) {

            storageRef.getFile(file1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                   if(!multipapers&&!secondpaper.equals("open")){
                       common_util.progressDialogHide();
                    Intent i = new Intent(activity, PdfLoad.class);
                    i.putExtra("FileName", Actualname);
                    activity.startActivity(i);}
                    else if(secondpaper.equals("open")){
                       common_util.progressDialogHide();
                       Intent i = new Intent(activity, PdfLoad.class);
                       i.putExtra("firstPaper", firspaper);
                       i.putExtra("secondPaper", Actualname);
                       activity.startActivity(i);
                   }else
                        {
                            firspaper=Actualname;
                       downloadpapers(secondpaper, true,"open");

                   }

                }


            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    common_util.progressDialogHide();
                    Log.i("ERROR", "error");

                }
            });


        } else {
            common_util.progressDialogHide();
            if(!multipapers&&!secondpaper.equals("open")){
                common_util.progressDialogHide();
            Intent i = new Intent(activity, PdfLoad.class);
            i.putExtra("FileName", Actualname);
            activity.startActivity(i);}
            else if(secondpaper.equals("open")){
                common_util.progressDialogHide();
                Intent i = new Intent(activity, PdfLoad.class);
                i.putExtra("firstPaper", firspaper);
                i.putExtra("secondPaper", Actualname);
                activity.startActivity(i);

            }else{
                firspaper=Actualname;
                downloadpapers(secondpaper, true,"open");

            }

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

    //The search bar's main shit
    private class searchFilter extends Filter {

        Pastpaper_adapter adapter;
        ArrayList<String> filterList;
        ArrayList<String> actualnames;
        ArrayList<paperObject> data;
        String TAG = "searchFilter";

        //Constructor
        public searchFilter(ArrayList<String> actualnames1, ArrayList<paperObject> data1, Pastpaper_adapter adapter) {
            actualnames = actualnames1;
            data = data1;
            this.adapter = adapter;
            this.filterList = filterList;

        }

        //The main shit
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();


            if (constraint != null && constraint.length() > 0) {

                String text = constraint.toString().toUpperCase().trim();



                //All the important arrays for pastpapers
                ArrayList<paperObject> filteeddata = new ArrayList<>();
                ArrayList<String> filteredActualNames = new ArrayList<>();

                //This is just an over complicated Array to contain all the other Arrays and return them to publishResults method. Think of it as a bus
                //Which is carrying all the Arrays to the adapter so that they can be applied. I could have created and object and used that but too lazy
                ArrayList<ArrayList> returnData = new ArrayList<>();

                //Splitting the query by spaces. Basically extracting individual words
                String[] firstsplit = text.split("\\s+");
                ;
                //Logs the total number of words.Including "Paper and Scheme" and I think mayber spaces too. Not sure but who cares?
                Log.i(TAG, Integer.toString(firstsplit.length));

                //Going to run through each word and check if paperObject contains it. Rest is self explanatory
                for (int x = 0; x < data.size(); x++) {
                    //Total words in the sentence excluding spaces and the word "PAPER" and "SCHEME" as they are follow ups to other words and hence are useless
                    int count = 0;
                    //The number of words that are found in paperObject
                    int found = 0;

                    for (String value : firstsplit) {
                        if (!value.trim().isEmpty()) {
                            if (!value.trim().equals("PAPER") || !value.trim().equals("SCHEME")) {
                                count++;
                            }
                            Boolean contains = itContains(value, data.get(x));
                            if (contains) {
                                found++;
                            }

                        }

                    }
                    //If all words are found that means this specific paperObject is relevant to the user and hence add to the filteredList
                    if (found == count) {
                        filteeddata.add(data.get(x));
                        filteredActualNames.add(actualnames.get(x));

                    }

                }
                //Putting all the arrays on the bus and sending them to publishResults :)
                returnData.add(filteeddata);
                returnData.add(filteredActualNames);
                results.count = returnData.size();
                results.values = returnData;
                return results;


            } else {
                //incase user clears his search bar. Returns original data
                ArrayList<ArrayList> returnData = new ArrayList<>();
                returnData.add(data);
                returnData.add(actualnames);
                returnData.add(filterList);

                results.count = returnData.size();
                results.values = returnData;
                return results;
            }


        }

        //Method to check if the paperObject contains a specific word
        public Boolean itContains(String value, paperObject paperObject) {
            try {
                if (paperObject.getVariant().toUpperCase().contains(value)
                        || paperObject.getYear().toUpperCase().contains(value)
                        || paperObject.getType().toUpperCase().contains(value)
                        || paperObject.getMonth().toUpperCase().contains(value)
                        ) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {

                e.printStackTrace();
                return false;
            }
        }

        //Just updating the adapter. Dw about this method here unless.....;)
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            ArrayList<ArrayList> resultFinal;
            resultFinal = (ArrayList<ArrayList>) results.values;

            if (results.values == null) {
                Log.i("TAG", "Results is null");

            } else {


                adapter.data = resultFinal.get(0);
                adapter.ActualNames = resultFinal.get(1);
                adapter.notifyDataSetChanged();
            }
        }
    }




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
    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new searchFilter(ActualNames, data, this);
        }

        return filter;

    }
}
