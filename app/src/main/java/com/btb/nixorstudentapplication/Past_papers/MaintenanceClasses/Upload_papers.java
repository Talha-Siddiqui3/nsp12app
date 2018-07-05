package com.btb.nixorstudentapplication.Past_papers.MaintenanceClasses;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Upload_papers {
    static String TAG = "Upload_papers";

    public static void readDataFromFile(Activity activity,String subjectName) {
        String s;
        ArrayList<String> PastPapersList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(activity.getAssets().open("Past Papers txt documents/"+subjectName+".txt")));

            while ((s = reader.readLine()) != null) {

                PastPapersList.add(s);
            }


        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "ERROR");
        }

        Log.i(TAG, Integer.toString(PastPapersList.size()));
        StringManipulate(PastPapersList,subjectName);

    }

    public static void StringManipulate(ArrayList<String> Data,String subjectName) {
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
            } catch (Exception e) {
                Log.i(TAG, Data.get(i));
            }

            if (s.length == 4) {
                if (s[3].compareTo("gt") == 0) {
                    s[3] = "Grade Threshold";

                } else if (s[3].compareTo("ci") == 0) {
                    s[3] = "Confidential Instructions";
                } else if (s[3].compareTo("er") == 0 || s[3].compareTo("er") == 0) {
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


        uploadData(Data, month, year, type, variant,subjectName);
    }


    public static void uploadData(ArrayList<String> data, String month[], String year[], String type[],
                                  String variant[],String subjectName) {

        CollectionReference cr = FirebaseFirestore.getInstance().collection("Past Papers/Subjects/"+subjectName);
        //hashmap moved outside loop
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (int x = 0; x < data.size(); x++) {

            map.put("name", data.get(x));
            map.put("month", month[x]);
            map.put("year", year[x]);
            map.put("type", type[x]);
            map.put("variant", variant[x]);

            cr.add(map);
            Log.i(TAG, "DONE");

        }
    }


    public void uploadData(Map<String, Object> map, String doc) {

        DocumentReference cr = FirebaseFirestore.getInstance().collection("Past Papers/Subjects/Chemfdaf").document(doc);
        //hashmap moved outside loop

        cr.update(map);
        Log.i(TAG, "DONE");
    }

    ArrayList<DocumentSnapshot> errordocs = new ArrayList();

    public void changeErrors() {
        for (DocumentSnapshot documentSnapshot : errordocs) {


        }


    }

    ArrayList<String> years = new ArrayList<>();
    ArrayList<String> types = new ArrayList<>();
    ArrayList<String> variants = new ArrayList<>();
    ArrayList<String> months = new ArrayList<>();

    public void repair(final String changeValue, final String oldvalue, final String newValue) {
        final CollectionReference cr = FirebaseFirestore.getInstance().collection("Past Papers/Subjects/Chem");
        cr.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Map<String, Object> map;
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        map = document.getData();

                        if (map.get("name") != null) {
                            if (map.get(changeValue) != null) {
                                if (map.get(changeValue).equals(oldvalue)) {
                                    map.put(changeValue, newValue);
                                    uploadData(map, document.getId());
                                    Log.i(TAG, map.get("name").toString());
                                }

                            }
                        }
                    }


                }
            }
        });
    }


    public void myRepairMethod() {
        final CollectionReference cr = FirebaseFirestore.getInstance().collection("Past Papers/Subjects/Chem");
        cr.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Map<String, Object> map;
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        map = document.getData();
                        try {
                            if (map.get("year") != null) {
                                if (!years.contains(map.get("year").toString())) {
                                    years.add(map.get("year").toString());
                                }
                            }
                            if (map.get("type") != null) {
                                if (!types.contains(map.get("type").toString())) {
                                    types.add(map.get("type").toString());
                                }
                            }
                            if (map.get("month") != null) {
                                if (!months.contains(map.get("mont").toString())) {
                                    months.add(map.get("month").toString());
                                }
                            }
                            if (map.get("variant") != null) {
                                if (!variants.contains(map.get("variant").toString())) {
                                    variants.add(map.get("variant").toString());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }/*
                        if (map.get("name") != null) {
                            if(map.get("type")!=null){
                            if(map.get("type").equals("sp")) {
                               map.put("type","Specimen Paper");
                             uploadData(map,document.getId());
                             Log.i(TAG,map.get("name").toString());
                            }

                        }}*/
                    }
                    for (String x : years) {
                        Log.i(TAG, x);
                    }

                    for (String x : variants) {
                        Log.i(TAG, x);
                    }

                    for (String x : types) {
                        Log.i(TAG, x);
                    }

                    for (String x : months) {
                        Log.i(TAG, x);
                    }


                }
            }
        });
    }


}

