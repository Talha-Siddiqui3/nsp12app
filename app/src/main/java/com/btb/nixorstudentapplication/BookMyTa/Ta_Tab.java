package com.btb.nixorstudentapplication.BookMyTa;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.btb.nixorstudentapplication.R;

public class Ta_Tab extends AppCompatActivity {
private TabLayout tablayout;
private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ta__tab);
    tablayout=findViewById(R.id.tablayout_id);
    viewPager=findViewById(R.id.viewpager_id);
    ViewPageAdaptor adaptor=new ViewPageAdaptor(getSupportFragmentManager());
adaptor.AddFragment(new Search_Ta_Fragment(),"Search Ta");
        adaptor.AddFragment(new Student_Requests_For_Ta_Fragment(),"Requests for you");
        adaptor.AddFragment(new Requests_For_Ta_Fragment(),"Your requets for Ta");
        viewPager.setAdapter(adaptor);
        tablayout.setupWithViewPager(viewPager);









    }
}
