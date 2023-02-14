package com.example.a2ndhandapp.Controller.Fragments;

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
import com.example.a2ndhandapp.R;
import com.example.a2ndhandapp.Utils.CurrentUser;
import com.example.a2ndhandapp.Utils.Database;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private ArrayList<String> categories = new ArrayList<>();
    private RecyclerView home_LST_products;
    private ProductItemAdapter productAdapter;
    private GetProductCallback getProductCallback;
    private FirebaseAuth mAuth;

    private ArrayList<Product> products = new ArrayList<>();

    public void setGetProductCallback(GetProductCallback getProductCallback) {
        this.getProductCallback = getProductCallback;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();

        findViews(view);

        initViews();

        return view;
    }

    /**
     * Init the products list that we show in the recycler view to the user according to his
     * searching
     */
    public void initProductsList() {
        products = Database.getInstance().getAllProducts();
        for (int i = products.size() - 1; i >= 0; i--) {
            if (CurrentUser.getInstance().getCurrentCategory().equals("Other")) {
                if (categories.contains(products.get(i).getCategory()))
                    products.remove(i);
            } else if (!CurrentUser.getInstance().getCurrentCategory().equals("All")) {
                if (!products.get(i).getCategory().equals(CurrentUser.getInstance().getCurrentCategory())) {
                    products.remove(i);
                }
            }
        }
        CurrentUser.getInstance().setCurrentShowingProducts(products);
    }

    private void initViews() {
        initProductsList();
        initProductRV();
    }

    private void initProductRV() {
        productAdapter = new ProductItemAdapter(getContext(), products);
        home_LST_products.setLayoutManager(new LinearLayoutManager(getContext()));
        home_LST_products.setAdapter(productAdapter);
        productAdapter.setProductItemCallback(new ProductItemCallback() {
            @Override
            public void favoriteClicked(Product product, int position) {
                if (CurrentUser.getInstance().getUser().isFavorite(product)) {
                    CurrentUser.getInstance().getUser().removeFavorite(product);
                    Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                } else {
                    CurrentUser.getInstance().getUser().addFavorite(product);
                    Toast.makeText(getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                }
                home_LST_products.getAdapter().notifyItemChanged(position);
            }

            @Override
            public void itemClicked(Product product, int position) {
                if (getProductCallback != null) {
                    Toast.makeText(getContext(), "position:  " + position, Toast.LENGTH_SHORT).show();
                    getProductCallback.getProduct(product);
                }
            }
        });
    }

    private void findViews(View view) {
        home_LST_products = view.findViewById(R.id.home_LST_products);
    }

}
