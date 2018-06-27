package com.btb.nixorstudentapplication.BookMyTa.Adaptors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btb.nixorstudentapplication.BookMyTa.TA_Object;
import com.btb.nixorstudentapplication.BookMyTa.Ta_Dialogue;
import com.btb.nixorstudentapplication.BookMyTa.Main_Activity_Ta_Tab;
import com.btb.nixorstudentapplication.R;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.List;

//Adaptor For Searching TA Layout
public class RV_Adaptor_3_For_Search_Ta extends RecyclerView.Adapter<RV_Adaptor_3_For_Search_Ta.Rv_ViewHolder> {
    List<TA_Object> ta_objects = new ArrayList<>();
    Main_Activity_Ta_Tab mainActivityTa_tab = new Main_Activity_Ta_Tab();
    Context context;


    String TAG = "RV_Adaptor_3_For_Search_Ta";

    public RV_Adaptor_3_For_Search_Ta(List<TA_Object> ta_objects) {
        this.ta_objects = ta_objects;

    }

    @NonNull
    @Override
    public RV_Adaptor_3_For_Search_Ta.Rv_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cell, parent, false);
        return new Rv_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RV_Adaptor_3_For_Search_Ta.Rv_ViewHolder holder, final int position) {
        Log.i(TAG, ta_objects.get(position).getTaName());
        holder.TaNamesText.setText(ta_objects.get(position).getTaName());
        holder.TeachesText.setText(ta_objects.get(position).getSubject());
     /*   holder.relativeLayoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //sends the speceific TAObject from the ArrayList of TaObjects.
                //Ta_Dialogue ta_dialogue = new Ta_Dialogue(context,ta_objects.get(position));
                //ta_dialogue.ParseData();
                //ta_dialogue.ExtractTime();
               // ta_dialogue.UpdateLayout();
                //ta_dialogue.show();






            }
        });
    */
        holder.fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.fc.toggle(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ta_objects.size();
    }

    class Rv_ViewHolder extends RecyclerView.ViewHolder {
        TextView TaNamesText;
        TextView TeachesText;
        RelativeLayout relativeLayoutLayout;
       FoldingCell fc;

        public Rv_ViewHolder(View itemView) {
            super(itemView);
            TaNamesText = itemView.findViewById(R.id.TA_NAME_Search);
            TeachesText = itemView.findViewById(R.id.Teaches_Search);
           // relativeLayoutLayout = itemView.findViewById(R.id.cell_title_layout);
            context = itemView.getContext();
      fc=itemView.findViewById(R.id.cell);
        }
    }
}







