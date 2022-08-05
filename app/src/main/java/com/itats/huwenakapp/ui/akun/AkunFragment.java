package com.itats.huwenakapp.ui.akun;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AkunFragment extends Fragment {
    int idAkun, success, status;
    AlertDialog alertDialog;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipe;
    private AkunAdapter adapter;
    private ArrayList<ModelAkun> dataList;
    AkunViewModel akunViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        akunViewModel = new ViewModelProvider(this).get(AkunViewModel.class);
        View root = inflater.inflate(R.layout.fragment_akun, container, false);
        swipe = root.findViewById(R.id.akun_refresh);
        recyclerView = root.findViewById(R.id.akunrv);
        recyclerView.setHasFixedSize(true);
        dataList = new ArrayList<ModelAkun>();
        adapter = new AkunAdapter(dataList);

        setHasOptionsMenu(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (Servers.isServerConnected()) {
            loadAkun();
        } else {
            Toast.makeText(getContext(), "Server tidak tersambung", Toast.LENGTH_SHORT).show();
        }

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataList.clear();
                adapter.notifyDataSetChanged();
                loadAkun();
            }
        });

        return root;
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.akunrv) {
            getActivity().getMenuInflater().inflate(R.menu.akun_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.akun_menu1 :
                deleteAkun();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void loadAkun() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Servers.VIEW_ACCOUNT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray array = obj.getJSONArray("result");

                    for (int i=0; i<array.length(); i++) {
                        JSONObject jObj = array.getJSONObject(i);

                        ModelAkun modelAkun = new ModelAkun();
                        modelAkun.setId(jObj.getInt("idaccount"));
                        modelAkun.setStatus(jObj.getInt("status"));
                        modelAkun.setName(jObj.getString("name"));
                        modelAkun.setBranch(jObj.getString("branchname"));

                        dataList.add(modelAkun);
                    }

                    adapter = new AkunAdapter(dataList, new ClickListener() {
                        @Override
                        public void onItemLongClick(View v, int position) {
                            registerForContextMenu(recyclerView);
                            idAkun = dataList.get(position).getId();
                            status = dataList.get(position).getStatus();
                            Toast.makeText(getContext(), "ID : " + idAkun, Toast.LENGTH_LONG).show();
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

    private void deleteAkun() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Hapus data ini?");
        dialog.setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Servers.DEL_ACCOUNT_URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jObj = new JSONObject(response);
                                    success = jObj.getInt("success");

                                    if (success == 1) {
                                        dataList.clear();
                                        adapter.notifyDataSetChanged();
                                        loadAkun();
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
                                params.put("id", String.valueOf(idAkun));
                                params.put("status", String.valueOf(status));

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
