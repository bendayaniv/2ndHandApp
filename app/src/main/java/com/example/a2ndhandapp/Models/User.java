package com.example.a2ndhandapp.Models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class User {
    private String name;
    private String email;
    private String uid;
    private ArrayList<Product> myProducts;
    private ArrayList<Product> myFavorites;


    public User() {
    }

    public User(String name, String email, String uid) {
        this.name = name;
        this.email = email;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public User setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public ArrayList<Product> getMyProducts() {
        return myProducts;
    }

    public User setMyProducts(ArrayList<Product> myProducts) {
        this.myProducts = myProducts;
        return this;
    }

    public User addProduct(Product product) {
        if (this.myProducts == null) {
            this.myProducts = new ArrayList<>();
        }
        this.myProducts.add(product);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").getRef()
                .child(this.getUid());
        userRef.setValue(this);

        return this;
    }

    public User removeProduct(Product product) {
        if (this.myProducts != null) {
            for (int i = 0; i < myProducts.size(); i++) {
//                if (theSameProduct(product, i)) {
                if (this.myProducts.get(i).theSameProduct(product)) {
                    this.myProducts.remove(this.myProducts.get(i));
                    break;
                }
            }
        }

        FirebaseDatabase.getInstance().getReference("Users").getRef()
                .child(this.getUid()).child("myProducts").getRef().setValue(this.myProducts);
        return this;
    }

    public boolean isMyProduct(Product product) {
        if (this.myProducts != null) {
            for (int i = 0; i < myProducts.size(); i++) {
                if (this.myProducts.get(i).theSameProduct(product))
                    return true;
            }
        }
        return false;
    }

    public ArrayList<Product> getMyFavorites() {
        return myFavorites;
    }

    public User setMyFavorites(ArrayList<Product> myFavorites) {
        this.myFavorites = myFavorites;
        return this;
    }

    public User addFavorite(Product product) {
        if (this.myFavorites == null) {
            this.myFavorites = new ArrayList<>();
        }
        this.myFavorites.add(product);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").getRef()
                .child(this.getUid());
        userRef.setValue(this);
        return this;
    }

    public User removeFavorite(Product product) {
        if (this.myFavorites != null) {
            for (int i = 0; i < myFavorites.size(); i++) {
                if (this.myFavorites.get(i).theSameProduct(product)) {
                    this.myFavorites.remove(this.myFavorites.get(i));
                    break;
                }
            }
        }

        FirebaseDatabase.getInstance().getReference("Users").getRef()
                .child(this.getUid()).child("myFavorites").getRef().setValue(this.myFavorites);
        return this;
    }

    public boolean isFavorite(Product product) {
        if (this.myFavorites != null) {
            for (int i = 0; i < myFavorites.size(); i++) {
                if (this.myFavorites.get(i).theSameProduct(product))
                    return true;
            }
        }
        return false;
    }
}
