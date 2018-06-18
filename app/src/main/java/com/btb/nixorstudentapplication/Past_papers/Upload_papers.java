package com.btb.nixorstudentapplication.Past_papers;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Upload_papers {
    public void readDataFromFile(Activity activity) {
        String s;
        ArrayList<String> PastPapersList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(activity.getAssets().open("chem.txt")));

            while ((s = reader.readLine()) != null) {

                PastPapersList.add(s);

//Log.i("PUPU",DataUpload.get(i));
                //i=i+1;
            }


        } catch (IOException e) {
            e.printStackTrace();
            Log.i("ERROR", "ERROR");
        }

        Log.i("COUNT", Integer.toString(PastPapersList.size()));
        StringManipulate(PastPapersList);

    }

    public void StringManipulate(ArrayList<String> Data) {
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
                Log.i("ERROR", Data.get(i));
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


    public void uploadData(ArrayList<String> data, String month[], String year[], String type[],
                           String variant[]) {

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
            Log.i("DONE", "DONE");

        }
    }

}
