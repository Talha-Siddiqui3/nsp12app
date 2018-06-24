package com.btb.nixorstudentapplication.Past_papers;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.btb.nixorstudentapplication.R;
import com.google.firebase.firestore.Query;

public class PaperFilter extends Activity implements View.OnClickListener {

    String TAG = "PaperFilter";
    MainPPActivity mainPPActivity;
    Button apply_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pastpapers_filter);
        apply_button=findViewById(R.id.apply_button);

        apply_button.setOnClickListener(this);

        mainPPActivity = new MainPPActivity();


        final String[] years = PaperFilter.this.getResources().getStringArray(R.array.listofyears);
        final String[] variants = mainPPActivity.listOfvariants;
        final String[] type = PaperFilter.this.getResources().getStringArray(R.array.listtype);
        final String[] month = PaperFilter.this.getResources().getStringArray(R.array.listofmonths);

        Spinner yearspinner = findViewById(R.id.yearspinner);
        final Spinner variantspinner = findViewById(R.id.variantspinner);
        final Spinner typespinner = findViewById(R.id.typespinner);
        Spinner monthspinner = findViewById(R.id.monthspinner);
        populateSpinner(yearspinner, years);
        populateSpinner(variantspinner, variants);
        populateSpinner(typespinner, type);
        populateSpinner(monthspinner, month);

        yearspinner.setSelection(java.util.Arrays.asList(years).indexOf(mainPPActivity.yearSelection));
        variantspinner.setSelection(java.util.Arrays.asList(variants).indexOf(mainPPActivity.variantSelection));
        monthspinner.setSelection(java.util.Arrays.asList(month).indexOf(mainPPActivity.monthSelection));
        typespinner.setSelection(java.util.Arrays.asList(type).indexOf(mainPPActivity.typeSelection));

        yearspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(years[position].equals("From: To: ")){


                }else{
                mainPPActivity.yearSelection = years[position];
                Log.i(TAG, mainPPActivity.yearSelection);}



            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        monthspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                mainPPActivity.monthSelection = month[position];
                Log.i(TAG, mainPPActivity.monthSelection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        variantspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                mainPPActivity.variantSelection = variants[position];
                Log.i(TAG, mainPPActivity.variantSelection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        typespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
               if(type[position].equals("Grade Threshold")){
                   variantspinner.setEnabled(false);
                   mainPPActivity.variantSelection = "All";

               }else{
                   variantspinner.setEnabled(true);
                mainPPActivity.typeSelection = type[position];
                Log.i(TAG, mainPPActivity.typeSelection);
            }}

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    public Query addOrderBy(Query query,String data){
        return query.orderBy(data);
}

    public Query addEquals(Query query,String data, String value){
           return query.whereEqualTo(data,value);
    }
    public Query buildQuery(Query query) {
        Query queryReturn=query;
        if(mainPPActivity.yearSelection.equals("All")&& mainPPActivity.monthSelection.equals("All")
                && mainPPActivity.typeSelection.equals("All")&& mainPPActivity.variantSelection.equals("All")) {
            return queryReturn.orderBy("year"); }
        if(mainPPActivity.yearSelection.equals("All")){
            queryReturn=   addOrderBy(queryReturn,"year");
        }else{
            queryReturn=  addEquals(queryReturn,"year", mainPPActivity.yearSelection);
        }
        if(mainPPActivity.monthSelection.equals("All")){
            queryReturn=addOrderBy(queryReturn,"month");
        }else{
         switch (mainPPActivity.monthSelection){
             case "May/June":queryReturn= addEquals(queryReturn,"month","Summer");break;
             case "October/November":queryReturn= addEquals(queryReturn,"month","Winter");break;
             case "February/March":queryReturn= addEquals(queryReturn,"month","March");break;
             default:queryReturn= addEquals(queryReturn,"month", mainPPActivity.monthSelection);
         }
        }
        if(mainPPActivity.typeSelection.equals("All")){
            queryReturn=   addOrderBy(queryReturn,"type");
        }else{
            queryReturn=  addEquals(queryReturn,"type", mainPPActivity.typeSelection);
        }
        if(mainPPActivity.variantSelection.equals("All")){
            queryReturn=    addOrderBy(queryReturn,"variant");
        }
        else{
            queryReturn=  addEquals(queryReturn,"variant", mainPPActivity.variantSelection);
        }

        return queryReturn;


    }

    public void populateSpinner(Spinner spinner, String[] data) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(PaperFilter.this,
                        android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.apply_button:setMyQuery();break;
        }
    }
    public void setMyQuery(){


        mainPPActivity.queryVariable.setBoo(buildQuery(mainPPActivity.queryVariable.isBoo()));
        finish();

    }
}
