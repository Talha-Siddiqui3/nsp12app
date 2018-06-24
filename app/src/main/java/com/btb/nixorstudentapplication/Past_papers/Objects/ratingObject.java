package com.btb.nixorstudentapplication.Past_papers.Objects;

public class ratingObject {

    float ratingvalue;

    public ratingObject(float ratingvalue) {

        this.ratingvalue = ratingvalue;
    }

    public ratingObject() {
    }




    public float getRatingvalue() {
        return ratingvalue;
    }

    public ratingObject setRatingvalue(float ratingvalue) {
        this.ratingvalue = ratingvalue;
        return this;
    }
}
