package com.itats.huwenakapp.ui.kategori;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.itats.huwenakapp.R;
import com.itats.huwenakapp.koneksi.Servers;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class KategoriTambah extends AppCompatActivity {
    String edit, name, desc;
    int id;
    EditText edtName, edtDesc;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int width, height;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kategori_tambah);

        edtName = findViewById(R.id.edt_kategorinama);
        edtDesc = findViewById(R.id.edt_kategoridesc);
        btnAdd = findViewById(R.id.btn_kategoriadd);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        getWindow().setLayout((int) (width*.8), (int) (height*.6));

        //Check edit or add state
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            edit = extra.getString("EDIT");
            if (edit.equals("edit")) {
                id = extra.getInt("ID");
                name = extra.getString("NAME");
                desc = extra.getString("DESC");

                edtName.setText(name);
                edtDesc.setText(desc);
            }
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edt1 = edtName.getText().toString();
                String edt2 = edtDesc.getText().toString();

                if (edt1.equals("") || edt2.equals("")) {
                    Toast.makeText(getApplicationContext(), "Kolom tidak boleh kosong!", Toast.LENGTH_LONG).show();
                } else if (edit.equals("edit")) {
                    editKategori();
                } else {
                    addKategori();
                }
            }
        });
    }

    private void addKategori() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Servers.ADD_CATEGORY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("success");

                    if (success == 1) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", edtName.getText().toString());
                params.put("desc", edtDesc.getText().toString());
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void editKategori() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Servers.EDT_CATEGORY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("success");

                    if (success == 1) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", String.valueOf(id));
                params.put("name", name);
                params.put("name_edit", edtName.getText().toString());
                params.put("desc", edtDesc.getText().toString());
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}