package com.itats.huwenakapp.ui.pesanan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itats.huwenakapp.R;

import java.util.ArrayList;

public class PesananAdapter extends RecyclerView.Adapter<PesananAdapter.PesananViewHolder> {
    private ArrayList<ModelPesanan> dataList;

    public PesananAdapter(ArrayList<ModelPesanan> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public PesananViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_pesanan, parent, false);
        return new PesananViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PesananViewHolder holder, int position) {
        holder.txtOrigin.setText(dataList.get(position).getOrigin());
        holder.txtTime.setText(dataList.get(position).getTime());
        holder.txtId.setText(dataList.get(position).getId());
        holder.txtOrder1.setText(dataList.get(position).getOrder1());
        holder.txtOrder2.setText(dataList.get(position).getOrder2());
        holder.txtOrder3.setText(dataList.get(position).getOrder3());
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class PesananViewHolder extends RecyclerView.ViewHolder {
        public TextView txtOrigin, txtTime, txtId, txtOrder1, txtOrder2, txtOrder3;
        public Button btnPesanan;

        public PesananViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrigin = itemView.findViewById(R.id.pesanan_origin);
            txtTime = itemView.findViewById(R.id.pesanan_time);
            txtId = itemView.findViewById(R.id.pesanan_id);
            txtOrder1 = itemView.findViewById(R.id.pesanan_order1);
            txtOrder2 = itemView.findViewById(R.id.pesanan_order2);
            txtOrder3 = itemView.findViewById(R.id.pesanan_order3);
            btnPesanan = itemView.findViewById(R.id.pesanan_btn);
        }
    }
}
