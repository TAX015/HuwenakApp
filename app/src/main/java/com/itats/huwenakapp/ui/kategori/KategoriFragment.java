package com.itats.huwenakapp.ui.kategori;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.itats.huwenakapp.ui.cabang.CabangAdapter;
import com.itats.huwenakapp.ui.cabang.CabangTambah;
import com.itats.huwenakapp.ui.cabang.ModelCabang;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KategoriFragment extends Fragment {
    int id;
    String name, desc;
    ArrayList<ModelKategori> dataList;
    KategoriAdapter adapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipe;
    AlertDialog alertDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_kategori, container, false);
        recyclerView = root.findViewById(R.id.catrv);
        swipe = root.findViewById(R.id.cat_refresh);
        dataList = new ArrayList<ModelKategori>();
        adapter = new KategoriAdapter(dataList);

        setHasOptionsMenu(true);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (Servers.isServerConnected()) {
            loadCategory();
        } else {
            Toast.makeText(getContext(), "Server tidak tersambung", Toast.LENGTH_SHORT).show();
        }

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataList.clear();
                adapter.notifyDataSetChanged();
                loadCategory();
            }
        });

        return root;
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.catrv) {
            getActivity().getMenuInflater().inflate(R.menu.cabang_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cabang_menu1 :
                Intent intent = new Intent(getContext(), KategoriTambah.class);
                intent.putExtra("EDIT", "edit");
                intent.putExtra("ID", id);
                intent.putExtra("NAME", name);
                intent.putExtra("DESC", desc);
                startActivity(intent);
                return true;
            case R.id.cabang_menu2 :
                deleteKategori();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void loadCategory() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Servers.MENU_CATEGORY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray jArray = obj.getJSONArray("result");

                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject jObj = jArray.getJSONObject(i);

                                ModelKategori modelKategori = new ModelKategori();
                                modelKategori.setId(jObj.getInt("idcat"));
                                modelKategori.setName(jObj.getString("category"));
                                modelKategori.setDesc(jObj.getString("desc"));

                                dataList.add(modelKategori);
                            }

                            adapter = new KategoriAdapter(dataList, new ClickListener() {
                                @Override
                                public void onItemLongClick(View v, int position) {
                                    id = dataList.get(position).getId();
                                    name = dataList.get(position).getName();
                                    desc = dataList.get(position).getDesc();
                                    registerForContextMenu(recyclerView);
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

    private void deleteKategori() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Hapus data ini?");
        dialog.setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Servers.DEL_CATEGORY_URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jObj = new JSONObject(response);
                                    int success = jObj.getInt("success");

                                    if (success == 1) {
                                        dataList.clear();
                                        adapter.notifyDataSetChanged();
                                        loadCategory();
                                        Toast.makeText(getContext(), jObj.getString("message"),
                                                Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getContext(), jObj.getString("message"),
                                                Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
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
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("id", String.valueOf(id));

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
