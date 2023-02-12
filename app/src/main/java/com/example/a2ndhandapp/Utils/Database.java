package com.example.a2ndhandapp.Utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Database {
    //    private FirebaseAuth mAuth;
    private static FirebaseDatabase firebaseDB;
    private static Database instance = null;
    private static ArrayList<String> categories = new ArrayList<>();
    private static ArrayList<String> prices = new ArrayList<>();

    public Database() {

    }

    public static void initDatabase(Context context) {
        if (instance == null) {
            instance = new Database();
        }
        if (firebaseDB == null) {
            firebaseDB = FirebaseDatabase.getInstance();
        }
    }

    public static Database getInstance() {
        return instance;
    }

    public static ArrayList<String> getCategories() {
        readData("Categories");
        Log.d("firebase", "getCategories: " + categories);
        return categories;
    }

    public static ArrayList<String> getPrices() {
        readData("Prices");
        return prices;
    }

    public static void readData(String opt) {
        switch (opt) {
            case "Categories":
                DatabaseReference categoriesRef = firebaseDB.getReference("Categories");
                readDataFromDB(categoriesRef, categories);
                break;
            case "Prices":
                DatabaseReference pricesRef = firebaseDB.getReference("Prices");
                readDataFromDB(pricesRef, prices);
                break;
            default:
                break;
        }
    }

    public static void readDataFromDB(DatabaseReference reference, ArrayList<String> list) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    list.add(ds.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase", "Error getting data" + error.getMessage());
            }
        });




//        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Log.d("firebase", "Error getting data" + task.getException());
//                } else {
//                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                    for (DataSnapshot ds : task.getResult().getChildren()) {
//                        list.add(ds.getValue().toString());
//                    }
//                }
//            }
//        });
    }

}
