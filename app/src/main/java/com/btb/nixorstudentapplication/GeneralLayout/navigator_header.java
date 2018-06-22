package com.btb.nixorstudentapplication.GeneralLayout;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.btb.nixorstudentapplication.R;

import java.util.ArrayList;

public class navigator_header extends RelativeLayout {
    View rootView;
  RecyclerView recyclerView;
  ArrayList<String> allitems = new ArrayList();
    public navigator_header(Context context) {
        super(context);
        init(context);
        allitems.add("Home");
        allitems.add("Carpool");
        allitems.add("Soc");
        allitems.add("Portal");
        allitems.add("Bookmyta");
        allitems.add("Pastpapers");
    }

    public navigator_header(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.menu_scroll, this);
        recyclerView =  rootView.findViewById(R.id.list_recycler);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
            recyclerView.setAdapter(new nav_menuAdapter(allitems,context));

        }
}
