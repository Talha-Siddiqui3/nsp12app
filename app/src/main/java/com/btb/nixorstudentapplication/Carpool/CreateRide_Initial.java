package com.btb.nixorstudentapplication.Carpool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btb.nixorstudentapplication.Carpool.Objects.CarpoolInfoObject;
import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import net.igenius.customcheckbox.CustomCheckBox;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import info.hoang8f.android.segmented.SegmentedGroup;

public class CreateRide_Initial extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    //xml
    private common_util cu = new common_util();
    private SegmentedGroup driverType_segmented;
    private RelativeLayout daysSelector;
    private TextView dateTitle;
    private TextView next;
    private ImageView dateCross;
    private Button dateSelectButton;
    private Button timeSelectButton;
    private RadioButton taxiButton;
    private RadioButton privateCarButton;
    private RadioButton iAmTheDriverButton;
    private RadioButton differentDriverButton;
    private RadioButton onceButton;
    private RadioButton repeatedButton;
    private CustomCheckBox dateCheckBox;
    private CustomCheckBox timeCheckBox;
    private CustomCheckBox monday;
    private CustomCheckBox tuesday;
    private CustomCheckBox wednesday;
    private CustomCheckBox thursday;
    private CustomCheckBox friday;
    private CustomCheckBox saturday;
    private CustomCheckBox sunday;
    private TextView Name;
    private TextView ID;
    private TextView contactNumber;

    //DataObject
    private CarpoolInfoObject carpoolInfoObject = new CarpoolInfoObject();
    private HashMap<String, Boolean> daysMap = new HashMap<>();

    //checks
    private boolean isDateAlreadySelected = false;
    private boolean isDaysSelected = false;

    //CLASS TAG
    String TAG = "CreateRideInitial";

    //date and time picker
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;


    //FirebaseRef
    private CollectionReference cr = FirebaseFirestore.getInstance().collection("/Carpool").document("Rides").collection("AvailableRides");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride__initial);
        driverType_segmented = findViewById(R.id.segmented_rideInitial_DriverType);
        daysSelector = findViewById(R.id.days_realtiveLayout);
        dateTitle = findViewById(R.id.Date_Titile);
        next = findViewById(R.id.next_Ride_Initial);
        dateCross = findViewById(R.id.dateCross);
        dateSelectButton = findViewById(R.id.dateSelect);
        timeSelectButton = findViewById(R.id.timeSelect);
        taxiButton = findViewById(R.id.Taxi_Button);
        privateCarButton = findViewById(R.id.PrivateCarButton);
        iAmTheDriverButton = findViewById(R.id.iAmDriver_Button);
        differentDriverButton = findViewById(R.id.differentDriverButton);
        onceButton = findViewById(R.id.Once_Button);
        repeatedButton = findViewById(R.id.Repeated_Button);
        dateCheckBox = findViewById(R.id.date_checkbox);
        timeCheckBox = findViewById(R.id.time_checkbox);
        monday = findViewById(R.id.monday_Checkbox);
        tuesday = findViewById(R.id.tuesday_CheckBox);
        wednesday = findViewById(R.id.wednesday_CheckBox);
        thursday = findViewById(R.id.thursday_Checkbox);
        friday = findViewById(R.id.friday_Checkbox);
        saturday = findViewById(R.id.saturday_CheckBox);
        sunday = findViewById(R.id.sunday_Checkbox);
        next.setOnClickListener(this);
        driverType_segmented.setOnClickListener(this);
        daysSelector.setOnClickListener(this);
        dateSelectButton.setOnClickListener(this);
        timeSelectButton.setOnClickListener(this);
        taxiButton.setOnClickListener(this);
        privateCarButton.setOnClickListener(this);
        iAmTheDriverButton.setOnClickListener(this);
        differentDriverButton.setOnClickListener(this);
        onceButton.setOnClickListener(this);
        repeatedButton.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.PrivateCarButton):
                showDriverType();
                break;
            case (R.id.Taxi_Button):
                hideDriverType();
                break;
            case (R.id.Once_Button):
                isDaysSelected = false;
                hideDays();
                showDate();
                break;
            case (R.id.Repeated_Button):
                isDaysSelected = true;
                showDays();
                hideDate();
                break;
            case (R.id.iAmDriver_Button):
                break;
            case (R.id.differentDriverButton):
                break;
            case R.id.dateSelect:
                getDate();
                break;
            case R.id.timeSelect:
                getTime();
                break;
            case R.id.next_Ride_Initial:
                setDays();
                if (isAllDataComplete()) {
                    setDataToObject();
                } else {
                    cu.showAlertDialogue(this, "Information Incomplete", "Please make sure all field are filled.").show();
                }
        }

    }

    private void setDataToObject() {
        if (taxiButton.isChecked()) {
            carpoolInfoObject.setPrivateCarOrTaxi("taxi");
        } else if (privateCarButton.isChecked()) {
            carpoolInfoObject.setPrivateCarOrTaxi("privateCar");
            if (iAmTheDriverButton.isChecked()) {
                carpoolInfoObject.setiAmTheDriver(true);
            } else {
                carpoolInfoObject.setiAmTheDriver(false);
            }
        }
        if (onceButton.isChecked()) {
            carpoolInfoObject.setOneTimeOrScheduled("once");
        } else if (repeatedButton.isChecked()) {
            carpoolInfoObject.setOneTimeOrScheduled("scheduled");
            carpoolInfoObject.setSelectedDays(setDays());
        }
        setTime();

    }

    private void setTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, second);
        //long selectedTime = calendar.getTimeInMillis() / 1000;
        Date selectedDate = calendar.getTime();
        long selectedTime = selectedDate.getTime();
        Log.i(TAG, String.valueOf(selectedTime));
        carpoolInfoObject.setSelectedTime(selectedTime);
    }

    private boolean isAllDataComplete() {
        boolean isDataComplete = true;
        if (repeatedButton.isChecked()) {
            if (!daysMap.containsValue(true) || !isTimeDataComplete()) {
                isDataComplete = false;
            }
        } else if (onceButton.isChecked()) {
            if (!isDateTimeDataComplete()) {
                isDataComplete = false;
            }
        }

        return isDataComplete;
    }


    private boolean isDateTimeDataComplete() {
        if (dateCheckBox.getVisibility() == View.VISIBLE && timeCheckBox.getVisibility() == View.VISIBLE) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isTimeDataComplete() {
        if (timeCheckBox.getVisibility() == View.VISIBLE) {
            return true;
        } else {
            return false;
        }

    }


    private void getDate() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                CreateRide_Initial.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );


        datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
        isDateAlreadySelected = true;
    }

    private void getTime() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                CreateRide_Initial.this,
                now.get(Calendar.HOUR),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND),
                false
        );
        timePickerDialog.show(getFragmentManager(), "Timepickerdialog");
    }


    private void hideDate() {
        dateTitle.setVisibility(View.GONE);
        dateCheckBox.setVisibility(View.GONE);
        dateSelectButton.setVisibility(View.GONE);
        dateCross.setVisibility(View.GONE);
    }

    private void showDate() {
        dateTitle.setVisibility(View.VISIBLE);
        dateSelectButton.setVisibility(View.VISIBLE);
        if (isDateAlreadySelected) {
            dateCheckBox.setVisibility(View.VISIBLE);
        } else {
            dateCross.setVisibility(View.VISIBLE);
        }
    }


    private void showDays() {
        isDaysSelected = true;
        daysSelector.setVisibility(View.VISIBLE);
    }

    private void hideDays() {
        isDaysSelected = false;
        daysSelector.setVisibility(View.GONE);
    }

    private void showDriverType() {
        driverType_segmented.setVisibility(View.VISIBLE);
    }

    private void hideDriverType() {
        driverType_segmented.setVisibility(View.GONE);
    }


    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        timeCheckBox.setVisibility(View.VISIBLE);
        timeCheckBox.setChecked(true, true);
        timeCheckBox.setClickable(false);
        if (hourOfDay > 12) {
            int tempHour = hourOfDay - 12;
            timeSelectButton.setText(tempHour + " " + minute + " pm");
        } else {
            timeSelectButton.setText(hourOfDay + " " + minute + " am");
        }
        hour = hourOfDay;
        this.minute = minute;
        this.second = second;

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        dateCheckBox.setVisibility(View.VISIBLE);
        dateCheckBox.setChecked(true, true);
        dateCheckBox.setClickable(false);
        dateSelectButton.setText(dayOfMonth + " " + (monthOfYear + 1) + " " + year);//TODO CHANGE DATE TO MONTH NAME
        this.year = year;
        month = monthOfYear;
        day = dayOfMonth;
    }


    private HashMap<String, Boolean> setDays() {


        daysMap.put("Monday", monday.isChecked());
        daysMap.put("Tuesday", tuesday.isChecked());
        daysMap.put("Wednesday", wednesday.isChecked());
        daysMap.put("Thursday", thursday.isChecked());
        daysMap.put("Friday", friday.isChecked());
        daysMap.put("Saturday", saturday.isChecked());
        daysMap.put("Sunday", sunday.isChecked());


        return daysMap;
    }
}
