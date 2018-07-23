package com.btb.nixorstudentapplication.BookMyTa;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.btb.nixorstudentapplication.BookMyTa.Adaptors.ViewPageAdaptor;
import com.btb.nixorstudentapplication.BookMyTa.Fragments_for_tabs.Requests_To_Book_Ta_Fragment;
import com.btb.nixorstudentapplication.BookMyTa.Fragments_for_tabs.Search_Ta_Fragment;
import com.btb.nixorstudentapplication.BookMyTa.Fragments_for_tabs.Student_Requests_For_Ta_Fragment;
import com.btb.nixorstudentapplication.GeneralLayout.activity_header;
import com.btb.nixorstudentapplication.Misc.permission_util;
import com.btb.nixorstudentapplication.R;


public class Main_Activity_Ta_Tab extends AppCompatActivity {
    private TabLayout tablayout;
    private ViewPager viewPager;
    private activity_header activity_header;
    public static int i = 100;
    ImageView filterButton;
    EditText taSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ta__tab);
        tablayout = findViewById(R.id.tablayout_id);
        viewPager = findViewById(R.id.viewpager_id);
        activity_header = findViewById(R.id.toolbar_top_Ta_Tab);
        ViewPageAdaptor adaptor = new ViewPageAdaptor(this.getSupportFragmentManager());
        adaptor.AddFragment(new Search_Ta_Fragment(), "Search Ta");
        adaptor.AddFragment(new Student_Requests_For_Ta_Fragment(), "Requests for you");
        adaptor.AddFragment(new Requests_To_Book_Ta_Fragment(), "Your requests for Ta");
        viewPager.setAdapter(adaptor);
        viewPager.setOffscreenPageLimit(3);
        tablayout.setupWithViewPager(viewPager);
        activity_header.setActivityname("Bookmyta");
        GetVibratinPermission();
        taSearch=findViewById(R.id.searchfield_bookmyta);
        HideKeyboard(taSearch);
        filterButton = findViewById(R.id.filterbutton_bookmyta);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main_Activity_Ta_Tab.this, TA_Filter.class));
            }
        });


    }

    private void GetVibratinPermission() {
        permission_util permission_util = new permission_util();
        String[] permissions = {Manifest.permission.VIBRATE};
        permission_util.getPermissions(this, permissions);
    }


    public void HideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}