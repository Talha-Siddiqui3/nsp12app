package com.btb.nixorstudentapplication.BookMyTa;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.btb.nixorstudentapplication.Past_papers.RvAdaptor;
import com.btb.nixorstudentapplication.R;

public class RV_Adaptor_1 extends RecyclerView.Adapter<RvAdaptor.Rv_ViewHolder> {
    @NonNull
    @Override
    public RvAdaptor.Rv_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.student_requests_for_ta_rv,parent,false);
        return new RvAdaptor.Rv_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvAdaptor.Rv_ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
class Rv_ViewHolder extends RecyclerView.ViewHolder{
    TextView txt;
    public Rv_ViewHolder(View itemView) {
        super(itemView);
        txt= itemView.findViewById(R.id.text_item);


    }
}

