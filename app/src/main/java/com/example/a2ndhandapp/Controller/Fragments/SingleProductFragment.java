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
import com.example.a2ndhandapp.R;
import com.example.a2ndhandapp.Utils.CurrentUser;
import com.google.android.material.textview.MaterialTextView;

public class SingleProductFragment extends Fragment {
    private AppCompatImageView singleProduct_IMG_image;
    private MaterialTextView singleProduct_LBL_close;
    private MaterialTextView singleProduct_LBL_name;
    private MaterialTextView singleProduct_LBL_category;
    private MaterialTextView singleProduct_LBL_price;
    private MaterialTextView singleProduct_EDT_description;
    private MaterialTextView singleProduct_LBL_sellerDetails;
    private MaterialTextView singleProduct_LBL_date;
    private Product currentProduct;

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

            singleProduct_LBL_close.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Close", Toast.LENGTH_SHORT).show();
            });
            if (CurrentUser.getInstance().getUser() == null) {
                singleProduct_LBL_close.setVisibility(View.GONE);
            }

            singleProduct_LBL_name.setText(currentProduct.getName());
            singleProduct_LBL_category.setText(currentProduct.getCategory());
            singleProduct_LBL_price.setText(currentProduct.getPrice() + "₪");
            singleProduct_EDT_description.setText(currentProduct.getDescription());
        }
    }

    private void findViews(View view) {
        singleProduct_IMG_image = view.findViewById(R.id.singleProduct_IMG_image);
        singleProduct_LBL_close = view.findViewById(R.id.singleProduct_LBL_close);
        singleProduct_LBL_name = view.findViewById(R.id.singleProduct_LBL_name);
        singleProduct_LBL_category = view.findViewById(R.id.singleProduct_LBL_category);
        singleProduct_LBL_price = view.findViewById(R.id.singleProduct_LBL_price);
        singleProduct_EDT_description = view.findViewById(R.id.singleProduct_EDT_description);
        singleProduct_LBL_sellerDetails = view.findViewById(R.id.singleProduct_LBL_sellerDetails);
        singleProduct_LBL_date = view.findViewById(R.id.singleProduct_LBL_date);
    }

    public void setCurrentProduct(Product product) {
        this.currentProduct = product;
        if (singleProduct_EDT_description != null) {
            singleProduct_EDT_description.setText(product.getDescription());
        }
    }
}
