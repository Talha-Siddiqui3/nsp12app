package com.btb.nixorstudentapplication.Autentication.nsp_web;

import java.util.ArrayList;

/**
 * Created by Hassan Abbasi on 25/05/2018.
 */

public class StudentDetails {
    String student_name;
    String student_id;
    String student_email;
    String student_year;
    String student_house;
    String student_profileUrl;
    String student_guid;
    ArrayList<String> studentSubjects;
    ArrayList<String> studentClasses;

    public String getStudent_guid() {
        return student_guid;
    }

    public void setStudent_guid(String student_guid) {
        this.student_guid = student_guid;
    }

    public StudentDetails() {
    }

    public StudentDetails(String student_name, String student_id, String student_email, String student_year, String student_house, String student_profileUrl,String student_guid) {
        this.student_name = student_name;
        this.student_id = student_id;
        this.student_email = student_email;
        this.student_year = student_year;
        this.student_house = student_house;
        this.student_profileUrl = student_profileUrl;

    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getStudent_email() {
        return student_email;
    }

    public void setStudent_email(String student_email) {
        this.student_email = student_email;
    }

    public String getStudent_year() {
        return student_year;
    }

    public void setStudent_year(String student_year) {
        this.student_year = student_year;
    }

    public String getStudent_house() {
        return student_house;
    }

    public void setStudent_house(String student_house) {
        this.student_house = student_house;
    }

    public String getStudent_profileUrl() {
        return student_profileUrl;
    }

    public void setStudent_profileUrl(String student_profileUrl) {
        this.student_profileUrl = student_profileUrl;
    }

    public ArrayList<String> getStudentSubjects() {
        return studentSubjects;
    }

    public void setStudentSubjects(ArrayList<String> studentSubjects) {
        this.studentSubjects = studentSubjects;
    }

    public ArrayList<String> getStudentClasses() {
        return studentClasses;
    }

    public void setStudentClasses(ArrayList<String> studentClasses) {
        this.studentClasses = studentClasses;
    }
}
