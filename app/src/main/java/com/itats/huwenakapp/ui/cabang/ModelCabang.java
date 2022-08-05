package com.itats.huwenakapp.ui.cabang;

public class ModelCabang {
    int id;
    String image, name, location;
    Boolean dlt;

    public ModelCabang(int id, String image, String name, String location, Boolean dlt) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.location = location;
        this.dlt = dlt;
    }

    public Boolean getDlt() {
        return dlt;
    }

    public void setDlt(Boolean dlt) {
        this.dlt = dlt;
    }

    public ModelCabang(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
