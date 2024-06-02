package com.example.yourvelociraptorflowers.model.plant;


import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;

public class Plants {

    // Аннотация позволяет записать в поле класса имя документа
    @DocumentId
    private String id;
    private String name;
    private String opisanie;
    private String opisanie2;
    private String opisanie3;
    private String opisanie4;

    private String resinok;
    private String resinok2;
    private String resinok3;
    private String resinok4;
    private String opisanie5;
    private String lastWateredFormatted;
    private Integer koofesiant_poliva;
    private Integer coefficient_illumination;
    private ArrayList <String> lastWatered;


    public Plants(String name, String opisanie, String resinok,  String resinok2, String resinok3, String resinok4, String opisanie2, String opisanie3, String opisanie4, String opisanie5, Integer koofesiant_poliva, Integer coefficient_illumination) {
        this.setName(name);
        this.setOpisanie(opisanie);
        this.setResinok(resinok);
        this.setOpisanie2(opisanie2);
        this.setOpisanie3(opisanie3);
        this.setOpisanie4(opisanie4);
        this.setResinok2(resinok2);
        this.setResinok3(resinok3);
        this.setResinok4(resinok4);
        this.setOpisanie5(opisanie5);
        this.setKoofesiant_poliva(koofesiant_poliva);
        this.setCoefficient_illumination(coefficient_illumination);


    }
    public Plants() {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpisanie2() {
        return opisanie2;
    }

    public void setOpisanie2(String opisanie2) {
        this.opisanie2 = opisanie2;
    }

    public String getOpisanie3() {
        return opisanie3;
    }

    public void setOpisanie3(String opisanie3) {
        this.opisanie3 = opisanie3;
    }

    public String getOpisanie4() {
        return opisanie4;
    }

    public void setOpisanie4(String opisanie4) {
        this.opisanie4 = opisanie4;
    }
    public String getResinok2() {
        return resinok2;
    }

    public void setResinok2(String resinok2) {
        this.resinok2 = resinok2;
    }

    public String getResinok3() {
        return resinok3;
    }

    public void setResinok3(String resinok3) {
        this.resinok3 = resinok3;
    }

    public String getResinok4() {
        return resinok4;
    }

    public void setResinok4(String resinok4) {
        this.resinok4 = resinok4;
    }

    public String getOpisanie5() {
        return opisanie5;
    }

    public void setOpisanie5(String opisanie5) {
        this.opisanie5 = opisanie5;
    }

    public void setLastWateredFormatted(String lastWateredFormatted) {
        this.lastWateredFormatted = lastWateredFormatted;
    }

    public String getLastWateredFormatted() {
        return lastWateredFormatted;
    }

    public Integer getKoofesiant_poliva() {
        return koofesiant_poliva;
    }

    public void setKoofesiant_poliva(Integer koofesiant_poliva) {
        this.koofesiant_poliva = koofesiant_poliva;
    }

    public ArrayList<String> getLastWatered() {
        return lastWatered;
    }

    public void setLastWatered(ArrayList<String> lastWatered) {
        this.lastWatered = lastWatered;
    }

    public Integer getCoefficient_illumination() {
        return coefficient_illumination;
    }

    public void setCoefficient_illumination(Integer coefficient_illumination) {
        this.coefficient_illumination = coefficient_illumination;
    }
}
