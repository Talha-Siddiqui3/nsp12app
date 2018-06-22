package com.btb.nixorstudentapplication.Past_papers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class PdfLoad extends Activity {

common_util common_util = new common_util();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_load);
Intent i=getIntent();
File file=new File(Environment.getExternalStorageDirectory() + "/nixorapp/pastpapers/"+i.getStringExtra("FileName"));
        PDFView pdfView=findViewById(R.id.pdfView);
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

    float rating=-1;
    @Override
    public void onBackPressed() {


        String ratingTitle = getString(R.string.ratingTitle);
        String ratingMessage = getString(R.string.ratingMessage);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.setpaperratingdialogue, null);
        dialogBuilder.setView(dialogView);

        RatingBar ratingBar = dialogView.findViewById(R.id.rating);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating = v;
            }
        });
        Button done = dialogView.findViewById(R.id.done_button);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rating != -1) {

                   // Load_papers load_papers = new Load_papers();
                 //   load_papers.setRating(load_papers.paperOpened.getDocumentID(), rating, PdfLoad.this);

                } else {
                   finish();
                }
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
