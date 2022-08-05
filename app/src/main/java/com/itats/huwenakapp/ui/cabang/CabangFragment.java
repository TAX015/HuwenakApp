package com.itats.huwenakapp.ui.cabang;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.itats.huwenakapp.ClickListener;
import com.itats.huwenakapp.R;
import com.itats.huwenakapp.koneksi.Servers;
import com.itats.huwenakapp.ui.transaksi.TransaksiKeranjang;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CabangFragment extends Fragment {
    AlertDialog alertDialog;
    RecyclerView recyclerView;
    CabangAdapter adapter;
    ArrayList<ModelCabang> dataList;
    CabangViewModel cabangViewModel;
    SwipeRefreshLayout swipe;
    int success, idCabang;
    String imgCabang, nameCabang, locationCabang;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        cabangViewModel = new ViewModelProvider(this).get(CabangViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cabang, container, false);
        recyclerView = root.findViewById(R.id.cabangrv);
        swipe = root.findViewById(R.id.cabang_refresh);
        dataList = new ArrayList<ModelCabang>();
        adapter = new CabangAdapter(dataList);

        setHasOptionsMenu(true);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (Servers.isServerConnected()) {
            loadCabang();
        } else {
            Toast.makeText(getContext(), "Server tidak tersambung", Toast.LENGTH_SHORT).show();
        }

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataList.clear();
                adapter.notifyDataSetChanged();
                loadCabang();
            }
        });

        return root;
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.cabangrv) {
            getActivity().getMenuInflater().inflate(R.menu.cabang_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.cabang_menu1 :
                Intent intent = new Intent(getContext(), CabangTambah.class);
                intent.putExtra("EDIT", "edit");
                intent.putExtra("ID", idCabang);
                intent.putExtra("NAME", nameCabang);
                intent.putExtra("LOCATION", locationCabang);
                startActivity(intent);
                return true;
            case R.id.cabang_menu2 :
                deleteCabang();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void loadCabang() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Servers.VIEW_BRANCH_URL,
                new Response.Listener<String>() {
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
                                modelCabang.setLocation(jObj.getString("addrsbranch"));
                                modelCabang.setImage(jObj.getString("imgbranch"));
                                if (jObj.getString("dltbranch").equals("0")) {
                                    modelCabang.setDlt(false);
                                }

                                dataList.add(modelCabang);
                            }

                            adapter = new CabangAdapter(dataList, new ClickListener() {
                                @Override
                                public void onItemLongClick(View v, int position) {
                                    registerForContextMenu(recyclerView);
                                    idCabang = dataList.get(position).getId();
                                    nameCabang = dataList.get(position).getName();
                                    locationCabang = dataList.get(position).getLocation();
                                    imgCabang = dataList.get(position).getImage();
                                }
                            });
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("TAG", "Error : " + error.getMessage());
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);
        swipe.setRefreshing(false);
    }

    public void deleteCabang() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Hapus data ini?");
        dialog.setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Servers.DEL_BRANCH_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jObj = new JSONObject(response);
                                            success = jObj.getInt("success");

                                            if (success == 1) {
                                                dataList.clear();
                                                adapter.notifyDataSetChanged();
                                                loadCabang();
                                                Toast.makeText(getContext(), jObj.getString("message"),
                                                        Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(getContext(), jObj.getString("message"),
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("TAG", "Error: " + error.getMessage());
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                //Post parameter to POST url
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("id", String.valueOf(idCabang));
                                params.put("img", imgCabang);

                                return params;
                            }
                        };

                        requestQueue.add(stringRequest);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.cancel();
                    }
                });
        alertDialog = dialog.create();
        alertDialog.show();
    }
}
