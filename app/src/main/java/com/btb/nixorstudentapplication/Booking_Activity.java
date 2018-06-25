package com.btb.nixorstudentapplication;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Booking_Activity extends AppCompatActivity {
    private static  Date dateFinal;
    private static Context context;




    int day,month,year;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_);
        getServertime(null);


    }

    public void showDialogue(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFinal);
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                null,
                year,
                month,
                day
        );
        //Min date is post the day the user is looking to book
        dpd.setMinDate(calendar);
        //Max booking can be within a month's time
        calendar.add(Calendar.MONTH,1);
        dpd.setMaxDate(calendar);



    }

    //Method to get time from Firebase since user's time may not be accurate and hence booking system will be affected
    public void getServertime(Context activity){
        context =activity;
      final  FirebaseDatabase ref = FirebaseDatabase.getInstance();
        try {
            ref.getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("getmytime").setValue(ServerValue.TIMESTAMP).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    ref.getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("getmytime").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                            }

                             dateFinal = new Date((long) dataSnapshot.getValue());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public ArrayList<Calendar> getAlldays(String dayname, Date d){
        ArrayList<Calendar> mondays = new ArrayList();
        int dayOfWeek=1;
        switch (dayname){
            case "Monday":
                dayOfWeek = Calendar.MONDAY;
                break;
            case "Tuesday":
                dayOfWeek = Calendar.TUESDAY;
                break;
            case "Wednesday":
                dayOfWeek = Calendar.WEDNESDAY;
                break;
            case "Thursday":
                dayOfWeek = Calendar.THURSDAY;
                break;
            case "Friday":
                dayOfWeek = Calendar.FRIDAY;
                break;

        }


        Calendar cal = new GregorianCalendar();
        cal.setTime(d);
        cal.add(Calendar.DATE,2);
        int month = cal.get(Calendar.MONTH);
        //  cal.set(2015, 0, 1, 0, 0);

        cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        for(int x=0; x<4;x++) {
            Calendar day = Calendar.getInstance();
            day.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE));
            mondays.add(day);
            System.out.println(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 7);
        }

        return  mondays;
    }


}
