/*
Layout : card_transaksi.xml
Child Adapter of : TransaksiFragment.java
Parent Adapter : TransaksiAdapterParent.java

This is the child adapter of TransaksiFragment where the list CardView of the menu will be set
 */

package com.itats.huwenakapp.ui.transaksi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.itats.huwenakapp.ClickListener;
import com.itats.huwenakapp.MenuDrawer;
import com.itats.huwenakapp.R;
import com.itats.huwenakapp.koneksi.Servers;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransaksiAdapter extends RecyclerView.Adapter<TransaksiAdapter.TransaksiViewHolder> {
    int status;
    ClickListener clickListener;
    private ArrayList<ModelTransaksi> dataList;
    public Context context;
    AlertDialog alertDialog;
    ArrayList<ModelCart> cartData;
    HashMap<Integer, Integer> map;

    public TransaksiAdapter(ArrayList<ModelTransaksi> dataList) {
        this.dataList = dataList;
    }

    public TransaksiAdapter(Context context, ArrayList<ModelTransaksi> dataList,int status,
                            ClickListener clickListener, ArrayList<ModelCart> cartData, HashMap<Integer, Integer> map) {
        this.context = context;
        this.dataList = dataList;
        this.status = status;
        this.clickListener = clickListener;
        this.cartData = cartData;
        this.map = map;
    }

    @NonNull
    @Override
    public TransaksiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_transaksi, parent, false);

        return new TransaksiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransaksiViewHolder holder, int position) {
        ModelTransaksi modelTransaksi = dataList.get(position);
        holder.txtName.setText(dataList.get(position).getName());
        holder.txtPrice.setText(dataList.get(position).getPrice1()+"");
        holder.modelTransaksi = modelTransaksi;

        Glide.with(holder.itemView.getContext())
                .load(Servers.IMAGE_PATH + dataList.get(position).getImage()).into(holder.transaksiImage);

        if (status == 2) {
            holder.btnDel.setVisibility(View.INVISIBLE);
        }

        if (dataList.get(position).getSize() == 0) {
            holder.btnSedang.setVisibility(View.INVISIBLE);
            holder.btnBesar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class TransaksiViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private int count;
        private ImageView transaksiImage;
        private TextView txtName, txtPrice;
        private Button btnPlus, btnMin, btnTambah, btnSedang, btnBesar;
        ImageButton btnDel;
        private EditText edtCount;
        ModelTransaksi modelTransaksi;

        public TransaksiViewHolder(@NonNull View itemView) {
            super(itemView);
            transaksiImage = (ImageView) itemView.findViewById(R.id.transaksilist_image);
            txtName = (TextView) itemView.findViewById(R.id.transaksilist_name);
            txtPrice = (TextView) itemView.findViewById(R.id.transaksilist_price);
            edtCount = (EditText) itemView.findViewById(R.id.transaksi_edtcount);
            btnSedang = (Button) itemView.findViewById(R.id.transaksi_btnsedang);
            btnBesar = (Button) itemView.findViewById(R.id.transaksi_btnbesar);
            btnDel = itemView.findViewById(R.id.transaksi_btndel);
            btnTambah = itemView.findViewById(R.id.transaksi_btntambah);

            itemView.setOnLongClickListener(this);

            btnSedang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnBesar.setSelected(false);
                    btnSedang.setSelected(true);
                    txtPrice.setText(String.valueOf(dataList.get(getAbsoluteAdapterPosition()).getPrice1()));
                }
            });

            btnBesar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnSedang.setSelected(false);
                    btnBesar.setSelected(true);
                    txtPrice.setText(String.valueOf(dataList.get(getAbsoluteAdapterPosition()).getPrice2()));
                }
            });

            btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = dataList.get(getAbsoluteAdapterPosition()).getId();
                    Toast.makeText(context, "ID : " + id, Toast.LENGTH_SHORT).show();
                    deleteMenu(id);
                }
            });

            itemView.findViewById(R.id.transaksi_btnplus).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    count = Integer.parseInt(edtCount.getText().toString());
                    count = count + 1;
                    edtCount.setText(String.valueOf(count));
                }
            });

            itemView.findViewById(R.id.transaksi_btnmin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    count = Integer.parseInt(edtCount.getText().toString());
                    if (count > 0) {
                        count = count - 1;
                    }
                    edtCount.setText(String.valueOf(count));
                }
            });

            itemView.findViewById(R.id.transaksi_btntambah).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String amount = edtCount.getText().toString();
                    int amountInt = Integer.parseInt(amount);
                    int total = modelTransaksi.getPrice1() * amountInt;

                    if (amountInt <= 0) {
                        Toast.makeText(context, "Masukan jumlah pesanan terlebih dahulu", Toast.LENGTH_SHORT).show();
                    } else if (map.containsKey(modelTransaksi.getId())) {
                        Toast.makeText(context, "Menu ini sudah di keranjang", Toast.LENGTH_SHORT).show();
                    } else {
                        cartData.add(new ModelCart(modelTransaksi.getId(), modelTransaksi.getName(),
                                modelTransaksi.getPrice1(), amountInt, total));
                        map.put(modelTransaksi.getId(), modelTransaksi.getId());
                        MenuDrawer.cartList = cartData;
                        Toast.makeText(context, "Tambah Pesanan " + modelTransaksi.getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onItemLongClick(view, getAbsoluteAdapterPosition());
            return false;
        }

        public void deleteMenu(int id) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("Hapus menu ini?");
            dialog.setCancelable(false)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            RequestQueue requestQueue = Volley.newRequestQueue(context);
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Servers.DEL_MENU_URL, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jObj = new JSONObject(response);
                                        int success = jObj.getInt("success");

                                        if (success == 1) {
                                            Toast.makeText(context, jObj.getString("message"),
                                                    Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(context, jObj.getString("message"),
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
                                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
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
}
