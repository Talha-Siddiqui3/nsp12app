
package com.btb.nixorstudentapplication.Past_papers;

import android.content.ClipData;
import android.content.Intent;
import android.os.Environment;
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
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

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
        Intent i=getIntent();
        loadPaper(i.getStringExtra("firstPaper"),true);
        loadPaper(i.getStringExtra("secondPaper"),true);


    }
    public void loadPaper(String filename, Boolean top){
        PDFView pdfView;
        File file=new File(Environment.getExternalStorageDirectory() + "/nixorapp/pastpapers/"+filename);

        if(top){
       pdfView=findViewById(R.id.pdfViewTop);}else{
          pdfView=findViewById(R.id.pdfViewBottom);
       }
        pdfView.fromFile(file)
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                // allows to draw something on the current page, usually visible in the middle of the screen
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                .load();

    }
}