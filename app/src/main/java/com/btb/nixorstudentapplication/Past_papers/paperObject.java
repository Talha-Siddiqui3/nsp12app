package com.btb.nixorstudentapplication.Past_papers;

public class paperObject {
    String month,year,variant,type;

    public paperObject(String month, String year, String variant, String type) {
        this.month = month;
        this.year = year;
        this.variant = variant;
        this.type = type;
    }

    public paperObject() {
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
