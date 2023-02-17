package com.example.a2ndhandapp.Controller.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.a2ndhandapp.Interfaces.GoHomeCallback;
import com.example.a2ndhandapp.Models.Product;
import com.example.a2ndhandapp.Models.User;
import com.example.a2ndhandapp.R;
import com.example.a2ndhandapp.Utils.CurrentUser;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SingleProductFragment extends Fragment {
//public class SingleProductFragment implements AdapterView.OnItemClickListener {

    private FirebaseDatabase firebaseDB;
    private ArrayList<User> allUsers = new ArrayList<>();
    private ArrayList<Product> allProducts = new ArrayList<>();
    ArrayList<Product> favorites = new ArrayList<>();
    ArrayList<Product> myProducts = new ArrayList<>();
    private AppCompatImageView singleProduct_IMG_image;
    private AppCompatImageView singleProduct_IMG_delete;
    private MaterialTextView singleProduct_LBL_name;
    private MaterialTextView singleProduct_LBL_category;
    private MaterialTextView singleProduct_LBL_price;
    private MaterialTextView singleProduct_EDT_description;
    private MaterialTextView singleProduct_LBL_sellerDetails;
    private Product currentProduct;
    //    private User seller;
    private GoHomeCallback goHomeCallback;

    public void setGoHomeCallback(GoHomeCallback goHomeCallback) {
        this.goHomeCallback = goHomeCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_product, container, false);

        firebaseDB = FirebaseDatabase.getInstance();

//        getAllUsersFromDB();

        findViews(view);

        initViews(view);

        return view;
    }

    @SuppressLint("SetTextI18n")
    public void initViews(View view) {
        if (currentProduct != null) {
            getAllUsersFromDB();
            singleProduct_LBL_name.setText(currentProduct.getName());
            singleProduct_LBL_category.setText(currentProduct.getCategory());
            singleProduct_LBL_price.setText(currentProduct.getPrice() + "₪");
//            if (currentProduct.getDescription() == "") {
            if (currentProduct.getDescription().equals("")) {
                singleProduct_EDT_description.setText("There is no description for this product.");
            } else {
                singleProduct_EDT_description.setText(currentProduct.getDescription());
            }
//            if (seller != null) {
//                singleProduct_LBL_sellerDetails.setText("Contact " + seller.getName() + ": " + seller.getEmail());
//            }
            singleProduct_LBL_sellerDetails.setText("Contact " + currentProduct.getSellerName() +
                    ": " + currentProduct.getSellerEmail());
            Glide.
                    with(this)
                    .load((String) null) // TODO:  change to currentProduct.getImages().get(0)
                    .placeholder(R.drawable.temporary_img)
                    .into((ImageView) view.findViewById(R.id.singleProduct_IMG_image));

            if (!CurrentUser.getInstance().getUser().isMyProduct(currentProduct)) {
                if (CurrentUser.getInstance().getUser().isFavorite(currentProduct)) {
                    singleProduct_IMG_delete.setImageResource(R.drawable.red_heart);
                } else {
                    singleProduct_IMG_delete.setImageResource(R.drawable.white_heart);
                }
                singleProduct_IMG_delete.setOnClickListener(v -> {
                    favoriteClick();
                });
            } else {
                singleProduct_IMG_delete.setOnClickListener(v -> {
                    removeProduct();

                    for (User user : allUsers) {
                        if (user.isMyProduct(currentProduct)) {
                            user.removeProduct(currentProduct);
//                            updateUser(user);
                            user.updateUser(firebaseDB);
                        } else if (user.isFavorite(currentProduct)) {
                            user.removeFavorite(currentProduct);
//                            updateUser(user);
                            user.updateUser(firebaseDB);
                        }
                    }
                    goHomeCallback.goHome();
                });
            }
        }
    }

    private void favoriteClick() {
        if (CurrentUser.getInstance().getUser().isFavorite(currentProduct)) {
            CurrentUser.getInstance().getUser().removeFavorite(currentProduct);
            singleProduct_IMG_delete.setImageResource(R.drawable.white_heart);
        } else {
            CurrentUser.getInstance().getUser().addFavorite(currentProduct);
            singleProduct_IMG_delete.setImageResource(R.drawable.red_heart);
        }
//        updateUser(CurrentUser.getInstance().getUser());
        CurrentUser.getInstance().getUser().updateUser(firebaseDB);
    }

    private void findViews(View view) {
        singleProduct_IMG_image = view.findViewById(R.id.singleProduct_IMG_image);
        singleProduct_IMG_delete = view.findViewById(R.id.singleProduct_IMG_delete);
        singleProduct_LBL_name = view.findViewById(R.id.singleProduct_LBL_name);
        singleProduct_LBL_category = view.findViewById(R.id.singleProduct_LBL_category);
        singleProduct_LBL_price = view.findViewById(R.id.singleProduct_LBL_price);
        singleProduct_EDT_description = view.findViewById(R.id.singleProduct_EDT_description);
        singleProduct_LBL_sellerDetails = view.findViewById(R.id.singleProduct_LBL_sellerDetails);
    }

    public void setCurrentProduct(Product product) {
        this.currentProduct = product;
        if (singleProduct_EDT_description != null) {
            singleProduct_EDT_description.setText(product.getDescription());
        }
    }


    /**
     * In case there is update that connects to a product
     * For example - user delete one of his products, so we need to remove the product from all the
     * users favorites lists and from his the specific user myProducts list
     */
    private void getAllUsersFromDB() {
        DatabaseReference usersRef = firebaseDB.getReference("Users");
        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
//                    ArrayList<Product> asd = new ArrayList<>();
                    allUsers.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        User user = ds.getValue(User.class);
                        if (user != null) {
                            if (ds.child("myFavorites").exists()) {
                                favorites.clear();
                                for (DataSnapshot ds2 : ds.child("myFavorites").getChildren()) {
                                    Product product = ds2.getValue(Product.class);
                                    if (user.isFavorite(product)) {
                                        favorites.add(product);
                                    }
                                }
                                user.setMyFavorites(favorites);
                            }
//                        ArrayList<Product> asd = new ArrayList<>();
                            if (ds.child("myProducts").exists()) {
                                myProducts.clear();
                                for (DataSnapshot ds2 : ds.child("myProducts").getChildren()) {
                                    Product product = ds2.getValue(Product.class);
                                    if (user.isMyProduct(product)) {
                                        myProducts.add(product);
//                                    if (currentProduct.theSameProduct(product)) {
//                                        product.setSellerName(user.getName());
//                                        product.setSellerEmail(user.getEmail());
//                                        asd.add(product);
//                                        Log.d("setTheSellersetTheSeller", "setTheSeller: " + user.getName());
////                                        this.seller = user;
////                                        Log.d("setTheSellersetTheSeller", "setTheSeller: " + seller.getName());
////                                        DatabaseReference productRef = firebaseDB.getReference("Products");
//                                    }
                                    }
                                }
                                user.setMyProducts(myProducts);
                            }
                            allUsers.add(user);
                        }
                    }
