
package com.btb.nixorstudentapplication.Past_papers;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btb.nixorstudentapplication.GeneralLayout.SplitView;
import com.btb.nixorstudentapplication.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class MultiView extends Activity {
    private Button reset;
    private Button mMaximizePrimaryContent;
    private Button mMaximizeSecondaryContent;
    private Button back_button;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_view);
        reset = findViewById(R.id.reset);
        final PDFView pdfView=findViewById(R.id.pdfViewTop);
        final PDFView pdfView2 =findViewById(R.id.pdfViewBottom);
         back_button =findViewById(R.id.back_button);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height_screen = displayMetrics.heightPixels/2;
        init(height_screen);

        mMaximizePrimaryContent = (Button)findViewById(R.id.maximize_primary);
        mMaximizePrimaryContent.setOnClickListener( new View.OnClickListener() {
            @Override public void onClick(View v) {
                pdfView.resetZoom();
                ((SplitView)findViewById(R.id.split_view)).maximizePrimaryContent();
            }

        });

        mMaximizeSecondaryContent = (Button)findViewById(R.id.maximize_secondary);
        mMaximizeSecondaryContent.setOnClickListener( new View.OnClickListener() {
            @Override public void onClick(View v) {
                pdfView2.resetZoom();
                ((SplitView)findViewById(R.id.split_view)).maximizeSecondaryContent();
            }

        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SplitView)findViewById(R.id.split_view)).setPrimaryContentHeight(height_screen);
           if(pdfView!=null){
               pdfView.resetZoom();
           }
                if(pdfView2!=null){
                    pdfView2.resetZoom();
                }
            }
        });


        Intent i=getIntent();
        File file=new File(Environment.getExternalStorageDirectory() + "/nixorapp/pastpapers/"+i.getStringExtra("firstPaper"));



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


        File file2=new File(Environment.getExternalStorageDirectory() + "/nixorapp/pastpapers/"+i.getStringExtra("secondPaper"));



        pdfView2.fromFile(file2)
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

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MultiView.super.onBackPressed();
            }
        });

    }
    public void init(int height){
        LinearLayout primary = findViewById(R.id.primary);
        ViewGroup.LayoutParams lp = primary.getLayoutParams();
        lp.height = height;
        primary.setLayoutParams(lp);
    }






}