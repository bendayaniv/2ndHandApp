package com.example.a2ndhandapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2ndhandapp.Interfaces.ProductItemCallback;
import com.example.a2ndhandapp.Models.Product;
import com.example.a2ndhandapp.R;
import com.example.a2ndhandapp.Utils.CurrentUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This adapter is for the RV of the products in both Home and Favorites pages
 */
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
        holder.product_LBL_price.setText(product.getPrice() + "₪");

        if (CurrentUser.getInstance() != null) {
            if (CurrentUser.getInstance().getUser().getMyFavorites() != null) {
                if (CurrentUser.getInstance().getUser().isFavorite(product)) {
                    holder.product_IMG_favorite.setImageResource(R.drawable.red_heart);
                } else {
                    holder.product_IMG_favorite.setImageResource(R.drawable.white_heart);
                }
            }
            if (CurrentUser.getInstance().getUser().getMyProducts() != null) {
                if (CurrentUser.getInstance().getUser().isMyProduct(product)) {
                    holder.product_IMG_favorite.setVisibility(View.GONE);
                }
            }
        }

        // The uploading image part from Storage Firebase
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("uploads/" + product.getImageId());

        try {
            File localFile = File.createTempFile("tempFile", ".jpg");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    holder.product_IMG_image.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    holder.product_IMG_image.setImageResource(R.drawable.temporary_img);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return products == null ? 0 : products.size();
    }

    private Product getItem(int position) {
        return products.get(position);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView product_IMG_image;
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
                productCallback.itemClicked(products.get(getAdapterPosition()),
                        getAdapterPosition());
            });
            product_IMG_favorite.setOnClickListener(v -> {
                productCallback.favoriteClicked(getItem(getAdapterPosition()), getAdapterPosition());
            });
        }
    }
}
