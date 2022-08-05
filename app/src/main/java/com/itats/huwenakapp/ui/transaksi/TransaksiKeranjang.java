/*
Layout : transaksi_keranjang.xml
Adapter : CartAdapter.java

This is a class for the cart popup windows
 */
package com.itats.huwenakapp.ui.transaksi;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itats.huwenakapp.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

public class TransaksiKeranjang extends AppCompatActivity {
    AlertDialog.Builder builder;
    RecyclerView recyclerView;
    public static ArrayList<ModelCart> dataList;
    static CartAdapter adapter;
    static WeakReference<TextView> wrt;

    public static void setDataList(ArrayList<ModelCart> dataList) {
        TransaksiKeranjang.dataList = dataList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int width, height;
        TextView txtTotal;
        Button btnSubmit;
        builder = new AlertDialog.Builder(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaksi_keranjang);
        recyclerView = findViewById(R.id.popup_transaksirv);
        txtTotal = findViewById(R.id.popup_transaksitotal);
        btnSubmit = findViewById(R.id.btnpopup_transaksi);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        getWindow().setLayout((int) (width*.8), (int) (height*.6));

        adapter = new CartAdapter(dataList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        wrt = new WeakReference<>(txtTotal);
        txtGrandTotal();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = "";
                int price = 0;
                s = wrt.get().getText().toString();
                s = s.replaceAll("Rp\\.", "");
                price = Integer.parseInt(s);

                if (price <= 0) {
                    Toast.makeText(getBaseContext(), "Keranjang Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show();
                } else {
                    final EditText editText = new EditText(getApplicationContext());
                    builder.setTitle("Masukan Nama Customer");
                    builder.setView(editText);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String customerName = editText.getText().toString();
                            Toast.makeText(getBaseContext(), "Selesai", Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                }
            }
        });
    }

    public static void txtGrandTotal() {
        int total;
        total = adapter.grandTotal(dataList);
        Log.d("TAG", "" + total);
        wrt.get().setText("Rp." + total);
    }
}