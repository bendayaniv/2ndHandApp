package com.example.a2ndhandapp.Utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.a2ndhandapp.Models.Product;
import com.example.a2ndhandapp.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Database {
    private static FirebaseDatabase firebaseDB;
    private static Database instance = null;
    private static ArrayList<String> categories = new ArrayList<>();
    private static ArrayList<Product> allProducts = new ArrayList<>();
    private static ArrayList<User> allUsers = new ArrayList<>();

    public Database() {

    }

    public static void initDatabase(Context context) {
        if (instance == null) {
            instance = new Database();
        }
        if (firebaseDB == null) {
            firebaseDB = FirebaseDatabase.getInstance();
        }

        // Need to start listening to the database here before we want to get the information
        // from the database
        startListeningToDB();
    }

    /**
     * This method is used to read data from the database, and start listening to the database
     */
    private static void startListeningToDB() {
        getInstance().getCategories();

        getInstance().getAllUsers();

        getInstance().getAllProducts();
    }

    public static Database getInstance() {
        return instance;
    }

    public ArrayList<String> getCategories() {
        readData("Categories");
        return categories;
    }

    public ArrayList<Product> getAllProducts() {
        readData("Products");
        return allProducts;
    }

    public ArrayList<User> getAllUsers() {
        readData("AllUsers");
        return allUsers;
    }

    public void readData(String opt) {
        switch (opt) {
            case "Categories":
                DatabaseReference categoriesRef = firebaseDB.getReference("Categories");
                readStringALDataFromDB(categoriesRef, categories);
                break;
            case "AllUsers":
                DatabaseReference usersRef = firebaseDB.getReference("Users");
                getAllUsersFromDB(usersRef);
                break;
            case "Products":
                DatabaseReference productsRef = firebaseDB.getReference("Products");
                readProductsFromDB(productsRef);
                break;
            default:
                break;
        }
    }

    private void readProductsFromDB(DatabaseReference productsRef) {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allProducts.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Product product = ds.getValue(Product.class);
                    // TODO - to handle with images
                    allProducts.add(product);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase", "Error getting data" + error.getMessage());
            }
        });
    }

    /**
     * In case there is update that connects to a product
     * For example - user delete one of his products, so we need to remove the product from all the
     * users favorites lists and from his the specific user myProducts list
     *
     * @param usersRef
     */
    private void getAllUsersFromDB(DatabaseReference usersRef) {

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allUsers.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    if (ds.child("myFavorites").exists()) {
                        ArrayList<Product> favorites = new ArrayList<>();
                        for (DataSnapshot ds2 : ds.child("myFavorites").getChildren()) {
                            Product product = ds2.getValue(Product.class);
                            favorites.add(product);
                        }
                        user.setMyFavorites(favorites);
                    }
                    if (ds.child("MyProducts").exists()) {
                        ArrayList<Product> myProducts = new ArrayList<>();
                        for (DataSnapshot ds2 : ds.child("MyProducts").getChildren()) {
                            Product product = ds2.getValue(Product.class);
                            myProducts.add(product);
                        }
                        user.setMyProducts(myProducts);
                    }
                    allUsers.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase", "Error getting data" + error.getMessage());
            }
        });
    }

    public void readStringALDataFromDB(DatabaseReference reference, ArrayList<String> list) {
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
    }

    public void updateUser(User updatedUser) {
        DatabaseReference currentUserRef = firebaseDB.getReference("Users")
                .child(updatedUser.getUid());
        currentUserRef.setValue(updatedUser);
    }

    public void removeProduct(Product deletedProduct) {
        DatabaseReference productRef = firebaseDB.getReference("Products");
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allProducts.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Product product = ds.getValue(Product.class);
                    // TODO - to handle with images
                    if (deletedProduct.theSameProduct(product)) {
                    } else {
                        allProducts.add(product);
                    }
                }
                productRef.setValue(allProducts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase", "Error getting data" + error.getMessage());
            }
        });

    }
}
