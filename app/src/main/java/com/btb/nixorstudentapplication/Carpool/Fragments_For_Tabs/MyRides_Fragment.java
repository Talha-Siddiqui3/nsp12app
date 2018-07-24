package com.btb.nixorstudentapplication.Carpool.Fragments_For_Tabs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.btb.nixorstudentapplication.R;

public class MyRides_Fragment extends Fragment {
    private View view;
    private Button createRide;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.my_rides, container, false);
      /*  RecyclerView rv = (RecyclerView) view.findViewById(R.id.request_to_book_ta_rv);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvAdaptor = new RV_Adaptor_2(DisplayRequest());
         \rv.setAdapter(rvAdaptor);*/
        createRide = view.findViewById(R.id.create_ride);
        return view;

    }
}
