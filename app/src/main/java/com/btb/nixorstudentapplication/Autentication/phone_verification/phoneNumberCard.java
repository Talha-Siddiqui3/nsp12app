package com.btb.nixorstudentapplication.Autentication.phone_verification;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;



public  class phoneNumberCard extends Fragment implements View.OnClickListener {
    static EditText phone_entry;

    String TAG ="phoneNumberCardFragment";
    static com.btb.nixorstudentapplication.Misc.common_util common_util;
    static PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_login_codesend, container, false);
        phone_entry =(EditText) rootview.findViewById(R.id.phone_entry);
        ((login_screen)getActivity()).send_code = (Button) rootview.findViewById(R.id.send_code);
        ((login_screen)getActivity()).send_code.setOnClickListener(this);
        ui_Updates(phone_entry,((login_screen)getActivity()).send_code);
        common_util = new common_util();
        verification_callback();
        return rootview;

    }
    private void sendCode(){
        if(phone_entry.getText()!=null){
            String loadinText = getActivity().getString(R.string.phoneAuthSendCodeLoadingText);
            common_util.progressDialogShow(getActivity(),loadinText);
            ((login_screen)getActivity()).phoneNumber = phone_entry.getText().toString();
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    ((login_screen)getActivity()).phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    getActivity(),               // Activity (for callback binding)
                    mCallbacks);
            ((login_screen)getActivity()).beingverified=true;
        }       // OnVerificationStateChangedCallbacks
    }
    private void verification_callback(){
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                common_util.progressDialogHide();
                ((login_screen)getActivity()).beingverified=false;
                ((login_screen)getActivity()).updateInstanceState();
                Log.d(TAG, "onVerificationCompleted:" + credential);
                ((login_screen)getActivity()).signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                common_util.progressDialogHide();
                Log.w(TAG, "onVerificationFailed", e);
                ((login_screen)getActivity()).beingverified=false;
                String errorcode="999";
                ((login_screen)getActivity()).updateInstanceState();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    errorcode="0001";
                    common_util.errorReporting(TAG,errorcode,e);
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    errorcode="0002";
                    common_util.errorReporting(TAG,errorcode,e); }
                ((login_screen)getActivity()).displayError(e,errorcode,getString(R.string.unabletosendcode));
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                common_util.progressDialogHide();
                ((login_screen)getActivity()).updateUICodeSent();
                ((login_screen)getActivity()).enableSms();
                ((login_screen)getActivity()).checkforSms();
                Log.d(TAG, "onCodeSent:" + verificationId);
                ((login_screen)getActivity()).mVerificationId = verificationId;
                ((login_screen)getActivity()).mResendToken = token;

            }
        };
    }

    @Override
    public void onClick(View view) {
        sendCode();
    }
    public void ui_Updates(EditText phone_entry, final Button send_code){
        //Enable verify button on phone number entered
        phone_entry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>4){
                    send_code.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>4){
                    send_code.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()>4){
                    send_code.setEnabled(true);
                }
            }
        });
    }


}