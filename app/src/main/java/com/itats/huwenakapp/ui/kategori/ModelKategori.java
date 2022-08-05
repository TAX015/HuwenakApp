package com.itats.huwenakapp.ui.kategori;

public class ModelKategori {
    int id;
    String name, desc;
    Boolean dlt;

    public ModelKategori() {}

    public ModelKategori(int id, String name, Boolean dlt) {
        this.id = id;
        this.name = name;
        this.dlt = dlt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDlt() {
        return dlt;
    }

    public void setDlt(Boolean dlt) {
        this.dlt = dlt;
    }
}
