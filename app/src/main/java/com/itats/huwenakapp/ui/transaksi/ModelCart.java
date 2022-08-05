package com.itats.huwenakapp.ui.transaksi;

public class ModelCart {
    int id, price, amount, total;
    String name;

    public ModelCart() {}

    public ModelCart(int id, String name, int price, int amount, int total) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
