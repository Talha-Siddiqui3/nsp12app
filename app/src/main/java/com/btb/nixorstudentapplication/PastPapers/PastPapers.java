package com.btb.nixorstudentapplication.PastPapers;

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
        GetDataFireBase();
        }


        public void readDataFromFile () {
            String s;
            ArrayList<String> PastPapersList = new ArrayList<>();
            int i = 0;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("123.txt")));

                while (reader.readLine() != null) {
                    s = reader.readLine();
                    PastPapersList.add(s);

//Log.i("PUPU",DataUpload.get(i));
                    //i=i+1;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

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
                FormattedData.add(Data.get(i).substring(0, 6) + "_" + Data.get(i).substring(6, Data.get(i).length()));
                s = FormattedData.get(i).split("_");
                s[s.length - 1] = s[s.length - 1].replace(".pdf", "");

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


            }
        }


        ArrayList<String> FbQueryData = new ArrayList<>();
    public void GetDataFireBase () {

            CollectionReference cr = FirebaseFirestore.getInstance().collection("Past Papers/Subjects/Chem");
            Query ppQuery = cr.whereEqualTo("variant", "22").orderBy("year");


            ppQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    Map<String, Object> map;
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            map = document.getData();
                            FbQueryData.add(map.get("year").toString() + " " + (map.get("month").toString()) + " " + (map.get("type").toString()) + " " +
                                    (map.get("variant").toString()));

                            Log.i("abc", document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.i("abc", "Error getting documents: ", task.getException());
                    }
                    loadpapers(FbQueryData);
                }
            });


        }


        ArrayList<String> pasptpaer;
        public void getPastpaperNames () {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            db.addValueEventListener(new ValueEventListener() {
                ArrayList<String> pastpapers = new ArrayList<>();

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //String name = dataSnapshot.getValue().toString();
                /*for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("FIELD1").getValue().toString();
                    pastpapers.add(name);
                }
                loadpapers(pastpapers);

            */
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


        private void loadpapers (ArrayList < String > mydata) {
            RecyclerView rv = findViewById(R.id.rv_list);
            rv.setLayoutManager(new LinearLayoutManager(this));
            RvAdaptor rvAdaptor = new RvAdaptor(mydata, PastPapers.this);
            rv.setAdapter(rvAdaptor);
        }

    }
