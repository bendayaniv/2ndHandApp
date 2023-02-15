package com.example.a2ndhandapp.Controller.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2ndhandapp.Adapters.MyProductItemAdapter;
import com.example.a2ndhandapp.Adapters.ProductItemAdapter;
import com.example.a2ndhandapp.Interfaces.GetProductCallback;
import com.example.a2ndhandapp.Interfaces.MyProductItemCallback;
import com.example.a2ndhandapp.Interfaces.ThereIsProductsCallback;
import com.example.a2ndhandapp.Models.Product;
import com.example.a2ndhandapp.Models.User;
import com.example.a2ndhandapp.R;
import com.example.a2ndhandapp.Utils.CurrentUser;
import com.example.a2ndhandapp.Utils.Database;

import java.util.ArrayList;

public class MyFragment extends Fragment {

    private RecyclerView my_RV_products;
    private ArrayList<Product> products = new ArrayList<>();
    private MyProductItemAdapter myProductAdapter;
    private GetProductCallback getProductCallback;
    private ThereIsProductsCallback thereIsProductsCallback;

    public void setGetProductCallback(GetProductCallback getProductCallback) {
        this.getProductCallback = getProductCallback;
    }

    public void setThereIsProductsCallback(ThereIsProductsCallback thereIsProductsCallback) {
        this.thereIsProductsCallback = thereIsProductsCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        findViews(view);

        initViews();

        return view;
    }

    /**
     * Init the products list that we show in the recycler view to the user
     * according to his own products
     */
    public void initProductsList() {
        products = Database.getInstance().getAllProducts();
        ArrayList<Product> myProducts = CurrentUser.getInstance().getUser().getMyProducts();
        if (myProducts != null && !myProducts.isEmpty()
                && products != null && !products.isEmpty()) {
            for (int i = products.size() - 1; i >= 0; i--) {
                for (int j = 0; j < myProducts.size(); j++) {
                    if (products.get(i).theSameProduct(myProducts.get(j))) {
                        break;
                    }
                    if (j == myProducts.size() - 1) {
                        products.remove(i);
                    }
                }
            }
        }
        CurrentUser.getInstance().setCurrentShowingProducts(products);
        initProductRV();
        thereIsProductsCallback.thereIsProducts();
    }

    private void initProductRV() {
        myProductAdapter = new MyProductItemAdapter(getContext(), /*products*/ CurrentUser.getInstance().getUser().getMyProducts());
        my_RV_products.setLayoutManager(new LinearLayoutManager(getContext()));
        my_RV_products.setAdapter(myProductAdapter);
        myProductAdapter.setMyProductItemCallback(new MyProductItemCallback() {

            @Override
            public void deleteClicked(Product product, int position) {
                Database.getInstance().removeProduct(product);

                ArrayList<User> allUsers = Database.getInstance().getAllUsers();

                for (User user : allUsers) {
                    if (user.isMyProduct(product)) {
                        user.removeProduct(product);
                    } else if (user.isFavorite(product)) {
                        user.removeFavorite(product);
                    }
                }
                my_RV_products.getAdapter().notifyItemRemoved(position);
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

    private void initViews() {
        initProductsList();
    }

    private void findViews(View view) {
        my_RV_products = view.findViewById(R.id.my_RV_products);
    }
}
