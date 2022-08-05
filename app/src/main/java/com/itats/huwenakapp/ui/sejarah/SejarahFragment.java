package com.itats.huwenakapp.ui.sejarah;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itats.huwenakapp.R;
import com.itats.huwenakapp.ui.pesanan.ModelPesanan;
import com.itats.huwenakapp.ui.pesanan.PesananAdapter;

import java.util.ArrayList;

public class SejarahFragment extends Fragment {
    private RecyclerView recyclerView;
    private SejarahAdapter adapter;
    private ArrayList<ModelSejarah> dataList;
    SejarahViewModel sejarahViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        sejarahViewModel = new ViewModelProvider(this).get(SejarahViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sejarah, container, false);
        addData();
        recyclerView = root.findViewById(R.id.sejarahrv);
        recyclerView.setHasFixedSize(true);
        adapter = new SejarahAdapter(dataList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return root;
    }

    void addData() {
        dataList = new ArrayList<>();
        dataList.add(new ModelSejarah("11:00", "#AAAAA", "Ori Kacang x2", "", ""));
        dataList.add(new ModelSejarah("12:00", "#AAAAB", "Ori Cokelat x1", "Martabak Biasa x1", ""));
        dataList.add(new ModelSejarah("12:30", "#AAAAC", "Ori Kacang + Cokelat x1", "Martabak Spesial x1", "Ori Oreo x1"));
        dataList.add(new ModelSejarah("14:00", "#AAAAD", "Ori Cokelat + Keju Kraft x1", "Chocomaltine x1", ". . . . ."));
    }
}
