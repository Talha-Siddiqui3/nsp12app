package com.btb.nixorstudentapplication.Past_papers;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.btb.nixorstudentapplication.R;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class pastpapers_filter extends Activity implements View.OnClickListener {

    String TAG = "pastpapers_filter";
    Load_papers load_papers;
    Button apply_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pastpapers_filter);
        apply_button=findViewById(R.id.apply_button);

        apply_button.setOnClickListener(this);

        load_papers = new Load_papers();


        final String[] years = pastpapers_filter.this.getResources().getStringArray(R.array.listofyears);
        final String[] variants = load_papers.listOfvariants;
        final String[] type = pastpapers_filter.this.getResources().getStringArray(R.array.listtype);
        final String[] month = pastpapers_filter.this.getResources().getStringArray(R.array.listofmonths);

        Spinner yearspinner = findViewById(R.id.yearspinner);
        final Spinner variantspinner = findViewById(R.id.variantspinner);
        Spinner typespinner = findViewById(R.id.typespinner);
        Spinner monthspinner = findViewById(R.id.monthspinner);
        populateSpinner(yearspinner, years);
        populateSpinner(variantspinner, variants);
        populateSpinner(typespinner, type);
        populateSpinner(monthspinner, month);

        yearspinner.setSelection(java.util.Arrays.asList(years).indexOf(load_papers.yearSelection));
        variantspinner.setSelection(java.util.Arrays.asList(variants).indexOf(load_papers.variantSelection));
        monthspinner.setSelection(java.util.Arrays.asList(month).indexOf(load_papers.monthSelection));
        typespinner.setSelection(java.util.Arrays.asList(type).indexOf(load_papers.typeSelection));

        yearspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(years[position].equals("From: To: ")){


                }else{
                load_papers.yearSelection = years[position];
                Log.i(TAG, load_papers.yearSelection);}



            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        monthspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                load_papers.monthSelection = month[position];
                Log.i(TAG, load_papers.monthSelection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        variantspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                load_papers.variantSelection = variants[position];
                Log.i(TAG, load_papers.variantSelection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        typespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                load_papers.typeSelection = type[position];
                Log.i(TAG, load_papers.typeSelection);
            }

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
        if(load_papers.yearSelection.equals("All")&&load_papers.monthSelection.equals("All")
                &&load_papers.typeSelection.equals("All")&&load_papers.variantSelection.equals("All")) {
            return queryReturn.orderBy("year"); }
        if(load_papers.yearSelection.equals("All")){
            queryReturn=   addOrderBy(queryReturn,"year");
        }else{
            queryReturn=  addEquals(queryReturn,"year",load_papers.yearSelection);
        }
        if(load_papers.monthSelection.equals("All")){
            queryReturn=addOrderBy(queryReturn,"month");
        }else{
         switch (load_papers.monthSelection){
             case "May/June":queryReturn= addEquals(queryReturn,"month","Summer");break;
             case "October/November":queryReturn= addEquals(queryReturn,"month","Winter");break;
             case "February/March":queryReturn= addEquals(queryReturn,"month","March");break;
             default:queryReturn= addEquals(queryReturn,"month",load_papers.monthSelection);
         }
        }
        if(load_papers.typeSelection.equals("All")){
            queryReturn=   addOrderBy(queryReturn,"type");
        }else{
            queryReturn=  addEquals(queryReturn,"type",load_papers.typeSelection);
        }
        if(load_papers.variantSelection.equals("All")){
            queryReturn=    addOrderBy(queryReturn,"variant");
        }
        else{
            queryReturn=  addEquals(queryReturn,"variant",load_papers.variantSelection);
        }

        return queryReturn;


    }

    public void populateSpinner(Spinner spinner, String[] data) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(pastpapers_filter.this,
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


        load_papers.queryVariable.setBoo(buildQuery(load_papers.queryVariable.isBoo()));
        finish();

    }
}
