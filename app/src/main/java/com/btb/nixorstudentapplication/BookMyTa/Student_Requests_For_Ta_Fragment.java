package com.btb.nixorstudentapplication.BookMyTa;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.btb.nixorstudentapplication.R;

public class Student_Requests_For_Ta_Fragment extends Fragment {
    View view;
    public Student_Requests_For_Ta_Fragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.student_requests_for_ta,container,false);
        return view;
    }
}

