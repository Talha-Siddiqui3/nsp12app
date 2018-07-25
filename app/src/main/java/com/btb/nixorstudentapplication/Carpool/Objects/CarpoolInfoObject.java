package com.btb.nixorstudentapplication.Carpool.Objects;

import java.util.HashMap;

public class CarpoolInfoObject {
    private boolean iAmTheDriver;
    private String oneTimeOrScheduled;
    private String privateCarOrTaxi;
    private HashMap<String, Boolean> selectedDays;
    private long selectedTime;
    private String student_name;
    private String student_number;
    private String student_username;

    public long getSelectedTime() {
        return selectedTime;
    }


    public void setSelectedTime(long selectedTime) {
        this.selectedTime = selectedTime;
    }

    public boolean isiAmTheDriver() {
        return iAmTheDriver;
    }

    public void setiAmTheDriver(boolean iAmTheDriver) {
        this.iAmTheDriver = iAmTheDriver;
    }

    public String getOneTimeOrScheduled() {
        return oneTimeOrScheduled;
    }

    public void setOneTimeOrScheduled(String oneTimeOrScheduled) {
        this.oneTimeOrScheduled = oneTimeOrScheduled;
    }

    public String getPrivateCarOrTaxi() {
        return privateCarOrTaxi;
    }

    public void setPrivateCarOrTaxi(String privateCarOrTaxi) {
        this.privateCarOrTaxi = privateCarOrTaxi;
    }

    public HashMap<String, Boolean> getSelectedDays() {
        return selectedDays;
    }

    public void setSelectedDays(HashMap<String, Boolean> selectedDays) {
        this.selectedDays = selectedDays;
    }


    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_number() {
        return student_number;
    }

    public void setStudent_number(String student_number) {
        this.student_number = student_number;
    }

    public String getStudent_username() {
        return student_username;
    }

    public void setStudent_username(String student_username) {
        this.student_username = student_username;
    }


}
