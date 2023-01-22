package com.example.a2ndhandapp.Controller.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.example.a2ndhandapp.Models.Product;
//import com.example.a2ndhandapp.Models.User;
import com.example.a2ndhandapp.R;
import com.example.a2ndhandapp.Utils.ImageLoader;
import com.google.android.material.textview.MaterialTextView;

public class SingleProductFragment extends Fragment {

//    private View view;
    private AppCompatImageView singleProduct_IMG_image;
    private MaterialTextView singleProduct_LBL_name;
    private MaterialTextView singleProduct_LBL_category;
    private MaterialTextView singleProduct_LBL_price;
    private MaterialTextView singleProduct_EDT_description;
    private MaterialTextView singleProduct_LBL_sellerDetails;
    private MaterialTextView singleProduct_LBL_date;
    private Product currentProduct;
//    private GetProductCallback getProductCallback;
//
//    public void setGetProductCallback(GetProductCallback getProductCallback) {
//        this.getProductCallback = getProductCallback;
//    }

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
        if (currentProduct != null /*&& view != null*/) {
//        singleProduct_IMG_image.setImageResource(Integer.parseInt(currentProduct.getImages().get(0)));
            ImageLoader.getInstance().load(null, view.findViewById(R.id.singleProduct_IMG_image));
            singleProduct_LBL_name.setText(currentProduct.getName());
            singleProduct_LBL_category.setText(currentProduct.getCategory());
            singleProduct_LBL_price.setText(currentProduct.getPrice());
            singleProduct_EDT_description.setText(currentProduct.getDescription());
//            singleProduct_LBL_sellerDetails.setText(currentProduct.getUser().getName()+ ", "
//                    + currentProduct.getUser().getPhone() + ", " + currentProduct.getUser().getAddress());
        }
    }

    private void findViews(View view) {
        singleProduct_IMG_image = view.findViewById(R.id.singleProduct_IMG_image);
        singleProduct_LBL_name = view.findViewById(R.id.singleProduct_LBL_name);
        singleProduct_LBL_category = view.findViewById(R.id.singleProduct_LBL_category);
        singleProduct_LBL_price = view.findViewById(R.id.singleProduct_LBL_price);
        singleProduct_EDT_description = view.findViewById(R.id.singleProduct_EDT_description);
        singleProduct_LBL_sellerDetails = view.findViewById(R.id.singleProduct_LBL_sellerDetails);
        singleProduct_LBL_date = view.findViewById(R.id.singleProduct_LBL_date);
    }

    public void setCurrentProduct(Product product) {
//        Toast.makeText(getContext(), "Description:  " + product.getDescription(), Toast.LENGTH_SHORT).show();
        Log.d("Description", "Description: " + product.getDescription());
        this.currentProduct = product;
        if(singleProduct_EDT_description != null) {
            singleProduct_EDT_description.setText(product.getDescription());
        }
//        singleProduct_EDT_description.setText(currentProduct.getDescription());
//        initViews();
    }
}
