package com.itats.huwenakapp.ui.akun;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itats.huwenakapp.ClickListener;
import com.itats.huwenakapp.R;

import java.util.ArrayList;

public class AkunAdapter extends RecyclerView.Adapter<AkunAdapter.AkunViewHolder> {
    int status;
    ClickListener clickListener;
    ArrayList<ModelAkun> dataList;

    public AkunAdapter(ArrayList<ModelAkun> dataList, ClickListener clickListener) {
        this.dataList = dataList;
        this.clickListener = clickListener;
    }

    public AkunAdapter(ArrayList<ModelAkun> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public AkunViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_akun, parent, false);
        return new AkunViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AkunViewHolder holder, int position) {
        holder.txtName.setText(dataList.get(position).getName());
        holder.txtBranch.setText(dataList.get(position).getBranch());
        status = dataList.get(position).getStatus();
        if (status == 1) {
            holder.txtStatus.setText("Admin");
        } else {
            holder.txtStatus.setText("Pegawai");
        }
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class AkunViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView txtName, txtBranch, txtStatus;

        public AkunViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.akun_name);
            txtBranch = itemView.findViewById(R.id.akun_branch);
            txtStatus = itemView.findViewById(R.id.akun_status);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onItemLongClick(view, getAbsoluteAdapterPosition());
            return false;
        }
    }
}
