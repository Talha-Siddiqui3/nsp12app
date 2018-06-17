package com.btb.nixorstudentapplication.PastPapers;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.btb.nixorstudentapplication.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class PdfLoad extends AppCompatActivity {





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
}
