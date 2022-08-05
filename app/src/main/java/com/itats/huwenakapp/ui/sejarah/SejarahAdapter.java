package com.itats.huwenakapp.ui.sejarah;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itats.huwenakapp.R;

import java.util.ArrayList;

public class SejarahAdapter extends RecyclerView.Adapter<SejarahAdapter.SejarahViewHolder> {
    private ArrayList<ModelSejarah> dataList;

    public SejarahAdapter(ArrayList<ModelSejarah> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public SejarahViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_sejarah, parent, false);
        return new SejarahViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SejarahViewHolder holder, int position) {
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

    public class SejarahViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTime, txtId, txtOrder1, txtOrder2, txtOrder3;

        public SejarahViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTime = itemView.findViewById(R.id.sejarah_time);
            txtId = itemView.findViewById(R.id.sejarah_id);
            txtOrder1 = itemView.findViewById(R.id.sejarah_order1);
            txtOrder2 = itemView.findViewById(R.id.sejarah_order2);
            txtOrder3 = itemView.findViewById(R.id.sejarah_order3);
        }
    }
}
