package com.btb.nixorstudentapplication.GeneralLayout;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.btb.nixorstudentapplication.Application_Home.home_screen;
import com.btb.nixorstudentapplication.BookMyTa.Main_Activity_Ta_Tab;
//import com.btb.nixorstudentapplication.Past_papers.MainPPActivity;
import com.btb.nixorstudentapplication.R;

import java.util.ArrayList;

public class navigator_header extends RelativeLayout {
    View rootView;
  RecyclerView recyclerView;
  ArrayList<String> allitems = new ArrayList();
    public navigator_header(Context context) {
        super(context);
        init(context);

    }

    public navigator_header(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.menu_scroll, this);
        recyclerView =  rootView.findViewById(R.id.list_recycler);
        allitems.add("Carpool");
        allitems.add("Chat");
        allitems.add("Home");
        allitems.add("Soc");
        allitems.add("Bookmyta");
        allitems.add("Pastpapers");

     // if(context instanceof MainPPActivity){
    //      allitems.remove("Pastpapers");
    //  }

        if(context instanceof home_screen){
            allitems.remove("Home");
        }
        if(context instanceof Main_Activity_Ta_Tab){
            allitems.remove("Bookmyta");
        }


        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
     recyclerView.setLayoutManager(horizontalLayoutManager);
        nav_adapter adapter= new nav_adapter(allitems,context);
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(recyclerView);
            recyclerView.setAdapter(adapter);


        }
}
