package com.btb.nixorstudentapplication.Autentication.User;

public class accountTypeGetSet {
    String username;
    String mode;

    public accountTypeGetSet(String username, String mode) {
        this.username = username;
        this.mode = mode;
    }

    public accountTypeGetSet() {
    }

    public String getUsername() {
        return username;
    }

    public accountTypeGetSet setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getMode() {
        return mode;
    }

    public accountTypeGetSet setMode(String mode) {
        this.mode = mode;
        return this;
    }
}
