package com.itats.huwenakapp.ui.cabang;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.itats.huwenakapp.ClickListener;
import com.itats.huwenakapp.R;
import com.itats.huwenakapp.koneksi.Servers;

import java.util.ArrayList;

public class CabangAdapter extends RecyclerView.Adapter<CabangAdapter.CabangViewHolder> {
    ClickListener clickListener;
    ArrayList<ModelCabang> dataList;

    public CabangAdapter(ArrayList<ModelCabang> dataList, ClickListener clickListener) {
        this.dataList = dataList;
        this.clickListener = clickListener;
    }

    public CabangAdapter(ArrayList<ModelCabang> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public CabangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_cabang, parent, false);
        return new CabangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CabangViewHolder holder, int position) {
        ModelCabang modelCabang = dataList.get(position);
        holder.txtName.setText(dataList.get(position).getName());
        holder.txtAddress.setText(dataList.get(position).getLocation());
        holder.modelCabang = modelCabang;

        //Load the image
        Glide.with(holder.itemView.getContext())
                .load(Servers.IMAGE_PATH + dataList.get(position).getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class CabangViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        ImageView image;
        TextView txtName, txtAddress;
        ModelCabang modelCabang;

        public CabangViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.cabang_image);
            txtName = itemView.findViewById(R.id.cabang_name);
            txtAddress = itemView.findViewById(R.id.cabang_address);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onItemLongClick(view, getAbsoluteAdapterPosition());
            return false;
        }
    }
}
