package com.btb.nixorstudentapplication.Autentication.User;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.btb.nixorstudentapplication.Application_Home.home_screen;
import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountType extends AppCompatActivity implements View.OnClickListener {
private Button studentmode_button;
private Button parentmode_button;

private common_util common_util;
private String username;
private String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_type);
        common_util = new common_util();
        studentmode_button = (Button)findViewById(R.id.studentmode_button);
        parentmode_button = (Button)findViewById(R.id.parentmode_button);
        parentmode_button.setOnClickListener(this);
        studentmode_button.setOnClickListener(this);
    }
public void activateStudent(){
    setAccountLink("student");
}
public void activateParent(){
        setAccountLink("parent");
    }

public void setAccountLink(final String mode){
    username = common_util.getUserDataLocally(AccountType.this,"username");
    phoneNumber = common_util.formatNumbers(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
    accountTypeGetSet account = new accountTypeGetSet(username,mode);
  //  FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    db.collection("account_link").document(phoneNumber).set(account).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
        common_util.saveUserDataLocally(AccountType.this,"Mode",mode);
            startActivity(new Intent(AccountType.this, home_screen.class));
            finish();
        }
    });
}

    @Override
    public void onClick(View view) {
        switch (view.getId()){

case R.id.studentmode_button:
    activateStudent();
    uploadFirebaseToken();
    break;
case R.id.parentmode_button:
    activateParent();
    break;
        }
    }

    private void uploadFirebaseToken() {

    }
}
