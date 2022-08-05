/*
This is the class where link to the server is saved
isServerConnected() is a method to check connection to the server
 */

package com.itats.huwenakapp.koneksi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Servers {
    public static final String URL_SERVER = "http://192.168.100.104/huwenakk/";
    public static final String URL_CONNECTION = URL_SERVER + "koneksi.php";
    public static final String LOGIN_URL = URL_SERVER + "login.php";
    public static final String VIEW_ACCOUNT_URL = URL_SERVER + "akun/akun_view.php";
    public static final String ADD_ACCOUNT_URL = URL_SERVER + "akun/akun_tambah.php";
    public static final String DEL_ACCOUNT_URL = URL_SERVER + "akun/akun_del.php";
    public static final String ADD_BRANCH_URL = URL_SERVER + "cabang/cabang_tambah.php";
    public static final String VIEW_BRANCH_URL = URL_SERVER + "cabang/cabang_view.php";
    public static final String DEL_BRANCH_URL = URL_SERVER + "cabang/cabang_del.php";
    public static final String EDT_BRANCH_URL = URL_SERVER + "cabang/cabang_edit.php";
    public static final String MENU_CATEGORY_URL = URL_SERVER + "transaksi/menu_category.php";
    public static final String ADD_CATEGORY_URL = URL_SERVER + "kategori/kategori_tambah.php";
    public static final String DEL_CATEGORY_URL = URL_SERVER + "kategori/kategori_del.php";
    public static final String EDT_CATEGORY_URL = URL_SERVER + "kategori/kategori_edit.php";
    public static final String VIEW_MENU_URL = URL_SERVER + "transaksi/menu_view.php";
    public static final String DEL_MENU_URL = URL_SERVER + "transaksi/menu_del.php";

    public static final String IMAGE_PATH = URL_SERVER + "images/";

    public static final String[] MONTH_LIST = {"Januari", "Februari", "Maret", "April", "Mei",
            "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};

    public static boolean isServerConnected() {
        try {
            URL myUrl = new URL(URL_CONNECTION);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}