//                    DatabaseReference productRef = firebaseDB.getReference("Products");
//                    productRef.setValue(asd);
                }
            }
        });
    }


    public void removeProduct() {
        DatabaseReference productRef = firebaseDB.getReference("Products");
        productRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    allProducts.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Product product = ds.getValue(Product.class);
                        if (product != null) {
                            // TODO - to handle with images
//                            if (currentProduct.theSameProduct(product)) {
//
//                            } else {
//                                allProducts.add(product);
//                            }
                            if (!currentProduct.theSameProduct(product)) {
                                allProducts.add(product);
                            }

                        }
                    }
                    productRef.setValue(allProducts);
                }

            }
        });

//        productRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                allProducts.clear();
//                for (DataSnapshot ds : snapshot.getChildren()) {
//                    Product product = ds.getValue(Product.class);
//                    // TODO - to handle with images
//                    if (currentProduct.theSameProduct(product)) {
//
//                    } else {
//                        allProducts.add(product);
//                    }
//                }
//                productRef.setValue(allProducts);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d("firebase", "Error getting data" + error.getMessage());
//            }
//        });

    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if()
////        Product product = (Product) parent.getItemAtPosition(position);
////        if (product != null) {
////            if (product.theSameProduct(currentProduct)) {
////                Toast.makeText(getContext(), "You can't buy your own product", Toast.LENGTH_SHORT).show();
////            } else {
////                Intent intent = new Intent(getContext(), SingleProductActivity.class);
////                intent.putExtra("product", product);
////                startActivity(intent);
////            }
////        }
//    }


//    public void updateUser(User updatedUser) {
//        DatabaseReference currentUserRef = firebaseDB.getReference("Users")
//                .child(updatedUser.getUid());
//        currentUserRef.setValue(updatedUser);
//    }
}
