package com.itats.huwenakapp.ui.akun;

public class ModelAkun {
    private String name, branch, username;
    int id, status;
    Boolean dlt;

    public ModelAkun(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getDlt() {
        return dlt;
    }

    public void setDlt(Boolean dlt) {
        this.dlt = dlt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
