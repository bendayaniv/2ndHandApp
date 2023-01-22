package com.example.a2ndhandapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2ndhandapp.Interfaces.ProductCallback;
import com.example.a2ndhandapp.Models.Product;
import com.example.a2ndhandapp.Models.User;
import com.example.a2ndhandapp.R;
import com.example.a2ndhandapp.Utils.ImageLoader;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private ArrayList<Product> products;
    private ProductCallback productCallback;
    private User currentUser;

    public ProductAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    public ProductAdapter setProductCallback(ProductCallback productCallback) {
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

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        Log.d("onBindViewHolder Position: ", "" + position);
        Product product = getItem(position);

        holder.product_LBL_name.setText(product.getName() + "");
        holder.product_LBL_category.setText(product.getCategory() + "");
        holder.product_LBL_price.setText(product.getPrice() + " NIS");
        if (currentUser != null) {
            if (currentUser.getFavorites().contains(product)) {
                holder.product_IMG_favorite.setImageResource(R.drawable.red_heart);
            } else {
                holder.product_IMG_favorite.setImageResource(R.drawable.white_heart);
            }
        }
//        if (currentUser != null && /*product.isFavorite(currentUser)*/ currentUser.getFavorites().contains(product)) {
//            holder.product_IMG_favorite.setImageResource(R.drawable.red_heart);
//        } else if (currentUser != null && !currentUser.getFavorites().contains(product)) {
//            holder.product_IMG_favorite.setImageResource(R.drawable.white_heart);
//        }
//        ImageLoader.getInstance().load(product.getImages().get(0), holder.product_IMG_image);
//        holder.product_IMG_image.setImageResource(Integer.parseInt(product.getImages().get(0)));
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

//        private AppCompatImageView singleProduct_IMG_image;
//        private MaterialTextView singleProduct_LBL_name;
//        private MaterialTextView singleProduct_LBL_category;
//        private MaterialTextView singleProduct_LBL_price;
//        private MaterialTextView singleProduct_LBL_description;
//        private EditText singleProduct_EDT_description;
//        private MaterialTextView singleProduct_LBL_sellerDetails;
//        private MaterialTextView singleProduct_LBL_date;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            product_IMG_image = itemView.findViewById(R.id.product_IMG_image);
            product_IMG_favorite = itemView.findViewById(R.id.product_IMG_favorite);
            product_LBL_name = itemView.findViewById(R.id.product_LBL_name);
            product_LBL_category = itemView.findViewById(R.id.product_LBL_category);
            product_LBL_price = itemView.findViewById(R.id.product_LBL_price);

//            singleProduct_IMG_image = itemView.findViewById(R.id.singleProduct_IMG_image);
//            singleProduct_LBL_name = itemView.findViewById(R.id.singleProduct_LBL_name);
//            singleProduct_LBL_category = itemView.findViewById(R.id.singleProduct_LBL_category);
//            singleProduct_LBL_price = itemView.findViewById(R.id.singleProduct_LBL_price);
//            singleProduct_LBL_description = itemView.findViewById(R.id.singleProduct_LBL_description);
//            singleProduct_EDT_description = itemView.findViewById(R.id.singleProduct_EDT_description);
//            singleProduct_LBL_sellerDetails = itemView.findViewById(R.id.singleProduct_LBL_sellerDetails);
//            singleProduct_LBL_date = itemView.findViewById(R.id.singleProduct_LBL_date);

            itemView.setOnClickListener(v -> productCallback.itemClicked(getItem(getAdapterPosition()),
                    getAdapterPosition()));
            product_IMG_favorite.setOnClickListener(v -> {
                productCallback.favoriteClicked(getItem(getAdapterPosition()), getAdapterPosition());
            });
        }
    }
}
