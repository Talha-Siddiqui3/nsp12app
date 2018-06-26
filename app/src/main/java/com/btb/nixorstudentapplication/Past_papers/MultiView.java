
package com.btb.nixorstudentapplication.Past_papers;

import android.content.ClipData;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btb.nixorstudentapplication.GeneralLayout.SplitView;
import com.btb.nixorstudentapplication.R;

public class MultiView extends AppCompatActivity {
    private Button mHalves;
    private Button mMaximizePrimaryContent;
    private Button mMaximizeSecondaryContent;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_view);

        mMaximizePrimaryContent = (Button)findViewById(R.id.maximize_primary);
        mMaximizePrimaryContent.setOnClickListener( new View.OnClickListener() {
            @Override public void onClick(View v) {
                ((SplitView)findViewById(R.id.split_view)).maximizePrimaryContent();
            }

        });

        mMaximizeSecondaryContent = (Button)findViewById(R.id.maximize_secondary);
        mMaximizeSecondaryContent.setOnClickListener( new View.OnClickListener() {
            @Override public void onClick(View v) {
                ((SplitView)findViewById(R.id.split_view)).maximizeSecondaryContent();
            }

        });




    }
}