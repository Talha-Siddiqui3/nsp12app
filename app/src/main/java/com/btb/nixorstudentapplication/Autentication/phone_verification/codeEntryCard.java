package com.btb.nixorstudentapplication.Autentication.phone_verification;

import android.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.chaos.view.PinView;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class codeEntryCard extends Fragment implements View.OnClickListener {


    Button verify_code;

ImageView back_button;
    common_util common_util;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_login_codeverify, container, false);
        ((login_screen)getActivity()).code_entry = (PinView) rootview.findViewById(R.id.code_entry);
        ((login_screen)getActivity()).code_timer = (TextView) rootview.findViewById(R.id.code_timer);
        verify_code =(Button) rootview.findViewById(R.id.verify_code);
        back_button =(ImageView) rootview.findViewById(R.id.back_button);


back_button.setOnClickListener(this);

        verify_code.setOnClickListener(this);
        common_util = new common_util();

        return rootview;
    }
//check;lasm
    @Override
    public void onClick(View view) {
        Log.i("buttonClicked","yellow");
       switch (view.getId()){

           case R.id.verify_code: ((login_screen)getActivity()).verifyCode();
               Log.i("buttonClicked","red");
           break;
           case R.id.back_button: ((login_screen)getActivity()).flipCard();break;
    }}
}

