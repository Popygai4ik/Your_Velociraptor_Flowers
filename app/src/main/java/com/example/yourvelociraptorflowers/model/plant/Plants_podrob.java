package com.example.yourvelociraptorflowers.model.plant;


import com.google.firebase.firestore.DocumentId;
public class Plants_podrob {

    // Аннотация позволяет записать в поле класса имя документа
    @DocumentId
    private String id;
    private String name;
    private String opisanie;
    private String opisanie2;
    private String opisanie3;
    private String opisanie4;
    private String opisanie5;
    private String opisanie6;
    private String opisanie7;
    private String opisanie8;
    private String opisanie9;
    private String opisanie10;

    private String resinok;
    private String resinok2;
    private String resinok3;
    private String resinok4;

    public Plants_podrob(String id, String name, String opisanie, String resinok2, String resinok3, String resinok4, String resinok, String opisanie2, String opisanie3, String opisanie4, String opisanie5, String opisanie6, String opisanie7, String opisanie8, String opisanie9, String opisanie10) {
        this.id = id;
        this.name = name;
        this.opisanie = opisanie;
        this.opisanie2 = opisanie2;
        this.opisanie3 = opisanie3;
        this.opisanie4 = opisanie4;
        this.opisanie5 = opisanie5;
        this.opisanie6 = opisanie6;
        this.opisanie7 = opisanie7;
        this.opisanie8 = opisanie8;
        this.opisanie9 = opisanie9;
        this.opisanie10 = opisanie10;
        this.resinok = resinok;
        this.resinok2 = resinok2;
        this.resinok3 = resinok3;
        this.resinok4 = resinok4;

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

    public String getOpisanie5() {
        return opisanie5;
    }

    public void setOpisanie5(String opisanie5) {
        this.opisanie5 = opisanie5;
    }

    public String getOpisanie6() {
        return opisanie6;
    }

    public void setOpisanie6(String opisanie6) {
        this.opisanie6 = opisanie6;
    }

    public String getOpisanie7() {
        return opisanie7;
    }

    public void setOpisanie7(String opisanie7) {
        this.opisanie7 = opisanie7;
    }

    public String getOpisanie8() {
        return opisanie8;
    }

    public void setOpisanie8(String opisanie8) {
        this.opisanie8 = opisanie8;
    }

    public String getOpisanie9() {
        return opisanie9;
    }

    public void setOpisanie9(String opisanie9) {
        this.opisanie9 = opisanie9;
    }

    public String getOpisanie10() {
        return opisanie10;
    }

    public void setOpisanie10(String opisanie10) {
        this.opisanie10 = opisanie10;
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
}
