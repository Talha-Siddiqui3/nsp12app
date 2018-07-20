package com.btb.nixorstudentapplication.BookMyTa;
//OBJECT for storing all TA DATA

import java.util.HashMap;

public class TA_Object {
    private String TaName;
    private String TaID;
    private String TaUserName;
    private String StudentUserName;
    private String StudentYear;
    private String Subject;

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public HashMap<String, Boolean> getMonday() {
        return Monday;
    }

    public void setMonday(HashMap<String, Boolean> monday) {
        Monday = monday;
    }

    private HashMap<String,Boolean> Monday;
    private HashMap<String,Boolean> Tuesday;
    private HashMap<String,Boolean> Wednesday;
    private HashMap<String,Boolean> Thursday;
    private HashMap<String,Boolean> Friday;

    public HashMap<String, Boolean> getTuesday() {
        return Tuesday;
    }

    public void setTuesday(HashMap<String, Boolean> tuesday) {
        Tuesday = tuesday;
    }

    public HashMap<String, Boolean> getWednesday() {
        return Wednesday;
    }

    public void setWednesday(HashMap<String, Boolean> wednesday) {
        Wednesday = wednesday;
    }

    public HashMap<String, Boolean> getThursday() {
        return Thursday;
    }

    public void setThursday(HashMap<String, Boolean> thursday) {
        Thursday = thursday;
    }

    public HashMap<String, Boolean> getFriday() {
        return Friday;
    }

    public void setFriday(HashMap<String, Boolean> friday) {
        Friday = friday;
    }

    public String getTAYear() {
        return TAYear;
    }

    public void setTAYear(String TAYear) {
        this.TAYear = TAYear;
    }

    private String TAYear;

    public String getStudentYear() {
        return StudentYear;
    }

    public void setStudentYear(String studentYear) {
        StudentYear = studentYear;
    }

    public String getStudentUserName() {
        return StudentUserName;
    }

    public void setStudentUserName(String studentUserName) {
        StudentUserName = studentUserName;
    }

    public String getTaUserName() {
        return TaUserName;
    }

    public void setTaUserName(String taUserName) {
        TaUserName = taUserName;
    }


public TA_Object()
{

}

    public String getTaName() {
        return TaName;
    }

    public void setTaName(String taName) {
        TaName = taName;
    }


    public String getTaID() {
        return TaID;
    }

    public void setTaID(String taID) {
        TaID = taID;
    }



    public TA_Object(String taName, String days, String taID, String timings, String subject) {
        TaName = taName;
        TaID = taID;

    }
}
