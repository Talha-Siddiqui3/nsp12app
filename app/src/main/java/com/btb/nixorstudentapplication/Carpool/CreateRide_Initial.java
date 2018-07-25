package com.btb.nixorstudentapplication.Carpool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateRide_Initial extends AppCompatActivity implements WheelPicker.OnItemSelectedListener {


    // Wheel scrolled flag
    private WheelPicker wheelHour;
    private WheelPicker wheelMinute;
    private WheelPicker wheelTimeSuffix;
    private common_util cu = new common_util();
    List<Integer> hours = new ArrayList<>();
    List<String> minutes = new ArrayList<>();
    List<String> timeSuffix = new ArrayList<>(Arrays.asList(new String[]{"AM", "PM"}));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride__initial);
        wheelHour = findViewById(R.id.wheel_hour);
        wheelMinute = findViewById(R.id.wheel_minute);
        wheelTimeSuffix = findViewById(R.id.wheel_time_suffix);
        wheelTimeSuffix.setData(timeSuffix);
        addHours();
        addMinutes();

    }

    private void addMinutes() {
        for (int i = 0; i < 60; i += 5) {
            if (i == 0 || i == 5) {
                minutes.add("0" + String.valueOf(i));
            } else {
                minutes.add(String.valueOf(i));
            }
        }
        wheelMinute.setData(minutes);
    }

    public void addHours() {
        for (int i = 0; i < 12; i++) {
            hours.add(i);
        }
        wheelHour.setData(hours);
    }

    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
        String text = "";
        switch (picker.getId()) {
            case R.id.wheel_hour:
                text = "Center:";
                break;
        }
        cu.ToasterShort(this, text + String.valueOf(data));
    }
}
