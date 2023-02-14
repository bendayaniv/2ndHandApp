package com.example.a2ndhandapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.a2ndhandapp.Interfaces.ProductItemCallback;
import com.example.a2ndhandapp.Models.Product;
import com.example.a2ndhandapp.R;
import com.example.a2ndhandapp.Utils.CurrentUser;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ProductViewHolder> {

    private Context context;
    private ArrayList<Product> products;
    private ProductItemCallback productCallback;

    public ProductItemAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    public ProductItemAdapter setProductItemCallback(ProductItemCallback productCallback) {
        this.productCallback = productCallback;
        return this;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        ProductViewHolder productViewHolder = new ProductViewHolder(view);
        return productViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductItemAdapter.ProductViewHolder holder, int position) {
        Product product = getItem(position);

        holder.product_LBL_name.setText(product.getName() + "");
        holder.product_LBL_category.setText(product.getCategory() + "");
        holder.product_LBL_price.setText(product.getPrice() + " NIS₪");

        if (CurrentUser.getInstance() != null) {
            if (CurrentUser.getInstance().getUser().getMyFavorites() != null) {
                if (CurrentUser.getInstance().getUser().isFavorite(product)) {
                    holder.product_IMG_favorite.setImageResource(R.drawable.red_heart);
                } else {
                    holder.product_IMG_favorite.setImageResource(R.drawable.white_heart);
                }

            }
        }
        Glide.
                with(ProductItemAdapter.this.context)
                .load((String) null) // TODO:  change to currentProduct.getImages().get(0)
                .placeholder(R.drawable.temporary_img)
                .into(holder.product_IMG_image);
    }

    @Override
    public int getItemCount() {
        return products == null ? 0 : products.size();
    }

    private Product getItem(int position) {
        return products.get(position);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView product_IMG_image;
        private AppCompatImageView product_IMG_favorite;
        private MaterialTextView product_LBL_name;
        private MaterialTextView product_LBL_category;
        private MaterialTextView product_LBL_price;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            product_IMG_image = itemView.findViewById(R.id.product_IMG_image);
            product_IMG_favorite = itemView.findViewById(R.id.product_IMG_favorite);
            product_LBL_name = itemView.findViewById(R.id.product_LBL_name);
            product_LBL_category = itemView.findViewById(R.id.product_LBL_category);
            product_LBL_price = itemView.findViewById(R.id.product_LBL_price);

            itemView.setOnClickListener(v -> {
                Toast.makeText(context, "Clicked on " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                productCallback.itemClicked(getItem(getAdapterPosition()),
                        getAdapterPosition());
            });
            product_IMG_favorite.setOnClickListener(v -> {
                Toast.makeText(context, "Favorite", Toast.LENGTH_SHORT).show();
                productCallback.favoriteClicked(getItem(getAdapterPosition()), getAdapterPosition());
            });
        }
    }
}
