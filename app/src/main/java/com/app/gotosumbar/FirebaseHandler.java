package com.app.gotosumbar;

import android.app.Application;
// Membuat kelas FirebaseHandler yang merupakan turunan dari kelas Application
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHandler extends Application {
    // Override method onCreate() dari kelas Application
    @Override
    public void onCreate() {
        super.onCreate(); // Memanggil metode onCreate() kelas induk
         // Mengaktifkan mode persistensi untuk instance FirebaseDatabase
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
