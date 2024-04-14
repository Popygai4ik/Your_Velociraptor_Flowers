package com.example.yourvelociraptorflowers.model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentId;

public class Plants {

    // Аннотация позволяет записать в поле класса имя документа
    @DocumentId
    private String id;
    private String name;
    private String opisanie;
    private String resinok;


    // Пустой конструктор необходим для парсинга модели firestore
    public Plants() {

    }

    public Plants(String name, String opisanie, String resinok) {
        this.setName(name);
        this.setOpisanie(opisanie);
        this.setResinok(resinok);
    }

    @NonNull
    @Override
    public String toString() {
        return "User = {id=" + getId() + ", name=" + getName() + ", opisanie=" + getOpisanie() + ", resinok=" + getResinok() +"}";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpisanie() {
        return opisanie;
    }

    public void setOpisanie(String opisanie) {
        this.opisanie = opisanie;
    }

    public String getResinok() {
        return resinok;
    }

    public void setResinok(String resinok) {
        this.resinok = resinok;
    }
}
