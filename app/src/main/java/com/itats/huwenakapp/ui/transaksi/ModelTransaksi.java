package com.itats.huwenakapp.ui.transaksi;

import java.util.List;

public class ModelTransaksi {
    private String name, image;
    private int id, category;
    private int size, price1, price2;

    public ModelTransaksi() {}

    public ModelTransaksi(int id, String name, int size, int price1, int price2, String image) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.price1 = price1;
        this.price2 = price2;
        this.image = image;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice2() {
        return price2;
    }

    public void setPrice2(int price2) {
        this.price2 = price2;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice1() {
        return price1;
    }

    public void setPrice1(int price1) {
        this.price1 = price1;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
