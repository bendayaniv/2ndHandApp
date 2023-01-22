package com.example.a2ndhandapp.Controller.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2ndhandapp.Adapters.ProductAdapter;
import com.example.a2ndhandapp.Interfaces.GetProductCallback;
import com.example.a2ndhandapp.Interfaces.ProductCallback;
import com.example.a2ndhandapp.Models.Product;
import com.example.a2ndhandapp.Models.User;
import com.example.a2ndhandapp.R;
import com.example.a2ndhandapp.Utils.DataManager;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView home_LST_products;
    private ProductAdapter productAdapter;
    private User currentUser;
    private GetProductCallback getProductCallback;

    public void setGetProductCallback(GetProductCallback getProductCallback) {
        this.getProductCallback = getProductCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        findViews(view);

        initViews();

        return view;
    }

    public static ArrayList<Product> getProducts() {
        ArrayList<Product> products = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            products.add(new Product("Product " + i,
                    "asd " + i,
                    "Price " + i,
                    "Category " + i,
                    "UID " + i,
                    null, null));
        }
        return products;
    }

    private void initViews() {
        productAdapter = new ProductAdapter(getContext(), /*null*//*DataManager.getProducts()*/getProducts());
        home_LST_products.setLayoutManager(new LinearLayoutManager(getContext()));
        home_LST_products.setAdapter(productAdapter);
        productAdapter.setProductCallback(new ProductCallback() {

            @Override
            public void favoriteClicked(Product product, int position) {
//                Toast.makeText(getContext(), "Favorite clicked " + product.getName(), Toast.LENGTH_SHORT).show();
                if (currentUser != null) {
                    if (currentUser.getFavorites().contains(product)) {
                        currentUser.getFavorites().remove(product);
                        Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                    } else {
                        currentUser.getFavorites().add(product);
                        Toast.makeText(getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                    }
                    home_LST_products.getAdapter().notifyItemChanged(position);
                }
//                if (getProductCallback != null) {
//                    Toast.makeText(getContext(), "Favorite clicked " + product.getName(), Toast.LENGTH_SHORT).show();
//                    getProductCallback.getProduct(product);
//                }
            }


            @Override
            public void itemClicked(Product product, int position) {
//                Toast.makeText(getContext(), "" + product.getName() + " has been clicked!", Toast.LENGTH_SHORT).show();
                if (getProductCallback != null) {
//                    Toast.makeText(getContext(), "" + product.getName() + " has been clicked!", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getContext(), "Description:  " + product.getDescription(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "position:  " + position, Toast.LENGTH_SHORT).show();
                    getProductCallback.getProduct(product);
                }
//                // TODO - To use callback to transfer the information to the main activity so I know what to do
////                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, new HomeFragment()).commit(); // Set the home fragment
////                getSupportActionBar().setTitle("Home"); // Set the title of the toolbar
            }
        });
    }

    private void findViews(View view) {
        home_LST_products = view.findViewById(R.id.home_LST_products);
    }


}
