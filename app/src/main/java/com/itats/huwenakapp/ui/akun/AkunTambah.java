 package com.itats.huwenakapp.ui.akun;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.itats.huwenakapp.R;
import com.itats.huwenakapp.koneksi.Servers;
import com.itats.huwenakapp.ui.cabang.ModelCabang;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AkunTambah extends AppCompatActivity {
    int selectedBranch, success;
    String selectedStatus;
    ArrayList<ModelCabang> dataList;
    ArrayList<String> valueBranch;
    int[] idBranch;
    EditText edtName, edtUser, edtPass;
    RadioGroup radioGroup;
    RadioButton selectedRadio;
    Spinner spinner;
    Button btnAkunAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int width, height;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.akun_tambah);

        edtName = findViewById(R.id.edt_akunname);
        edtUser = findViewById(R.id.edt_akunuser);
        edtPass = findViewById(R.id.edt_akunpass);
        radioGroup = findViewById(R.id.akun_radiog);
        spinner = findViewById(R.id.spinner_akun);
        btnAkunAdd = findViewById(R.id.btn_akunadd);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        getWindow().setLayout((int) (width*.8), (int) (height*.7));

        dataList = new ArrayList<ModelCabang>();
        loadSpinner();

        btnAkunAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edt1 = edtName.getText().toString();
                String edt2 = edtUser.getText().toString();
                String edt3 = edtPass.getText().toString();
                if (edt1.equals("") || edt2.equals("") || edt3.equals("")) {
                    Toast.makeText(getApplicationContext(), "Kolom tidak boleh kosong!", Toast.LENGTH_LONG).show();
                } else {
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    selectedRadio = findViewById(selectedId);
                    if (selectedRadio.getText().equals("Pegawai")) {
                        selectedStatus = "2";
                    } else if (selectedRadio.getText().equals("Admin")) {
                        selectedStatus = "1";
                    }
                    addAkun();
                }
            }
        });
    }

    private void loadSpinner() {
        dataList.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Servers.VIEW_BRANCH_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray jArray = obj.getJSONArray("result");

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObj = jArray.getJSONObject(i);

                        ModelCabang modelCabang = new ModelCabang();
                        modelCabang.setId(jObj.getInt("idbranch"));
                        modelCabang.setName(jObj.getString("namebranch"));
                        dataList.add(modelCabang);
                    }

                    valueBranch = new ArrayList<String>();
                    idBranch = new int[dataList.size()];
                    for (int i=0; i<dataList.size(); i++) {
                        valueBranch.add(dataList.get(i).getName());
                        idBranch[i] = dataList.get(i).getId();
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, valueBranch);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(arrayAdapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            selectedBranch = idBranch[i];
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("TAG", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(stringRequest);
    }

    private void addAkun() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Servers.ADD_ACCOUNT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    success = obj.getInt("success");

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
                params.put("username", edtUser.getText().toString());
                params.put("password", edtPass.getText().toString());
                params.put("status", selectedStatus);
                params.put("branchId", String.valueOf(selectedBranch));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}