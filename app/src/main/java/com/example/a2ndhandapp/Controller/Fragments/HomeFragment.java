package com.example.a2ndhandapp.Controller.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2ndhandapp.Adapters.ProductItemAdapter;
import com.example.a2ndhandapp.Interfaces.GetProductCallback;
import com.example.a2ndhandapp.Interfaces.ProductItemCallback;
import com.example.a2ndhandapp.Models.Product;
import com.example.a2ndhandapp.Models.User;
import com.example.a2ndhandapp.R;
import com.example.a2ndhandapp.Utils.CurrentUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FirebaseDatabase firebaseDB;
    private RecyclerView home_RV_products;
    private ArrayList<Product> productsByCategory = new ArrayList<>();
    private ProductItemAdapter productAdapter;
    private GetProductCallback getProductCallback;


    public void setGetProductCallback(GetProductCallback getProductCallback) {
        this.getProductCallback = getProductCallback;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        firebaseDB = FirebaseDatabase.getInstance();

        findViews(view);

//        initViews();
        initProductRV();

        return view;
    }


//    private void initViews() {
//        initProductRV();
//    }

    private void initProductRV() {
        readProductsByCategoryFromBD();

        productAdapter = new ProductItemAdapter(getContext(), productsByCategory);
        home_RV_products.setLayoutManager(new LinearLayoutManager(getContext()));
        home_RV_products.setAdapter(productAdapter);
        productAdapter.setProductItemCallback(new ProductItemCallback() {
            @Override
            public void favoriteClicked(Product product, int position) {
                if (CurrentUser.getInstance().getUser().isFavorite(product)) {
                    CurrentUser.getInstance().getUser().removeFavorite(product);
                } else {
                    CurrentUser.getInstance().getUser().addFavorite(product);
                }
//                updateUser(CurrentUser.getInstance().getUser());
                CurrentUser.getInstance().getUser().updateUser(firebaseDB);
                home_RV_products.getAdapter().notifyItemChanged(position);
//                Objects.requireNonNull(home_RV_products.getAdapter()).notifyItemChanged(position);
//                home_RV_products.getAdapter().notifyItemRemoved(position);

                // Is here for removing right away visually the product from the
                // main 'page' in case we like this product
                // (every product that we like will be removed from the main 'page')
//                initViews();
                initProductRV();
            }

            @Override
            public void itemClicked(Product product, int position) {
                if (getProductCallback != null) {
                    Toast.makeText(getContext(), "position:  " + position, Toast.LENGTH_SHORT).show();
                    getProductCallback.getProduct(product);
                }
            }
        });
//        }
    }

    private void findViews(View view) {
        home_RV_products = view.findViewById(R.id.home_RV_products);
    }


    @SuppressLint("NotifyDataSetChanged")
    public void readProductsByCategoryFromBD() {
        DatabaseReference productsRef = firebaseDB.getReference("Products");
        productsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    productsByCategory.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Product product = ds.getValue(Product.class);
                        if (product != null) {
                            if (!CurrentUser.getInstance().getUser().isMyProduct(product)
                                    && !CurrentUser.getInstance().getUser().isFavorite(product)) {
                                if (CurrentUser.getInstance().getCurrentCategory().equals(product.getCategory())) {
                                    productsByCategory.add(product);
                                } else if (CurrentUser.getInstance().getCurrentCategory().equals("All")) {
                                    productsByCategory.add(product);
                                }
                            }
                        }
                    }
                    productAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void updateUser(User updatedUser) {
        DatabaseReference currentUserRef = firebaseDB.getReference("Users")
                .child(updatedUser.getUid());
        currentUserRef.setValue(updatedUser);
    }

}
