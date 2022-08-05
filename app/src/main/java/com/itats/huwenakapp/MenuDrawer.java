package com.itats.huwenakapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.itats.huwenakapp.ui.akun.AkunTambah;
import com.itats.huwenakapp.ui.cabang.CabangTambah;
import com.itats.huwenakapp.ui.kategori.KategoriTambah;
import com.itats.huwenakapp.ui.transaksi.ModelCart;
import com.itats.huwenakapp.ui.transaksi.TransaksiKeranjang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

    public class MenuDrawer extends AppCompatActivity {
    boolean session;
    int status;
    String name, location;
    SharedPreferences sharedPreferences;
    private AppBarConfiguration mAppBarConfiguration;
    TextView headName, headBranch;
    public static ArrayList<ModelCart> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences(Login.SHARED, Context.MODE_PRIVATE);
        session = sharedPreferences.getBoolean(Login.TAG_SESSION, false);
        status = sharedPreferences.getInt(Login.TAG_STATUS, 2);
        name = sharedPreferences.getString(Login.TAG_NAME, null);
        location = sharedPreferences.getString(Login.TAG_LOCATION, null);

        name = getIntent().getStringExtra(Login.TAG_NAME);
        location = getIntent().getStringExtra(Login.TAG_LOCATION);

        FloatingActionButton fab = findViewById(R.id.fab);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_transaksi, R.id.nav_kategori, R.id.nav_pesanan, R.id.nav_sejarah, R.id.nav_inventory,
                R.id.nav_akun, R.id.nav_cabang, R.id.nav_laporan, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //Hide menu
        navigationView.getMenu().findItem(R.id.nav_sejarah).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_inventory).setVisible(false);
        if (status == 2) {
            navigationView.getMenu().findItem(R.id.nav_kategori).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_akun).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_cabang).setVisible(false);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (navigationView.getMenu().findItem(R.id.nav_transaksi).isChecked()) {
                    TransaksiKeranjang.setDataList(cartList);
                    Intent intent = new Intent(getApplicationContext(), TransaksiKeranjang.class);
                    startActivity(intent);
                } else if (navigationView.getMenu().findItem(R.id.nav_kategori).isChecked()) {
                    Intent intent = new Intent(getApplicationContext(), KategoriTambah.class);
                    startActivity(intent);
                } else if (navigationView.getMenu().findItem(R.id.nav_cabang).isChecked()) {
                    Intent intent = new Intent(getApplicationContext(), CabangTambah.class);
                    startActivity(intent);
                } else if (navigationView.getMenu().findItem(R.id.nav_akun).isChecked()) {
                    Intent intent = new Intent(getApplicationContext(), AkunTambah.class);
                    startActivity(intent);
                }
            }
        });

        //Change fab icon when branch fragment open
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if ((destination.getId() == R.id.nav_cabang) || (destination.getId() == R.id.nav_akun)
                || (destination.getId() == R.id.nav_kategori)) {
                    fab.show();
                    fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.icon_add));
                } else if (destination.getId() == R.id.nav_transaksi){
                    fab.show();
                    fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.icon_cart));
                } else {
                    fab.hide();
                }
            }
        });

        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(menuItem -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(MenuDrawer.this, Login.class);
            startActivity(intent);
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_drawer, menu);
        headName = findViewById(R.id.head_name);
        headBranch = findViewById(R.id.head_branch);

        headName.setText(name);
        headBranch.setText(location);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}