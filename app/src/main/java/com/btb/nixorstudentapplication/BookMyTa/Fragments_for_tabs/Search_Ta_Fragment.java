package com.btb.nixorstudentapplication.BookMyTa.Fragments_for_tabs;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.btb.nixorstudentapplication.BookMyTa.Adaptors.RV_Adaptor_3_For_Search_Ta;
import com.btb.nixorstudentapplication.BookMyTa.Main_Activity_Ta_Tab;
import com.btb.nixorstudentapplication.BookMyTa.TA_Object;
import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//Class for displaying List of TAs.

public class Search_Ta_Fragment extends Fragment {
    View view;
    static CollectionReference cr = FirebaseFirestore.getInstance().collection("BookMyTa/BookMyTaDocument/TA Details");
    static RV_Adaptor_3_For_Search_Ta rvAdaptor;
    static Map<String, Object> map = new HashMap();
    static String TAG = "Search_Ta_Fragment";
    private static common_util cu = new common_util();
    static Activity context;
    private static String myYear;

    public Search_Ta_Fragment() {

    }


    public static List<TA_Object> DisplayTa(Query query,boolean initial) {
        final List<TA_Object> ta_objects = new ArrayList<>();
        if (initial) {
            cr.orderBy("Name").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                            TA_Object ta_object = new TA_Object();
                            map = task.getResult().getDocuments().get(i).getData();
                            ta_objects.add(addDataToObject(ta_object, map.get("Name"),
                                    (HashMap<String,Boolean>)map.get("Monday"),(HashMap<String,Boolean>)map.get("Tuesday"),
                                    (HashMap<String,Boolean>)map.get("Wednesday"),(HashMap<String,Boolean>)map.get("Thursday"),
                                    (HashMap<String,Boolean>)map.get("Friday"),
                                    map.get("TaID"), map.get("Subject"), map.get("TaUserName"), map.get("TaYear")));
                            AddDataToAdaptor();

                        }
                    } else {
                        cu.ToasterLong(context, "Error Retreiving data form Server");
                    }

                }
            });

        } else {
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                            TA_Object ta_object = new TA_Object();
                            map = task.getResult().getDocuments().get(i).getData();
                            ta_objects.add(addDataToObject(ta_object, map.get("Name"),
                                    (HashMap<String,Boolean>)map.get("Monday"),(HashMap<String,Boolean>)map.get("Tuesday"),(HashMap<String,Boolean>)map.get("Wednesday"),
                                    (HashMap<String,Boolean>)map.get("Thursday"),(HashMap<String,Boolean>)map.get("Friday"),
                                    map.get("TaID"), map.get("Subject"), map.get("TaUserName"), map.get("TaYear")));
                            AddDataToAdaptor();

                        }
                    } else {
                        cu.ToasterLong(context, "Error Retreiving data form Server");
                    }

                }
            });
        }

        return ta_objects;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_ta, container, false);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.search_ta_rv);
        context = getActivity();
        setYear();
        DisplayTa(null,true);
       rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvAdaptor = new RV_Adaptor_3_For_Search_Ta(DisplayTa(null,true));
        rv.setAdapter(rvAdaptor);
        return view;
    }

    private void setYear() {
        String year = cu.getUserDataLocally(context, "year");
        if (year.equals("2020")) {
            myYear = "AS";
        } else {
            myYear = "A2";
        }
    }

    public static void AddDataToAdaptor() {

        rvAdaptor.notifyDataSetChanged();
    }

    //sets TA Object to data received from Firebase
    public static TA_Object addDataToObject(TA_Object ta_object, Object Ta_Name, HashMap<String,Boolean> Monday,HashMap<String,Boolean> Tuesday,
                                            HashMap<String,Boolean> Wednesday,HashMap<String,Boolean> Thursday,HashMap<String,Boolean> Friday,
                                            Object TaId, Object Subject, Object TaUserName, Object TaYear) {
HashMap<String,Object> testmap=new HashMap<>();

        ta_object.setSubject(Subject.toString());
        ta_object.setTaID(TaId.toString());
        ta_object.setTaName(Ta_Name.toString());
        ta_object.setTaUserName(TaUserName.toString());
        ta_object.setTAYear(TaYear.toString());
        ta_object.setStudentUserName(cu.getUserDataLocally(context, "username"));
        ta_object.setStudentYear(myYear);
        ta_object.setMonday(Monday);
        ta_object.setTuesday(Tuesday);
        ta_object.setWednesday(Wednesday);
        ta_object.setThursday(Thursday);
        ta_object.setFriday(Friday);
        return ta_object;
    }
}
/*
switch (dc.getType()) {
                            case ADDED:
                                map = dc.getDocument().getData();
                                addDataToObject(ta_object, map.get("Name"), map.get("Days"), map.get("TaID"), map.get("Timings"), map.get("Subject"),map.get("TaUserName"), map.get("Student_Year"));
                                AddDataToAdaptor();
                                break;

                            case REMOVED:
                                break;
                            case MODIFIED:
                                break;


                        }

 */