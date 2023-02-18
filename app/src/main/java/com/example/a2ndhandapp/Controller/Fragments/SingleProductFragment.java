package com.example.a2ndhandapp.Controller.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private ArrayList<Product> myProducts;
    private ArrayList<Product> favorites;

    public void setGoHomeCallback(GoHomeCallback goHomeCallback) {
        this.goHomeCallback = goHomeCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_product, container, false);

        findViews(view);

        initViews(view);

        return view;
    }

    @SuppressLint("SetTextI18n")
    public void initViews(View view) {
        if (currentProduct != null) {

            createProductDetails(view);

            if (!CurrentUser.getInstance().getUser().isMyProduct(currentProduct)) {
                creatingFavoritePart();
            } else {
                creatingDeletePart();
            }
        }
    }

    private void createProductDetails(View view) {
        singleProduct_LBL_name.setText(currentProduct.getName());
        singleProduct_LBL_category.setText(currentProduct.getCategory());
        singleProduct_LBL_price.setText(currentProduct.getPrice() + "₪");
        if (currentProduct.getDescription().equals("")) {
            singleProduct_EDT_description.setText("There is no description for this product.");
        } else {
            singleProduct_EDT_description.setText(currentProduct.getDescription());
        }
        singleProduct_LBL_sellerDetails.setText("Contact " + currentProduct.getSellerName() +
                ": " + currentProduct.getSellerEmail());
        Glide.
                with(this)
                .load((String) null) // TODO:  change to currentProduct.getImages().get(0)
                .placeholder(R.drawable.temporary_img)
                .into((ImageView) view.findViewById(R.id.singleProduct_IMG_image));
    }

    private void creatingDeletePart() {
        singleProduct_IMG_delete.setOnClickListener(v -> {

            removeDeletedProductFromUsersWhoLikeHim();

            // Remove the product from the user's list of products.
            CurrentUser.getInstance().getUser().removeProduct(currentProduct);

            DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Products");

            productRef.getRef().child(String.valueOf(currentProduct.getId())).removeValue();

            goHomeCallback.goHome();
        });
    }

    private void creatingFavoritePart() {
        if (CurrentUser.getInstance().getUser().isFavorite(currentProduct)) {
            singleProduct_IMG_delete.setImageResource(R.drawable.red_heart);
        } else {
            singleProduct_IMG_delete.setImageResource(R.drawable.white_heart);
        }
        singleProduct_IMG_delete.setOnClickListener(v -> {
            favoriteClick();
        });
    }

    private void favoriteClick() {
        if (CurrentUser.getInstance().getUser().isFavorite(currentProduct)) {
            CurrentUser.getInstance().getUser().removeFavorite(currentProduct);
            singleProduct_IMG_delete.setImageResource(R.drawable.white_heart);
        } else {
            CurrentUser.getInstance().getUser().addFavorite(currentProduct);
            singleProduct_IMG_delete.setImageResource(R.drawable.red_heart);
        }
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
     * This method removes the deleted product from all users who liked him.
     */
    private void removeDeletedProductFromUsersWhoLikeHim() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    if (favorites != null) {
                        favorites.clear();
                    } else {
                        favorites = new ArrayList<>();
                    }
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        User user = ds.getValue(User.class);
                        if (user != null) {
                            if (ds.child("myFavorites").exists()) {
                                for (DataSnapshot ds2 : ds.child("myFavorites").getChildren()) {
                                    Product product = ds2.getValue(Product.class);
                                    if (product != null) {
                                        if (user.isFavorite(product) && !product.theSameProduct(currentProduct)) {
                                            favorites.add(product);
                                        } else {
                                            ds2.getRef().removeValue();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}
