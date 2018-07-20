package com.btb.nixorstudentapplication.BookMyTa;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

import com.btb.nixorstudentapplication.BookMyTa.Fragments_for_tabs.Search_Ta_Fragment;
import com.btb.nixorstudentapplication.Past_papers.PaperFilter;
import com.btb.nixorstudentapplication.R;
import com.google.firebase.firestore.Query;

public class TA_Filter extends Activity implements View.OnClickListener {
    String TAG = "TA_Filter";
    Button apply_button;
    String[] years;
    String[] rating;
    String[] subjects;
    String[] classTeachers;
    Spinner yearSpinner;
    Spinner ratingSpinner;
    Spinner subjectSpinner;
    Spinner classTeacherSpinner;
    String yearsCurrentSelection;
    String ratingCurrentSelection;
    String subjectsCurrentSelection;
    String classTeachersCurrentSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmyta_filter);
        apply_button = findViewById(R.id.apply_button_bookmyta);
        yearSpinner = findViewById(R.id.yearspinner_bookmyta);
        ratingSpinner=findViewById(R.id.ratingspinner_bookmyta);
        subjectSpinner=findViewById(R.id.subjectspinner_bookmyta);
        classTeacherSpinner=findViewById(R.id.teacherspinner_bookmyta);
        years = new String[]{"All","AS", "A2"};
        rating=new String[]{"All","Top"};
        subjects=new String[]{"All","Mathematics","Physics","Chemistry","Computer Science"};//TODO: CHANGE THESE
        classTeachers=new String[]{"All","Miss Aaila","Sir Naushad","Sir Rizwan","Sir Nasir","Sir Raja"};
        populateSpinner(yearSpinner, years);
        populateSpinner(ratingSpinner, rating);
        populateSpinner(subjectSpinner, subjects);
        populateSpinner(classTeacherSpinner, classTeachers);
        yearSpinner.setSelection(0);
        ratingSpinner.setSelection(0);
        subjectSpinner.setSelection(0);
        classTeacherSpinner.setSelection(0);

yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        yearsCurrentSelection=years[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
});

ratingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
ratingCurrentSelection=rating[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
});

subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
subjectsCurrentSelection=subjects[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
});

classTeacherSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
       classTeachersCurrentSelection=classTeachers[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
});




    }







    public Query addOrderBy(Query query, String data){
        return query.orderBy(data);
    }

    public Query addEquals(Query query,String data, String value){
        return query.whereEqualTo(data,value);
    }
    public Query buildQuery(){
        Query query=null;
if(yearsCurrentSelection.equals("All")&& ratingCurrentSelection.equals("All")&&subjectsCurrentSelection.equals("All")
        &&classTeachersCurrentSelection.equals("All")){
    return null;
}
if(yearsCurrentSelection.equals("All")){
    addOrderBy(query,"TaYear");
}
else{
    addEquals(query,"TaYear",yearsCurrentSelection);
}
return query;
    }



    public void populateSpinner(Spinner spinner, String[] data) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.apply_button_bookmyta:
                Search_Ta_Fragment.DisplayTa(buildQuery(),false);
                finish();
        }
    }
}
