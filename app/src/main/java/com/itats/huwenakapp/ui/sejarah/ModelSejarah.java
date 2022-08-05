package com.itats.huwenakapp.ui.sejarah;

public class ModelSejarah {
    private String time, id, order1, order2, order3;

    public ModelSejarah(String time, String id, String order1, String order2, String order3) {
        this.time = time;
        this.id = id;
        this.order1 = order1;
        this.order2 = order2;
        this.order3 = order3;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder1() {
        return order1;
    }

    public void setOrder1(String order1) {
        this.order1 = order1;
    }

    public String getOrder2() {
        return order2;
    }

    public void setOrder2(String order2) {
        this.order2 = order2;
    }

    public String getOrder3() {
        return order3;
    }

    public void setOrder3(String order3) {
        this.order3 = order3;
    }
}
