package com.itats.huwenakapp.ui.transaksi;

import java.util.ArrayList;

public class ModelTransaksiParent {
    int idCategory;
    String transaksiCategory;
    ArrayList<ModelTransaksi> dataChild;

    public ModelTransaksiParent() {}

    public ModelTransaksiParent(int idCategory, String transaksiCategory, ArrayList<ModelTransaksi> dataChild) {
        this.idCategory = idCategory;
        this.transaksiCategory = transaksiCategory;
        this.dataChild = dataChild;
    }

    public ArrayList<ModelTransaksi> getDataChild() {
        return dataChild;
    }

    public void setDataChild(ArrayList<ModelTransaksi> dataChild) {
        this.dataChild = dataChild;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getTransaksiCategory() {
        return transaksiCategory;
    }

    public void setTransaksiCategory(String transaksiCategory) {
        this.transaksiCategory = transaksiCategory;
    }
}
