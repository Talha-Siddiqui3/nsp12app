package com.btb.nixorstudentapplication.PastPapers;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;
//Rv means Recycle View
public class RvAdaptor extends RecyclerView.Adapter<RvAdaptor.Rv_ViewHolder> {

    private ArrayList data;
    Activity activity;
    public RvAdaptor(ArrayList<String> pastpapers, Activity context){
        data=pastpapers;
        activity= context;

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
                // ((MainActivity)activity).downloadPaper(data.get(position).toString());

                Toast.makeText(activity,"hello"+data.get(position).toString(),Toast.LENGTH_LONG).show();
                Log.i("pupu","pupuupupup");
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





}
