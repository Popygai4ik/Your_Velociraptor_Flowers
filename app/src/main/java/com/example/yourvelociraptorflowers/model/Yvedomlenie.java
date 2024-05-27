package com.example.yourvelociraptorflowers.model;

import com.google.firebase.firestore.DocumentId;

import java.util.Date;

public class Yvedomlenie {
    @DocumentId
    private String id;
    private String title;
    private String message;
    private String timestamp;
    public Yvedomlenie() { }
    public Yvedomlenie(String title, String message, String timestamp) {
        this.setTitle(title);
        this.setMessage(message);
        this.setTimestamp(timestamp);
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
