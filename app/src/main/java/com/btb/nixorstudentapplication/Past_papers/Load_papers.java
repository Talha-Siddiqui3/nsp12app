package com.btb.nixorstudentapplication.Past_papers;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


import com.btb.nixorstudentapplication.GeneralLayout.activity_header;
import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.Misc.permission_util;
import com.btb.nixorstudentapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

public class Load_papers extends Activity implements View.OnClickListener {
    public static Context context;
    String TAG = "Load_papers";
    public static ArrayList<paperObject> initialobjectList;
    public static String[] listOfvariants;
    //Filter Query variable.
    public static queryVariable queryVariable = new queryVariable();


    public static String yearSelection = "All";
    public static String monthSelection = "All";
    public static String typeSelection = "All";
    public static String variantSelection = "All";
    public static String subjectname = "Chem";


    //XML
    ImageView FilterButton;
    EditText searchfield;
    activity_header activity_header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_papers);
        activity_header = findViewById(R.id.toolbar_top);
        FilterButton = findViewById(R.id.filterButton);
        searchfield = findViewById(R.id.searchfield);
        FilterButton.setOnClickListener(this);
        GetExternalStoragePermission();
        GetDataFireBase(true);
        initialize(); }



    public void initialize() { activity_header.setActivityname("Pastpapers"); }
    public void addTextWatcher(final Pastpaper_adapter pastpaper_adapter) {
        searchfield.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pastpaper_adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }
    private void GetExternalStoragePermission() {
        permission_util permission_util = new permission_util();
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        permission_util.getPermissions(Load_papers.this, permissions);
    }


    //ArrayList Variable for paper loading
    public static ArrayList<paperObject> paperObjectArrayList = new ArrayList<>();
    public static ArrayList<String> paperUrlArrayList = new ArrayList<>();
    public static ArrayList<String> paperNameArrayList = new ArrayList<>();



    //Initial variable checks to see if its the first call for this method for this particular paper. Which allows it to set and initial value for the query
    //and setup and on change listener so that when the user applies a filter and the query changes, the method is called again to display the new query results


    public void GetDataFireBase(final Boolean intial) {
        paperUrlArrayList = new ArrayList<>();
        paperObjectArrayList = new ArrayList<>();

        final CollectionReference subjectRootCollection = FirebaseFirestore.getInstance().collection("Past Papers/Subjects/" + subjectname);
        Query pastPaperQuery;
        if (intial) {
            pastPaperQuery = subjectRootCollection.orderBy("year");
            //Here giving the queryvariable a value of simple query without any sort
            queryVariable.setBoo(subjectRootCollection);
            //Adding the onChange listener to listen for any changes in the value after the inital query made by the user using the filter
            queryVariable.setListener(new queryVariable.ChangeListener() {
                @Override
                public void onChange() {
                    if (!queryVariable.isBoo().equals(subjectRootCollection)) {
                        Log.i(TAG, "Query Changed by Filter");
                        //The query has been changed by the user and hence the method is called. Think of this onchange method as an independent method
                        //Outside this method. It has only been placed inside this method so that the listener is setup only after the initial call
                        GetDataFireBase(false);
                    }
                }
            });


        } else {
            //Here because we know that this method has not been called for the first time as the value for inital is false. We know the user
            //Changed the Query. We will now be obtaining the new Query from the Query interface and getting data from Firebase accordingly
            pastPaperQuery = queryVariable.isBoo();
        }
        pastPaperQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Map<String, Object> map;
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        map = document.getData();
                        if (map.get("name") != null) {
                            if (!paperUrlArrayList.contains(map.get("name").toString())) {
                                paperObject paperObject = new paperObject();
                                addDataToObject(paperObject, map.get("month"), map.get("year"), map.get("variant"), map.get("type"));
                                if (!isPaperObjectInvalid(paperObject)) {
                                    paperObjectArrayList.add(paperObject);
                                    paperNameArrayList.add(convertObjectToString(paperObject));
                                    paperUrlArrayList.add(map.get("name").toString());
                                    Log.i(TAG, document.getId() + " => " + document.getData());
                                }
                            }
                        }
                    }
                } else {
                    Log.i(TAG, "Error getting documents: ", task.getException());
                }
                initialobjectList = paperObjectArrayList;

                loadPapers(paperObjectArrayList, paperUrlArrayList, paperNameArrayList);
                if (intial) {
                    getVariants(paperObjectArrayList);
                }
                queryVariable.setBoo(subjectRootCollection);

            }
        });


    }

    //A method which converts the object to a string with spaces. Used for Search
    public String convertObjectToString(paperObject paperObject) {
        String name = "";

        if (paperObject.getMonth() != null) {
            name += paperObject.getMonth();
        }

        if (paperObject.getYear() != null) {
            name += " " + paperObject.getYear();
        }

        if (paperObject.getVariant() != null) {
            name += " " + paperObject.getVariant();
        }
        if (paperObject.getType() != null) {
            name += " " + paperObject.getType();
        }
        return name;

    }

    //Method to add adapter to list and initialize a text watcher on the search field
    private void loadPapers(ArrayList<paperObject> mydata, ArrayList<String> Actualnames, ArrayList<String> stringname) {
        RecyclerView rv = findViewById(R.id.rv_list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        Pastpaper_adapter pastpaperadapter = new Pastpaper_adapter(mydata, Load_papers.this, Actualnames, stringname);
        rv.setAdapter(pastpaperadapter);
        if (Actualnames.size() != 0) {
            addTextWatcher(pastpaperadapter);
        }
    }

    //Checks to see if paper Object is valid
    public Boolean isPaperObjectInvalid(paperObject paperObject) {
        if (!paperObject.getMonth().equals("error")) {
            return false;
        }
        if (!paperObject.getType().equals("error")) {
            return false;
        }
        if (!paperObject.getVariant().equals("error")) {
            return false;
        }
        if (!paperObject.getYear().equals("error")) {
            return false;
        }
        return true;
    }

    //Simple meethod to convert data from firebase into a paper object. Add all alteration code here for displaying
    public void addDataToObject(paperObject paperObject, Object month, Object year, Object variant, Object type) {
        if (month != null) {
            paperObject.setMonth(month.toString());
        } else {
            paperObject.setMonth("error");
        }
        if (variant != null) {
            if (variant.toString().equals("1+2+3+4+5+6")) {
                paperObject.setVariant("All");
            } else {
                paperObject.setVariant(variant.toString());
            }

        } else {
            paperObject.setVariant("error");
        }
        if (year != null) {
            paperObject.setYear(year.toString());
        } else {
            paperObject.setYear("error");
        }
        if (type != null) {
            paperObject.setType(type.toString());
        } else {
            paperObject.setType("error");
        }
    }

    //Method to retrieve List of variants from the papers loaded
    public void getVariants(ArrayList<paperObject> paperObject) {
        ArrayList<String> variants = new ArrayList<String>();
        for (paperObject x : paperObject) {

            if (!variants.contains(x.getVariant()) && !x.getVariant().equals("error") && !x.getVariant().equals("All")) {
                variants.add(x.getVariant());
            }

        }
        variants.add("All");
        String[] variantsArray = variants.toArray(new String[0]);
        Arrays.sort((variantsArray), new Comparator<String>() {
            public int compare(String s1, String s2) {
                int first = 0;
                int second = 0;
                if (s1.equals("All")) {
                    first = -12000000;
                } else if (s2.equals("All")) {
                    second = -12000000;

                } else {
                    try {
                        first = Integer.valueOf(s1);
                    } catch (Exception e) {
                        first = -10000000;
                    }
                    try {
                        second = Integer.valueOf(s2);
                    } catch (Exception e) {
                        second = -1000000;
                    }
                }
                return Integer.valueOf(first).compareTo(Integer.valueOf(second));

            }

        });

        listOfvariants = variantsArray;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.filterButton:
                startActivity(new Intent(Load_papers.this, pastpapers_filter.class));
                break;
        }

    }

}