package com.btb.nixorstudentapplication.BookMyTa;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPageAdaptor extends FragmentPagerAdapter {
   List<Fragment> fragment_list=new ArrayList<>();
    List<String> fragment_list_title=new ArrayList<>();





    @Override
    public Fragment getItem(int position) {
       return fragment_list.get(position);
    }

    public ViewPageAdaptor(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
       return fragment_list.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
      return fragment_list_title.get(position);

    }

public void AddFragment(Fragment fragment,String title){
fragment_list.add(fragment);
fragment_list_title.add(title);
}

}
