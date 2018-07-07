package com.btb.nixorstudentapplication.BookMyTa;
//OBJECT for storing all TA DATA

public class TA_Object {
    private String TaName;
    private String Days;
    private String TaID;
    private String Timings;
    private String Subject;
    private String TaUserName;
    private String StudentUserName;

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

    public String getDays() {
        return Days;
    }

    public void setDays(String days) {
        Days = days;
    }

    public String getTaID() {
        return TaID;
    }

    public void setTaID(String taID) {
        TaID = taID;
    }

    public String getTimings() {
        return Timings;
    }

    public void setTimings(String timings) {
        Timings = timings;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public TA_Object(String taName, String days, String taID, String timings, String subject) {
        TaName = taName;
        Days = days;
        TaID = taID;
        Timings = timings;
        Subject = subject;
    }
}
