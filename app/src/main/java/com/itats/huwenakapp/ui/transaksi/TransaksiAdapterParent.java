package com.itats.huwenakapp.ui.transaksi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.itats.huwenakapp.VolleyCallback;
import com.itats.huwenakapp.koneksi.Servers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransaksiAdapterParent extends RecyclerView.Adapter<TransaksiAdapterParent.TransaksiParentViewHolder> {
    int status;
    RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private ArrayList<ModelTransaksiParent> dataList;
    public Context context;
    AlertDialog alertDialog;
    ArrayList<ModelCart> cartData;
    HashMap<Integer, Integer> map;

    public TransaksiAdapterParent(ArrayList<ModelTransaksiParent> dataList,int status, Context context) {
        this.dataList = dataList;
        this.status = status;
        this.context = context;
        cartData = new ArrayList<ModelCart>();
        map = new HashMap<>();
    }

    @NonNull
    @Override
    public TransaksiParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaksi_rvitems, parent, false);
        return new TransaksiParentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransaksiParentViewHolder holder, int position) {
        ModelTransaksiParent currItem = dataList.get(position);
        holder.category.setText(currItem.getTransaksiCategory());
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.childTransaksi.getContext(),
                LinearLayoutManager.VERTICAL, false);
        layoutManager.setInitialPrefetchItemCount(currItem.getDataChild().size());

        TransaksiAdapter childAdapter = new TransaksiAdapter(context, currItem.getDataChild(), status, new ClickListener() {
            @Override
            public void onItemLongClick(View v, int position) {
                int id = currItem.getDataChild().get(position).getId();
                Toast.makeText(context, "ID : " + id, Toast.LENGTH_SHORT).show();

                PopupMenu popupMenu = new PopupMenu(context, holder.itemView);
                popupMenu.inflate(R.menu.akun_menu);
            }
        }, cartData, map);
        holder.childTransaksi.setLayoutManager(layoutManager);
        holder.childTransaksi.setAdapter(childAdapter);
        holder.childTransaksi.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class TransaksiParentViewHolder extends RecyclerView.ViewHolder {
        public TextView category;
        public RecyclerView childTransaksi;

        public TransaksiParentViewHolder(@NonNull View itemView) {
            super(itemView);

            category = itemView.findViewById(R.id.transaksi_category);
            childTransaksi = itemView.findViewById(R.id.transaksi_rvchild);
        }
    }
}

