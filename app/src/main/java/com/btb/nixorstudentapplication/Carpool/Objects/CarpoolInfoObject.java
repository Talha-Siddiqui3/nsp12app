package com.btb.nixorstudentapplication.Carpool.Objects;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CarpoolInfoObject implements Serializable {
    public Boolean getiAmTheDriver() {
        return iAmTheDriver;
    }

    public void setiAmTheDriver(Boolean iAmTheDriver) {
        this.iAmTheDriver = iAmTheDriver;
    }

    private Boolean iAmTheDriver;
    private String oneTimeOrScheduled;
    private String privateCarOrTaxi;
    private HashMap<String, Boolean> selectedDays;
    private double selectedTime;
    private String student_name;
    private String student_number;
    private String student_username;
    private String student_id;
    private double myLat;
    private double myLong;
    private String mainCampusOrNcfp;
    private double estimatedCost;
    private String rideDuration;
    private double totalDistance;
    private int numberOfSeats;
    private int occupiedSeats;
    private double distanceFromMyLocation;

    @ServerTimestamp
    private Timestamp timestamp;
    @Exclude
    private String dateString;
    @Exclude
    private String timeString;
    @Exclude
    private String daysAvailableString;
    @Exclude
    private String availableSeatsString;
    @Exclude
    private String priceString;
    @Exclude
    private String stringifiedCurrentData;

    @Exclude
    public String getStringifiedCurrentData() {
        return stringifiedCurrentData;
    }

    @Exclude
    public void setStringifiedCurrentData(String stringifiedCurrentData) {
        this.stringifiedCurrentData = stringifiedCurrentData;
    }

    @Exclude
    public String getAvailableSeatsString() {
        return availableSeatsString;
    }

    @Exclude
    public void setAvailableSeatsString(String availableSeatsString) {
        this.availableSeatsString = availableSeatsString;
    }

    @Exclude
    public String getPriceString() {
        return priceString;
    }

    @Exclude
    public void setPriceString(String priceString) {
        this.priceString = priceString;
    }

    @Exclude
    public String getDateString() {
        return dateString;
    }

    @Exclude
    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    @Exclude
    public String getTimeString() {
        return timeString;
    }

    @Exclude
    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    @Exclude
    public String getDaysAvailableString() {
        return daysAvailableString;
    }

    @Exclude
    public void setDaysAvailableString(String daysAvailableString) {
        this.daysAvailableString = daysAvailableString;
    }

    public double getDistanceFromMyLocation() {
        return distanceFromMyLocation;
    }

    public void setDistanceFromMyLocation(double distanceFromMyLocation) {
        this.distanceFromMyLocation = distanceFromMyLocation;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getOccupiedSeats() {
        return occupiedSeats;
    }

    public void setOccupiedSeats(int occupiedSeats) {
        this.occupiedSeats = occupiedSeats;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public CarpoolInfoObject() {
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

    public double getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(double selectedTime) {
        this.selectedTime = selectedTime;
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

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public double getMyLat() {
        return myLat;
    }

    public void setMyLat(double myLat) {
        this.myLat = myLat;
    }

    public double getMyLong() {
        return myLong;
    }

    public void setMyLong(double myLong) {
        this.myLong = myLong;
    }

    public String getMainCampusOrNcfp() {
        return mainCampusOrNcfp;
    }

    public void setMainCampusOrNcfp(String mainCampusOrNcfp) {
        this.mainCampusOrNcfp = mainCampusOrNcfp;
    }

    public double getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public String getRideDuration() {
        return rideDuration;
    }

    public void setRideDuration(String rideDuration) {
        this.rideDuration = rideDuration;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    private String route;

}