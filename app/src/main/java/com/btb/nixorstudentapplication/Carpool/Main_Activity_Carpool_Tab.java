package com.btb.nixorstudentapplication.Carpool;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.btb.nixorstudentapplication.Carpool.Adaptors.ViewPage_Adaptor;
import com.btb.nixorstudentapplication.Carpool.Fragments_For_Tabs.MyRides_Fragment;
import com.btb.nixorstudentapplication.Carpool.Fragments_For_Tabs.Ride_Requests;
import com.btb.nixorstudentapplication.Carpool.Fragments_For_Tabs.Rides_Fragment;
import com.btb.nixorstudentapplication.GeneralLayout.activity_header;

import com.btb.nixorstudentapplication.R;

public class Main_Activity_Carpool_Tab extends AppCompatActivity {
    private activity_header activity_header;
    private TabLayout tablayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_carpool_tab);
        activity_header = findViewById(R.id.toolbar_top_Carpool);
        activity_header.setActivityname("Carpool");
        tablayout = findViewById(R.id.tablayout_carpool);
        viewPager = findViewById(R.id.viewpager_carpool);
        ViewPage_Adaptor adaptor = new ViewPage_Adaptor(this.getSupportFragmentManager());
       adaptor.AddFragment(new MyRides_Fragment(), "My rides");
       adaptor.AddFragment(new Rides_Fragment(), "Rides");
       adaptor.AddFragment(new Ride_Requests(), "Ride requests");

        viewPager.setAdapter(adaptor);
        viewPager.setOffscreenPageLimit(3);
        tablayout.setupWithViewPager(viewPager);

    }
}
