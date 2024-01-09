package com.app.gotosumbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.gotosumbar.Adapter.SearchAdapter;
import com.app.gotosumbar.Model.TempatWisata;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    SearchAdapter adapter;
    RecyclerView rv;
    ArrayList<TempatWisata> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Inisialisasi RecyclerView untuk menampilkan hasil pencarian
        rv = findViewById(R.id.rvSearch);
        adapter = new SearchAdapter(this, list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        rv.setVisibility(View.INVISIBLE);


        // Mendapatkan data dari Firebase Database dan menampilkannya dalam adapter
        ValueEventListener value = new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter.clear(); // Bersihkan adapter sebelum menambahkan data baru

                // Loop melalui hasil snapshot untuk menambahkan data ke dalam list
                for (DataSnapshot snap: snapshot.getChildren()){
                    TempatWisata data = snap.getValue(TempatWisata.class);
                    list.add(data);
                }
                adapter.notifyDataSetChanged(); // Perbarui tampilan setelah menambahkan data baru
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        
        // Query data dari Firebase Database untuk ditampilkan dalam pencarian
        Query addData = FirebaseDatabase.getInstance().getReference("Wisata");
        addData.addValueEventListener(value);

        // Inisialisasi SearchView dan memberikan respons saat teks berubah
         SearchView cari = findViewById(R.id.searchView);
         cari.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
             @Override
             public boolean onQueryTextSubmit(String query) {
                 return false;
             }

              // Saat teks berubah dalam SearchView, panggil metode filterData() untuk melakukan filter pada data
             @Override
             public boolean onQueryTextChange(String newText) {
                 filterData(newText);
                 return false;
             }
         });
        // Pengaturan Bottom Navigation
        BottomNavigationView navbar = findViewById(R.id.botNavbar);
        navbar.setSelectedItemId(R.id.search);
        navbar.setItemIconTintList(null);
        navbar.setOnItemSelectedListener(i -> {
            int itemId = i.getItemId();
             // Menangani pemilihan item pada bottom navigation dan memanggil activity yang sesuai
            if (itemId == R.id.search) {
                return true;
            } else if (itemId == R.id.akun) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            } else if (itemId == R.id.notif) {
                startActivity(new Intent(this, NotifActivity.class));
                return true;
            } else if (itemId == R.id.home) {
                startActivity(new Intent(this, HomeActivity.class));
                return true;
            }
            return false;
        });
    }

    private void filterData(String query) {
        ArrayList<TempatWisata> listCari = new ArrayList<>();
        if (query != null && !query.equals("")) {
            rv.setVisibility(View.INVISIBLE);
            for (TempatWisata i : list) {
                if (i.getNama().toLowerCase().contains(query.toLowerCase())) {
                    listCari.add(i);
                }
            } if (listCari.isEmpty()) {
                Log.d("Kosong", "data Kosong");
            } else {
                rv.setVisibility(View.VISIBLE);
                adapter.filterList(listCari);
            }
        } else {
            rv.setVisibility(View.INVISIBLE);
        }
    }
}
