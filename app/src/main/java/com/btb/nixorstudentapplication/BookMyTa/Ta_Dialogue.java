package com.btb.nixorstudentapplication.BookMyTa;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.btb.nixorstudentapplication.R;

public class Ta_Dialogue extends Dialog{

    public Ta_Dialogue(@NonNull Context context) {
        super(context);
        setTitle("Title");
        setContentView(R.layout.ta_dialogue_layout);
    }
}
