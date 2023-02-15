package com.example.a2ndhandapp.Controller.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.a2ndhandapp.Models.Product;
import com.example.a2ndhandapp.Models.User;
import com.example.a2ndhandapp.R;
import com.example.a2ndhandapp.Utils.CurrentUser;
import com.example.a2ndhandapp.Utils.Database;
import com.google.android.material.textview.MaterialTextView;

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
    private User seller;

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
            Glide.
                    with(this)
                    .load((String) null) // TODO:  change to currentProduct.getImages().get(0)
                    .placeholder(R.drawable.temporary_img)
                    .into((ImageView) view.findViewById(R.id.singleProduct_IMG_image));

            singleProduct_IMG_delete.setOnClickListener(v -> {
                Database.getInstance().removeProduct(currentProduct);
                ArrayList<User> allUsers = Database.getInstance().getAllUsers();

                for (User user : allUsers) {
                    if (user.isMyProduct(currentProduct)) {
                        user.removeProduct(currentProduct);
                    } else if (user.isFavorite(currentProduct)) {
                        user.removeFavorite(currentProduct);
                    }
                }
                // TODO - delete the product, and remove from the myList products of the user,
                //  and to update the DB
                // TODO - need to return to the previous fragment (which will be MyFragment)
            });
            if (!CurrentUser.getInstance().getUser().isMyProduct(currentProduct)) {
                // TODO - to check if this product belong to the current user,
                //  and if it does, show the close button
                singleProduct_IMG_delete.setVisibility(View.GONE);
            }

            singleProduct_LBL_name.setText(currentProduct.getName());
            singleProduct_LBL_category.setText(currentProduct.getCategory());
            singleProduct_LBL_price.setText(currentProduct.getPrice() + "₪");
            if (currentProduct.getDescription() == "") {
                singleProduct_EDT_description.setText("There is no description for this product.");
            } else {
                singleProduct_EDT_description.setText(currentProduct.getDescription());
            }
            setTheSeller();
            if (seller != null) {
                singleProduct_LBL_sellerDetails.setText(seller.getName() + ", " + seller.getEmail());
            }
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

    public void setTheSeller() {
        ArrayList<User> allUsers = Database.getInstance().getAllUsers();
        if (allUsers != null) {
            for (User user : allUsers) {
                if (user.isMyProduct(currentProduct)) {
                    seller = user;
                    break;
                }
            }
        }
    }
}
