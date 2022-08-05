package com.itats.huwenakapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.itats.huwenakapp.koneksi.Servers;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    int success, status;
    String username, password, name, location;
    boolean session;
    EditText edtUser, edtPass;
    Button btnLogin;
    SharedPreferences sharedPreferences;

    public static final String SHARED = "shared_preferences";
    public static final String TAG_SESSION = "session";
    public static final String TAG_NAME = "name";
    public static final String TAG_LOCATION = "location";
    public static final String TAG_STATUS = "status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUser = findViewById(R.id.edt_userlogin);
        edtPass = findViewById(R.id.edt_passlogin);
        btnLogin = (Button) findViewById(R.id.btn_login);

        //Check login session
        sharedPreferences = getSharedPreferences(SHARED, Context.MODE_PRIVATE);
        session = sharedPreferences.getBoolean(TAG_SESSION, false);
        name = sharedPreferences.getString(TAG_NAME, null);
        location = sharedPreferences.getString(TAG_LOCATION, null);
        status = sharedPreferences.getInt(TAG_STATUS, 2);
        if (session) {
            Intent intent = new Intent(Login.this, MenuDrawer.class);
            intent.putExtra(TAG_NAME, name);
            intent.putExtra(TAG_LOCATION, location);
            intent.putExtra(TAG_STATUS, status);
            startActivity(intent);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (Servers.isServerConnected()) {
                            Log.d("TAG", "Server Connected");
                            username = edtUser.getText().toString();
                            password = edtPass.getText().toString();
                            login();
                        } else {
                            Toast.makeText(getApplicationContext(), "Server tidak tersambung", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).start();
            }
        });
    }

    private void login() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Servers.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    success = obj.getInt("success");

                    if (success == 1) {
                        status = obj.getInt("status");
                        name = obj.getString("name");
                        location = obj.getString("bName");

                        //Save sharedpreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(TAG_SESSION, true);
                        editor.putInt(TAG_STATUS, status);
                        editor.putString(TAG_NAME, name);
                        editor.putString(TAG_LOCATION, location);
                        editor.apply();

                        Intent intent = new Intent(Login.this, MenuDrawer.class);
                        intent.putExtra(TAG_NAME, name);
                        intent.putExtra(TAG_LOCATION, location);
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error : " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}