package com.btb.nixorstudentapplication.Carpool;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.btb.nixorstudentapplication.Carpool.Fragments_For_Tabs.Available_Rides;
import com.btb.nixorstudentapplication.R;
import com.google.firebase.firestore.Query;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import net.igenius.customcheckbox.CustomCheckBox;

import java.util.Calendar;
import java.util.HashMap;

public class Rides_Filter extends Activity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    Button apply_button;
    Button timeSelectButton;
    Button dateSelectButton;
    private CustomCheckBox monday;
    private CustomCheckBox tuesday;
    private CustomCheckBox wednesday;
    private CustomCheckBox thursday;
    private CustomCheckBox friday;
    private CustomCheckBox saturday;
    private CustomCheckBox sunday;
    public static queryListener queryListener;
    RelativeLayout driverLayout;
    RelativeLayout dateLayout;
    RelativeLayout daysLayout;
    String[] vehicle;
    String[] driver;
    String[] gender;
    String[] rating;
    String[] rideType;
    Spinner vehicleSpinner;
    Spinner driverSpinner;
    Spinner genderSpinner;
    Spinner ratingSpinner;
    Spinner rideTypeSpinner;
    String vehicleCurrentSelection = "All";
    String ratingCurrentSelection = "All";
    String driverCurrentSelection = "All";
    String genderCurrentSelection = "All";
    String rideTypeCurrentSElection = "All";
    HashMap<String, Boolean> daysMap = new HashMap<>();
    boolean isDateSet = false;
    boolean isTimeSet = false;
    int year;
    int month;
    int day;
    int startHour;
    int startMinute;
    int startSecond;
    int endHour;
    int endMinute;
    int endSecond;

    double selectedTimeStart;
    double selectedTimeEnd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carpool_filter);
        vehicleSpinner = findViewById(R.id.vehcileType_spinner);
        driverSpinner = findViewById(R.id.driver_spinner);
        genderSpinner = findViewById(R.id.gender_spinner);
        ratingSpinner = findViewById(R.id.rating_spinner);
        rideTypeSpinner = findViewById(R.id.rideType_spinner);
        driverLayout = findViewById(R.id.driver_rl_filter);
        dateLayout = findViewById(R.id.date_layout_filter);
        daysLayout = findViewById(R.id.days_realtiveLayout);
        apply_button = findViewById(R.id.apply_button_carpoolfilter);
        monday = findViewById(R.id.monday_Checkbox);
        tuesday = findViewById(R.id.tuesday_CheckBox);
        wednesday = findViewById(R.id.wednesday_CheckBox);
        thursday = findViewById(R.id.thursday_Checkbox);
        friday = findViewById(R.id.friday_Checkbox);
        saturday = findViewById(R.id.saturday_CheckBox);
        sunday = findViewById(R.id.sunday_Checkbox);
        apply_button.setOnClickListener(this);
        timeSelectButton = findViewById(R.id.timeSelectButton_FilterCarpool);
        timeSelectButton.setOnClickListener(this);
        dateSelectButton = findViewById(R.id.dateSelectButton_FilterCarpool);
        dateSelectButton.setOnClickListener(this);

        vehicle = new String[]{"All", "Private", "Taxi"};
        driver = new String[]{"All", "Me", "Other"};
        gender = new String[]{"All", "Male", "Female"};
        rating = new String[]{"All", "1", "2", "3", "4", "5"};
        rideType = new String[]{"All", "Once", "Scheduled"};

        populateSpinner(vehicleSpinner, vehicle);
        populateSpinner(driverSpinner, driver);
        populateSpinner(genderSpinner, gender);
        populateSpinner(ratingSpinner, rating);
        populateSpinner(rideTypeSpinner, rideType);

        vehicleSpinner.setSelection(0);
        driverSpinner.setSelection(0);
        genderSpinner.setSelection(0);
        ratingSpinner.setSelection(0);
        rideTypeSpinner.setSelection(0);


        vehicleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                vehicleCurrentSelection = vehicle[i];
                if (vehicleCurrentSelection.equals("All") || vehicleCurrentSelection.equals("Taxi")) {
                    hideDriverType();
                } else {
                    showDriverType();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        driverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                driverCurrentSelection = driver[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                genderCurrentSelection = gender[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ratingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ratingCurrentSelection = rating[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rideTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                rideTypeCurrentSElection = rideType[i];
                if (rideTypeCurrentSElection.equals("Once")) {
                    showDate();
                    showTimeButton();
                    hideDays();
                } else if (rideTypeCurrentSElection.equals("Scheduled")) {
                    showTimeButton();
                    showDays();
                    hideDate();
                } else {
                    hideDate();
                    hideTimeButton();
                    hideDays();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


    public Query addOrderBy(Query query, String data) {
        return query.orderBy(data);
    }

    public Query addEquals(Query query, String data, Object value) {
        return query.whereEqualTo(data, value);
    }

    public Query buildQuery() {
        Query query;
        if (rideTypeCurrentSElection.equals("ALL")) {
            query = Available_Rides.initialQuery;
        } else {
            query = Available_Rides.ridesCr;
        }
        if (vehicleCurrentSelection.equals("All") && ratingCurrentSelection.equals("All") &&
                driverCurrentSelection.equals("All") && genderCurrentSelection.equals("All") &&
                rideTypeCurrentSElection.equals("All")) {
            return query;
        }
        if (!vehicleCurrentSelection.equals("All")) {
            if (vehicleCurrentSelection.equals("Private")) {
                query = addEquals(query, "privateCarOrTaxi", "privateCar");
                if (driverCurrentSelection.equals("Me")) {
                    query = addEquals(query, "iAmTheDriver", true);
                } else {
                    query = addEquals(query, "iAmTheDriver", false);
                }
            } else {
                query = addEquals(query, "privateCarOrTaxi", "taxi");
            }
        }

        if (rideTypeCurrentSElection.equals("Scheduled")) {

            HashMap<String, Boolean> tempMap = setDays();
            query = addEquals(query, "selectedDays.Monday", tempMap.get("Monday"));
            query = addEquals(query, "selectedDays.Tuesday", tempMap.get("Tuesday"));
            query = addEquals(query, "selectedDays.Wednesday", tempMap.get("Wednesday"));
            query = addEquals(query, "selectedDays.Thursday", tempMap.get("Thursday"));
            query = addEquals(query, "selectedDays.Friday", tempMap.get("Friday"));
            query = addEquals(query, "selectedDays.Saturday", tempMap.get("Saturday"));
            query = addEquals(query, "selectedDays.Sunday", tempMap.get("Sunday"));
            if (isTimeSet) {
                setDateAndTime();
                query = query.whereGreaterThanOrEqualTo("selectedTime", selectedTimeStart);
                query = query.whereLessThanOrEqualTo("selectedTime", selectedTimeEnd);
                query = addOrderBy(query, "selectedTime");
            }
        } else {
            if (isDateSet && isTimeSet) {
                setDateAndTime();
                query = query.whereGreaterThanOrEqualTo("selectedTime", selectedTimeStart);
                query = query.whereLessThanOrEqualTo("selectedTime", selectedTimeEnd);
                query = addOrderBy(query, "selectedTime");
                Log.i("123465789", selectedTimeStart + "  " + selectedTimeEnd);
            }
        }

        return query;
    }


    private void showDriverType() {
        driverLayout.setVisibility(View.VISIBLE);
    }

    private void showDate() {
        dateLayout.setVisibility(View.VISIBLE);
    }

    private void showDays() {
        daysLayout.setVisibility(View.VISIBLE);
    }

    private void hideDriverType() {
        driverLayout.setVisibility(View.GONE);
        driverCurrentSelection = "All";
    }

    private void hideDate() {
        dateLayout.setVisibility(View.GONE);
    }

    private void hideDays() {
        daysLayout.setVisibility(View.GONE);

    }

    private void showTimeButton() {
        timeSelectButton.setVisibility(View.VISIBLE);
    }

    private void hideTimeButton() {
        timeSelectButton.setVisibility(View.GONE);
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.apply_button_carpoolfilter) {
            Query query = buildQuery();
            queryListener.getQuery(query);
            finish();
        } else if (view.getId() == R.id.dateSelectButton_FilterCarpool) {
            getDate();
        } else if (view.getId() == R.id.timeSelectButton_FilterCarpool) {
            getTime("Select start time");
        }
    }


    private void getDate() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show(getFragmentManager(), "Datepickerdialog");

    }

    private void getTime(String tag) {
        Calendar now = Calendar.getInstance();
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND),
                false
        );
        timePickerDialog.setTitle(tag);
        timePickerDialog.show(getFragmentManager(), tag);
    }


    private void populateSpinner(Spinner spinner, String[] data) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        isDateSet = true;
        this.year = year;
        month = monthOfYear;
        day = dayOfMonth;
    }

    private void setDateAndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, startHour, startMinute, startSecond);
        double selectedTime = calendar.getTimeInMillis() / 1000;
        selectedTimeStart = selectedTime;
        calendar.set(year, month, day, endHour, endMinute, endSecond);
        selectedTime = calendar.getTimeInMillis() / 1000;
        selectedTimeEnd = selectedTime;
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        if (view.getTag().equals("Select start time")) {
            startHour = hourOfDay;
            this.startMinute = minute;
            this.startSecond = second;
            getTime("Select end time");
        } else {
            endHour = hourOfDay;
            endMinute = minute;
            endSecond = second;
            isTimeSet = true;
        }
    }
}
