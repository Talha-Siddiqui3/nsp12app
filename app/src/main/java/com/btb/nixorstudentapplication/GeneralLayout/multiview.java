package com.btb.nixorstudentapplication.GeneralLayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btb.nixorstudentapplication.R;

public class multiview extends AppCompatActivity {

    TextView divider;
    RelativeLayout bottom, top;


    int topheight;
    int topwidth;
    int bottomwidth;
    int bottomheight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pastpaper_multiview);
        divider=findViewById(R.id.divider);
        bottom =findViewById(R.id.bottom);
        top =findViewById(R.id.top);

 topheight = top.getHeight();
topwidth = top.getWidth();
 bottomwidth = bottom.getWidth();
bottomheight = bottom.getHeight();
        Log.i("YO",Integer.toString(topheight));
        divider.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    RelativeLayout.LayoutParams topParams = new RelativeLayout.LayoutParams(
                            topwidth, topheight+1);

                    RelativeLayout.LayoutParams bottomParams = new RelativeLayout.LayoutParams(
                            bottomwidth, bottomheight-1);
                    top.setLayoutParams(topParams);
                    bottom.setLayoutParams(bottomParams);
                    return true;
                }
                return false;
            }
        });
    }
}
