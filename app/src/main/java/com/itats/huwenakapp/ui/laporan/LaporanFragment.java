package com.itats.huwenakapp.ui.laporan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayout;
import com.itats.huwenakapp.R;

public class LaporanFragment extends Fragment {
    LaporanViewModel laporanViewModel;
    TabLayout tabLayout;
    FrameLayout frameLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        laporanViewModel = new ViewModelProvider(this).get(LaporanViewModel.class);
        View root = inflater.inflate(R.layout.fragment_laporan, container, false);
        tabLayout = root.findViewById(R.id.laporantab);
        frameLayout = root.findViewById(R.id.laporan_frame);

        //create tabs
        TabLayout.Tab dayTab = tabLayout.newTab();
        dayTab.setText("Hari");
        tabLayout.addTab(dayTab);
        TabLayout.Tab weekTab = tabLayout.newTab();
        weekTab.setText("Bulan");
        tabLayout.addTab(weekTab);
        TabLayout.Tab monthTab = tabLayout.newTab();
        monthTab.setText("Custom");
        tabLayout.addTab(monthTab);

        //Display DayTab the first time its open
        DayFragment fragment = new DayFragment();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.laporan_frame, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

        //Listener for tabs
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;

                switch (tab.getPosition()) {
                    case 0:
                        fragment = new DayFragment();
                        break;
                    case 1:
                        fragment = new WeekFragment();
                        break;
                    case 2:
                        fragment = new MonthFragment();
                        break;
                }

                FragmentManager fm = getChildFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.laporan_frame, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return root;
    }
}
