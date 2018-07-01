package com.btb.nixorstudentapplication.Nsp_Portal.Adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.btb.nixorstudentapplication.R;

import java.util.ArrayList;
import java.util.List;

public class Nsp_Adaptor extends BaseAdapter implements View.OnClickListener {
    List<String> iconList = new ArrayList<>();
    private TextView txt;
    private ImageView img;

public Nsp_Adaptor(List<String>iconList){
    this.iconList=iconList;
}
    @Override
    public void onClick(View v) {

    }

    @Override
    public int getCount() {
        return iconList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(R.layout.nsp_layout_for_grid_view, parent, false);
        }
        txt = convertView.findViewById(R.id.icon_text_nsp_portal);
        img = convertView.findViewById(R.id.iconimage_nsp_portal);
        //txt.setOnClickListener(this);
        //img.setOnClickListener(this);
        txt.setText(iconList.get(position));
        convertView.setTag(iconList.get (position));
        switch (iconList.get(position)) {
            case "Profile":
                img.setImageResource(R.drawable.newprofile);
                break;
            case "Gate Attendance":
                img.setImageResource(R.drawable.gateattendance);
                break;
            case "Class Attendance":
                img.setImageResource(R.drawable.class_attendance);
                break;
            case "Finance":
                img.setImageResource(R.drawable.finance);
                break;
            case "Schedule":
                img.setImageResource(R.drawable.schedule_image);
                break;
            case "Student Marks":
                img.setImageResource(R.drawable.studentmarks);
                break;
            case "CIE Grades":
                img.setImageResource(R.drawable.cie_grades);
                break;
            case "TA Schedule":
                img.setImageResource(R.drawable.tascheduleicon);
                break;
            case "TA Log":
                img.setImageResource(R.drawable.talogicon);
                break;
            default:
                img.setVisibility(View.INVISIBLE);
                break;


        }


        return convertView;
    }

}
