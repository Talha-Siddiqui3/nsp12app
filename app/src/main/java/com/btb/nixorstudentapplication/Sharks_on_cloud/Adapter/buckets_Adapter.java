package com.btb.nixorstudentapplication.Sharks_on_cloud.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.btb.nixorstudentapplication.Sharks_on_cloud.MainCloudActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class buckets_Adapter extends BaseAdapter {
    ArrayList<String> usernames;
    common_util common_util = new common_util();


    Context context;
    int count = 0;
    private static LayoutInflater inflater = null;

    public buckets_Adapter(ArrayList<String> urls, Context w) {
        usernames = urls;

        context = w;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return usernames.size();
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


    public class Holder {
        CircleImageView imageview;
        TextView upvotes;
        TextView creationDate;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        final Holder holder = new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.item_bucket, null);
        holder.imageview = rowView.findViewById(R.id.image);
        holder.creationDate = rowView.findViewById(R.id.creationDate);

        if (usernames.get(position).equals("MyBucket")) {
            getPhotUrls(common_util.getUserDataLocally(context, "username"), holder.imageview);
        } else {
            getPhotUrls(usernames.get(position), holder.imageview);
        }
        holder.creationDate.setText(usernames.get(position));

        holder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usernames.get(position).equals("MyBucket")) {
                    ((MainCloudActivity) context).getAllImages(context, common_util.getUserDataLocally(context, "username"));
                } else {
                    ((MainCloudActivity) context).getAllImages(context, usernames.get(position));
                }
            }
        });

        return rowView;

    }

    public void getPhotUrls(String username, final CircleImageView image) {

        String getNodeLocation = context.getString(R.string.mainUserNode);
        DocumentReference documentReference = FirebaseFirestore.getInstance().document(getNodeLocation + username);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Map<String, Object> map = task.getResult().getData();
                if (map.containsKey("photourl")) {
                    Glide.with(context).load(map.get("photourl")).into(image);
                }

            }
        });

    }

}