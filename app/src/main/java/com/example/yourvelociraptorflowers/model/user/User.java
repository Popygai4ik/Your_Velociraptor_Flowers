package com.example.yourvelociraptorflowers.model.user;

import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;

public class User {
    private String userId;
    public String uid;
    public String name;
    public String email;
    public String password;
    public Boolean vozrst;

    public ArrayList<String> moisFlowers;
    public ArrayList<String> notifications;
    public User() {

    }

    public User(String userId, String uid, String name, String email, String password, Boolean vozrst, ArrayList<String> moisFlowers, ArrayList<String> notifications) {
        this.vozrst = vozrst;
        this.userId = userId;
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.password = password;
        this.moisFlowers = moisFlowers;
        this.notifications = notifications;
    }

    public ArrayList<String> getNotifications() {
        return notifications;
    }
    public void setNotifications(ArrayList<String> notifications) {
        this.notifications = notifications;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Boolean getVozrst() {
        return vozrst;
    }
    public void setVozrst(Boolean vozrst) {
        this.vozrst = vozrst;
    }
    public ArrayList<String> getMoisFlowers() {
        return moisFlowers;
    }
    public void setMoisFlowers(ArrayList<String> moisFlowers) {
        this.moisFlowers = moisFlowers;
    }
}