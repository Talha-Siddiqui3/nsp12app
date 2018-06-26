package com.btb.nixorstudentapplication.Past_papers.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btb.nixorstudentapplication.Past_papers.MainPPActivity;
import com.btb.nixorstudentapplication.R;

import java.util.ArrayList;

public class Subject_adapter extends RecyclerView.Adapter<Subject_adapter.Rv_ViewHolder> implements Filterable {

   public static  ArrayList<Object> allitems;
    RecyclerView rv;
    SubjectFilter filter;

    Context activity;
    public Subject_adapter(ArrayList<Object> subjects, Context context, RecyclerView recyclerView){
        allitems=subjects;
        activity= context;
        rv=recyclerView;

    }


    @NonNull
    @Override
    public Rv_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.item_subjectname,parent,false);
        return new Rv_ViewHolder(view); }




    @Override
    public void onBindViewHolder(@NonNull Rv_ViewHolder holder, final int position) {


   if(allitems.get(position)!=null) {
       holder.item_name.setText(allitems.get(position).toString());
       holder.item_name.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               MainPPActivity mainPPActivity = new MainPPActivity();
               mainPPActivity.getPapersForSubject(true,allitems.get(position).toString(),activity, rv);
           }
       });
       holder.layout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               MainPPActivity mainPPActivity = new MainPPActivity();
               mainPPActivity.getPapersForSubject(true,allitems.get(position).toString(),activity, rv);
           }
       });
   }

    }

    @Override
    public int getItemCount() {
        return allitems.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new SubjectFilter(activity, allitems,this);
        }

        return filter;

    }

    class Rv_ViewHolder extends RecyclerView.ViewHolder{

        TextView item_name;
        RelativeLayout layout;




        public Rv_ViewHolder(View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.subjectname_textView);
            layout = itemView.findViewById(R.id.layout);


        }
    }
private class SubjectFilter extends Filter{
    Context context;
    ArrayList<String> allsubjects;
    Subject_adapter adapter;

        public SubjectFilter(Context activity, ArrayList data, Subject_adapter adaptr){
            allsubjects=data;
            context=activity;
            adapter=adaptr;

        }

        @Override
    protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();


            if (constraint != null && constraint.length() > 0) {

                String text = constraint.toString().toUpperCase().trim();
              ArrayList<String> classified = new ArrayList<>();

              for(int x=0;x<allsubjects.size();x++){
                  if(allsubjects.get(x).toUpperCase().contains(text)){
                      classified.add(allsubjects.get(x));
                  }

              }

                results.count = classified.size();
                results.values = classified;
                return results;
            }else{
                results.count = allsubjects.size();
                results.values = allsubjects;
                return results;
            }


    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults results) {

        if (results.values == null) {
            Log.i("TAG", "Results is null");

        } else {
            adapter.allitems = (ArrayList<Object>) results.values;
            adapter.notifyDataSetChanged();
    }
}




}}
