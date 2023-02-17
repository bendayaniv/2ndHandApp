package com.example.a2ndhandapp.Models;

//import com.example.a2ndhandapp.Utils.Database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class User {

    //    private FirebaseDatabase firebaseDB;
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
        // TODO - add product to database
//        Database.getInstance().updateUser(this);
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
        // TODO - remove product from database
//        Database.getInstance().updateUser(this);
        return this;
    }

    public boolean isMyProduct(Product product) {
        if (this.myProducts != null) {
            for (int i = 0; i < myProducts.size(); i++) {
//                if (theSameProduct(product, i))
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
//        Database.getInstance().updateUser(this);
        return this;
    }

    public User removeFavorite(Product product) {
        if (this.myFavorites != null) {
            for (int i = 0; i < myFavorites.size(); i++) {
//                if (theSameProduct(product, i)) {
                if (this.myFavorites.get(i).theSameProduct(product)) {
//                    this.myFavorites.remove(i);
                    this.myFavorites.remove(this.myFavorites.get(i));
                    break;
                }
            }
        }
//        Database.getInstance().updateUser(this);
        return this;
    }

    public boolean isFavorite(Product product) {
        if (this.myFavorites != null) {
            for (int i = 0; i < myFavorites.size(); i++) {
//                if (theSameProduct(product, i))
                if (this.myFavorites.get(i).theSameProduct(product))
                    return true;
            }
        }
        return false;
    }

//    public boolean theSameProduct(Product product, int index) {
//        if (myFavorites.get(index).getCategory().equals(product.getCategory())
//                && myFavorites.get(index).getName().equals(product.getName())
//                && myFavorites.get(index).getPrice().equals(product.getPrice())
//                && myFavorites.get(index).getDescription().equals(product.getDescription())) {
//            return true;
//        }
//        return false;
//    }


//    public void updateUser(User updatedUser) {
//        DatabaseReference currentUserRef = firebaseDB.getReference("Users")
//                .child(updatedUser.getUid());
//        currentUserRef.setValue(updatedUser);
//    }

    public void updateUser(/*User updatedUser,*/ FirebaseDatabase firebaseDB) {
        DatabaseReference currentUserRef = firebaseDB.getReference("Users")
                .child(getUid());
        currentUserRef.setValue(this);
    }
}
