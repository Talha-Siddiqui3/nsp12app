package com.btb.nixorstudentapplication.Sharks_on_cloud.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btb.nixorstudentapplication.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;



public class uploadedImages_Adapter extends BaseAdapter{
    ArrayList<String> imageUrls;
    ArrayList<String> creationDate;


    Context context;
    int count=0;
    private static LayoutInflater inflater=null;
    public uploadedImages_Adapter(ArrayList<String> urls,ArrayList<String> creationDates, Context w) {
        imageUrls = urls;
        creationDate = creationDates;
        context = w;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return imageUrls.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }



    public class Holder
    {
      ImageView imageview;
      TextView upvotes;
      TextView creationDate;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        Holder holder=new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.item_uploaded, null);
       holder.imageview = rowView.findViewById(R.id.image);
       holder.upvotes = rowView.findViewById(R.id.upvote_textview);
       holder.creationDate = rowView.findViewById(R.id.creationDate);

        Glide.with(context).load(imageUrls.get(position)).into(holder.imageview);
        holder.creationDate.setText(creationDate.get(position));

        return rowView;

    }

}