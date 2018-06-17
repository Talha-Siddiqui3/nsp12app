package com.btb.nixorstudentapplication.PastPapers;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import com.btb.nixorstudentapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PastPapers extends AppCompatActivity {
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_papers);
//sadfasf
        GetDataFireBase();
        GetExternalStoragePermission();

        }

    private void GetExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
// No explanation needed; request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE},1);


        }
    }


    public void readDataFromFile () {
            String s;
            ArrayList<String> PastPapersList = new ArrayList<>();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("chem.txt")));

                while ((s=reader.readLine()) != null) {

                    PastPapersList.add(s);

//Log.i("PUPU",DataUpload.get(i));
                    //i=i+1;
                }


            } catch (IOException e) {
                e.printStackTrace();
                Log.i("ERROR","ERROR");
            }

        Log.i("COUNT", Integer.toString(PastPapersList.size()));
            StringManipulate(PastPapersList);

        }

        public void StringManipulate (ArrayList < String > Data) {
            String month[] = new String[Data.size() + 1];
            String type[] = new String[Data.size() + 1];
            String variant[] = new String[Data.size() + 1];
            ;
            String year[] = new String[Data.size() + 1];
            String s[] = new String[Data.size() + 1];
            ArrayList<String> FormattedData = new ArrayList<>();

    for (int i = 0; i < Data.size(); i++) {

      try {
           FormattedData.add(Data.get(i).substring(0, 6) + "_" + Data.get(i).substring(6, Data.get(i).length()));
        s = FormattedData.get(i).split("_");
        s[s.length - 1] = s[s.length - 1].replace(".pdf", "");
      }
       catch(Exception e){
          Log.i("ERROR",Data.get(i));
       }

        if (s.length == 4) {
            if (s[3].compareTo("gt") == 0) {
                s[3] = "Grade Threeshold";

            } else if (s[3].compareTo("ci") == 0) {
                s[3] = "Condifential Instructions";
            } else if (s[3].compareTo("er") == 0) {
                s[3] = "Examiner Report";

            }
            switch (s[1]) {
                case "m":
                    s[1] = "March";
                    break;
                case "s":
                    s[1] = "Summer";
                    break;
                case "w":
                    s[1] = "Winter";
                    break;
                default:
                    s[1] = "error";
                    break;
            }
            s[2] = "20" + s[2];

            type[i] = s[3];
            year[i] = s[2];
            month[i] = s[1];
            variant[i] = null;
            //9701_m_18_gt.pdf
        } else if (s.length == 5) {
            switch (s[1]) {
                case "m":
                    s[1] = "March";
                    break;
                case "s":
                    s[1] = "Summer";
                    break;
                case "w":
                    s[1] = "Winter";
                    break;
                default:
                    s[1] = "error";
                    break;
            }


            s[2] = "20" + s[2];

            if (s[3].compareTo("ms") == 0) {
                s[3] = "Marking Scheme";

            } else if (s[3].compareTo("qp") == 0) {
                s[3] = "Question Paper";

            }

            // 9701_m_16_ms_33.pdf
            type[i] = s[3];
            year[i] = s[2];
            month[i] = s[1];
            variant[i] = s[4];
        }


    }











            uploadData(Data, month, year, type, variant);
        }


        public void uploadData (ArrayList < String > data, String month[],String year[], String type[],
        String variant[]){

            CollectionReference cr = FirebaseFirestore.getInstance().collection("Past Papers/Subjects/Chem");
            //hashmap moved outside loop
            HashMap<String, Object> map = new HashMap<String, Object>();
            for (int x = 0; x < data.size(); x++) {

                map.put("name", data.get(x));
                map.put("month", month[x]);
                map.put("year", year[x]);
                map.put("type", type[x]);
                map.put("variant", variant[x]);

                cr.add(map);
Log.i("DONE","DONE");

            }
        }


        ArrayList<String> FbQueryData = new ArrayList<>();
    ArrayList<String> Actualname = new ArrayList<>();
    public void GetDataFireBase () {

            CollectionReference cr = FirebaseFirestore.getInstance().collection("Past Papers/Subjects/Chem");
            Query ppQuery = cr.whereEqualTo("variant","22").orderBy("year").orderBy("month").orderBy("type");



            ppQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    Map<String, Object> map;
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            map = document.getData();
                            if (map.get("variant") == null) {
                                FbQueryData.add(map.get("year").toString() + " " + (map.get("month").toString()) + " " + (map.get("type").toString()));
                            }

                            else{
                                FbQueryData.add(map.get("year").toString() + " " + (map.get("month").toString()) + " " + (map.get("type").toString()) + " " +
                                        (map.get("variant").toString()));


                            }
                            Actualname.add(map.get("name").toString());
                            Log.i("abc", document.getId() + " => " + document.getData());
                        }

                    }






                     else {
                        Log.i("abc", "Error getting documents: ", task.getException());
                    }
                    loadpapers(FbQueryData,Actualname);
                }
            });


        }





        private void loadpapers (ArrayList < String > mydata,ArrayList<String> Actualnames) {
            RecyclerView rv = findViewById(R.id.rv_list);
            rv.setLayoutManager(new LinearLayoutManager(this));
            RvAdaptor rvAdaptor = new RvAdaptor(mydata, PastPapers.this,Actualnames);
            rv.setAdapter(rvAdaptor);
        }





}