package com.itats.huwenakapp.ui.cabang;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.itats.huwenakapp.R;
import com.itats.huwenakapp.koneksi.MultipartRequest;
import com.itats.huwenakapp.koneksi.Servers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class CabangTambah extends AppCompatActivity {
    EditText edtBranchName, edtBranchAddrs;
    TextView txtBranchPath;
    Button btnBranchImg, btnAddBranch;
    Uri selectedImg = null;
    String edit;
    int success, idBranch;
    String nameBranch, locationBranch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int width, height;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.cabang_tambah);
        edtBranchName = findViewById(R.id.edt_cabangname);
        edtBranchAddrs = findViewById(R.id.edt_cabangaddrs);
        txtBranchPath = findViewById(R.id.cabang_path);
        btnBranchImg = findViewById(R.id.btn_cabangpic);
        btnAddBranch = findViewById(R.id.btn_cabangadd);

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
                idBranch = extra.getInt("ID");
                nameBranch = extra.getString("NAME");
                locationBranch = extra.getString("LOCATION");

                edtBranchName.setText(nameBranch);
                edtBranchAddrs.setText(locationBranch);

                btnBranchImg.setVisibility(View.INVISIBLE);
                txtBranchPath.setVisibility(View.INVISIBLE);
            }
        }


        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            selectedImg = data.getData();
                            String[] filePath = {MediaStore.Images.Media.DATA};
                            Cursor c = getContentResolver().query(selectedImg, filePath, null, null, null);
                            c.moveToFirst();
                            int columnIndx = c.getColumnIndex(filePath[0]);
                            String imgPath = c.getString(columnIndx);
                            c.close();
                            Log.i("galery path", imgPath);
                            txtBranchPath.setText(imgPath);
                        }
                    }
                }
        );

        btnBranchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);
            }
        });

        btnAddBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit.equals("edit")) {
                    editCabang();
                } else if (selectedImg == null){
                    Toast.makeText(view.getContext(), "Pilih gambar terlebih dahulu!", Toast.LENGTH_LONG).show();
                } else {
                    //get bitmap object from uri
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                getApplicationContext().getContentResolver(), selectedImg);
                        uploadBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /*
     * The method is taking Bitmap as an argument
     * then it will return the byte[] array for the given bitmap
     * and we will send this array to the server
     * here we are using PNG Compression with 80% quality
     * you can give quality between 0 to 100
     * 0 means worse quality
     * 100 means best quality
     * */
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {
        final String bName = edtBranchName.getText().toString().trim();
        final String bAddrs = edtBranchAddrs.getText().toString().trim();

        //our custom volley request
        MultipartRequest multipartRequest = new MultipartRequest(Request.Method.POST, Servers.ADD_BRANCH_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            //Add parameters with image
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("branchName", bName);
                params.put("branchAddress", bAddrs);
                return params;
            }

            //Here we are passing image by renaming it with a unique name
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }

        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(multipartRequest);
    }

    private void editCabang() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Servers.EDT_BRANCH_URL, new Response.Listener<String>() {
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
                Map<String, String> params = new HashMap<String, String> ();
                params.put("id", String.valueOf(idBranch));
                params.put("name", edtBranchName.getText().toString());
                params.put("location", edtBranchAddrs.getText().toString());
                return params;
            }
        };

        requestQueue.add(request);
    }
}