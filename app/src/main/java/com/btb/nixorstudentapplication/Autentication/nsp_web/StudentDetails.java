package com.btb.nixorstudentapplication.Autentication.nsp_web;

/**
 * Created by Hassan Abbasi on 25/05/2018.
 */

public class StudentDetails {
    String Student_name;
    String Student_ID;
    String Student_email;
    String Student_year;
    String Student_house;
    String Student_profileUrl;
    public StudentDetails() {
    }

    public StudentDetails(String Student_name, String Student_ID, String Student_email, String Student_year, String Student_house, String Student_profileUrl) {
        this.Student_name = Student_name;
        this.Student_ID = Student_ID;
        this.Student_email = Student_email;
        this.Student_year = Student_year;
        this.Student_house = Student_house;
        this.Student_profileUrl = Student_profileUrl;
    }

    public String getStudent_profileUrl() {
        return Student_profileUrl;
    }

    public void setStudent_profileUrl(String Student_profileUrl) {
        this.Student_profileUrl = Student_profileUrl;
    }


    public String getStudent_name() {
        return Student_name;
    }

    public StudentDetails setStudent_name(String student_name) {
        Student_name = student_name;
        return this;
    }

    public String getStudent_ID() {
        return Student_ID;
    }

    public StudentDetails setStudent_ID(String student_ID) {
        Student_ID = student_ID;
        return this;
    }

    public String getStudent_email() {
        return Student_email;
    }

    public StudentDetails setStudent_email(String student_email) {
        Student_email = student_email;
        return this;
    }

    public String getStudent_year() {
        return Student_year;
    }

    public StudentDetails setStudent_year(String student_year) {
        Student_year = student_year;
        return this;
    }

    public String getStudent_house() {
        return Student_house;
    }

    public StudentDetails setStudent_house(String student_house) {
        Student_house = student_house;
        return this;
    }
}
