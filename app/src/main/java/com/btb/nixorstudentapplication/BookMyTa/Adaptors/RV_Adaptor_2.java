package com.btb.nixorstudentapplication.BookMyTa.Adaptors;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.btb.nixorstudentapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RV_Adaptor_2 extends RecyclerView.Adapter<RV_Adaptor_2.Rv_ViewHolder>{

    List<Map<String,Object>> mymaps=new ArrayList<>();


    String TAG="RV_Adaptor_2";
    public RV_Adaptor_2(List<Map<String, Object>> mymaps){
      this.mymaps=mymaps;

    }

    @NonNull
    @Override
    public RV_Adaptor_2.Rv_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.request_to_book_ta_layout, parent, false);
        return new Rv_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RV_Adaptor_2.Rv_ViewHolder holder, int position) {
holder.StatusText.setText(mymaps.get(position).get("Status").toString());
      holder.TaNamesText.setText(mymaps.get(position).get("TaName").toString());
      Log.i(TAG,mymaps.get(position).get("TaName").toString());
        Log.i(TAG,mymaps.get(position).get("Status").toString());
        Log.i(TAG,Integer.toString(position));

    }

    @Override
    public int getItemCount() {

        return mymaps.size();
    }

    class Rv_ViewHolder extends RecyclerView.ViewHolder {
        TextView TaNamesText;
        TextView StatusText;

        public Rv_ViewHolder(View itemView) {
            super(itemView);
TaNamesText=itemView.findViewById(R.id.Ta_Name);
StatusText=itemView.findViewById(R.id.Request_Status);



        }
    }
}
