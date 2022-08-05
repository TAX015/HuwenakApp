package com.itats.huwenakapp.ui.transaksi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.itats.huwenakapp.Login;
import com.itats.huwenakapp.R;
import com.itats.huwenakapp.koneksi.Servers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TransaksiFragment extends Fragment {
    int status;
    private RecyclerView recyclerView;
    private TransaksiAdapterParent parentAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public ArrayList<ModelTransaksiParent> transaksiArrayList;
    public ArrayList<ModelCart> cartList;
    TransaksiViewModel transaksiViewModel;
    SharedPreferences sharedPreferences;
    SwipeRefreshLayout swipe;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        sharedPreferences = this.getActivity().getSharedPreferences(Login.SHARED, Context.MODE_PRIVATE);
        status = sharedPreferences.getInt(Login.TAG_STATUS, 2);

        transaksiViewModel = new ViewModelProvider(this).get(TransaksiViewModel.class);
        View root = inflater.inflate(R.layout.fragment_transaksi, container, false);
        swipe = root.findViewById(R.id.transaksi_refresh);
        recyclerView = root.findViewById(R.id.transaksi_parentrv);
        transaksiArrayList = new ArrayList<ModelTransaksiParent>();
        cartList = new ArrayList<ModelCart>();

        loadCategory();

        recyclerView.setHasFixedSize(true);
        parentAdapter = new TransaksiAdapterParent(transaksiArrayList, status, getContext());
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(parentAdapter);
        parentAdapter.notifyDataSetChanged();

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                transaksiArrayList.clear();
                parentAdapter.notifyDataSetChanged();
                loadCategory();
            }
        });

        return root;
    }

    private void loadCategory() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Servers.VIEW_MENU_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Get parent's data
                    JSONObject obj = new JSONObject(response);
                    JSONArray jArray = obj.getJSONArray("result2");
                    Log.d("response", String.valueOf(jArray));

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObj = jArray.getJSONObject(i);
                        ModelTransaksiParent parentModel = new ModelTransaksiParent();
                        parentModel.setIdCategory(jObj.getInt("category"));
                        parentModel.setTransaksiCategory(jObj.getString("catname"));

                        //Get child data
                        JSONObject childObj = new JSONObject(response);
                        JSONArray childArray = childObj.getJSONArray("result");
                        ArrayList<ModelTransaksi> childList = new ArrayList<>();
                        for (int j=0; j < childArray.length(); j++) {
                            JSONObject cObj = childArray.getJSONObject(j);
                            if (jObj.getInt("category") == cObj.getInt("category")) {
                                childList.add(new ModelTransaksi(cObj.getInt("idmenu"),
                                        cObj.getString("name"), cObj.getInt("size"),
                                        cObj.getInt("price1"), cObj.getInt("price2"),
                                        cObj.getString("image")));
                            }
                        }
                        parentModel.setDataChild(childList);

                        transaksiArrayList.add(parentModel);
                    }

                    parentAdapter = new TransaksiAdapterParent(transaksiArrayList, status, getContext());
                    recyclerView.setAdapter(parentAdapter);
                    parentAdapter.notifyDataSetChanged();
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
}
