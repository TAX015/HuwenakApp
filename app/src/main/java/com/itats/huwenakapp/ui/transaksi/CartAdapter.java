/*
Layout : card_keranjang.xml
Adapter for : TransaksiKeranjang.java

This is an Adapter class for TransaksiKeranjang.java
 */

package com.itats.huwenakapp.ui.transaksi;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itats.huwenakapp.R;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    ArrayList<ModelCart> dataList;

    public CartAdapter(ArrayList<ModelCart> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_keranjang, parent, false);

        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        int amount = dataList.get(position).getAmount();
        int price = dataList.get(position).getPrice();
        int total = price * amount;
        holder.txtName.setText(dataList.get(position).getName());
        holder.txtPrice.setText("Rp." + price);
        holder.edtAmount.setText(Integer.toString(amount));
        holder.txtTotal.setText("Rp." + total);
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public int grandTotal(ArrayList<ModelCart> dataList) {
        int totalPrice = 0;
        if (dataList != null) {
            for (int i=0; i<dataList.size(); i++) {
                totalPrice = totalPrice + dataList.get(i).getTotal();
            }
        }
        return totalPrice;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        int count, price, total;
        String s;
        TextView txtName, txtPrice, txtTotal;
        EditText edtAmount;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.cart_name);
            txtPrice = itemView.findViewById(R.id.cart_price);
            edtAmount = itemView.findViewById(R.id.edt_cart);
            txtTotal = itemView.findViewById(R.id.cart_total);

            edtAmount.setFocusable(false);

            itemView.findViewById(R.id.btnplus_cart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    s = txtPrice.getText().toString();
                    s = s.replaceAll("Rp\\.", "");
                    s = s.replaceAll("\\.", "");
                    price = Integer.parseInt(s);

                    count = Integer.parseInt(edtAmount.getText().toString());
                    count = count + 1;
                    edtAmount.setText(String.valueOf(count));
                    dataList.get(getAbsoluteAdapterPosition()).setAmount(count);
                    total = price * count;
                    dataList.get(getAbsoluteAdapterPosition()).setTotal(total);
                    txtTotal.setText("Rp." + total);

                    TransaksiKeranjang.txtGrandTotal();
                }
            });

            itemView.findViewById(R.id.btnmin_cart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    s = txtPrice.getText().toString();
                    s = s.replaceAll("Rp\\.", "");
                    s = s.replaceAll("\\.", "");
                    price = Integer.parseInt(s);

                    count = Integer.parseInt(edtAmount.getText().toString());
                    if (count > 1) {
                        count = count - 1;
                    }
                    edtAmount.setText(String.valueOf(count));
                    dataList.get(getAbsoluteAdapterPosition()).setAmount(count);
                    total = price * count;
                    dataList.get(getAbsoluteAdapterPosition()).setTotal(total);
                    txtTotal.setText("Rp." + total);

                    TransaksiKeranjang.txtGrandTotal();
                }
            });
        }
    }
}
