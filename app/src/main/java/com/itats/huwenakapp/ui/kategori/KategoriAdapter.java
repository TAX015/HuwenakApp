package com.itats.huwenakapp.ui.kategori;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itats.huwenakapp.ClickListener;
import com.itats.huwenakapp.R;

import java.util.ArrayList;

public class KategoriAdapter extends RecyclerView.Adapter<KategoriAdapter.KategoriViewHolder> {
    ClickListener clickListener;
    ArrayList<ModelKategori> dataList;

    public KategoriAdapter(ArrayList<ModelKategori> dataList) {
        this.dataList = dataList;
    }

    public KategoriAdapter(ArrayList<ModelKategori> dataList, ClickListener clickListener) {
        this.dataList = dataList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public KategoriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_kategori, parent, false);
        return new KategoriViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KategoriViewHolder holder, int position) {
        holder.txtName.setText(dataList.get(position).getName());
        holder.txtDesc.setText(dataList.get(position).getDesc());
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class KategoriViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView txtName, txtDesc;

        public KategoriViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.cat_name);
            txtDesc = itemView.findViewById(R.id.cat_desc);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onItemLongClick(view, getAbsoluteAdapterPosition());
            return false;
        }
    }
}
