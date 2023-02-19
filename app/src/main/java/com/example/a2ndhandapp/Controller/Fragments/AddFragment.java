package com.example.a2ndhandapp.Controller.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import com.example.a2ndhandapp.Adapters.CategoryAutoCompleteTextAdapter;
import com.example.a2ndhandapp.Interfaces.GoHomeCallback;
import com.example.a2ndhandapp.Models.Product;
import com.example.a2ndhandapp.R;
import com.example.a2ndhandapp.Utils.CurrentUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class AddFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private AppCompatEditText add_EDT_productName;
    private AppCompatEditText add_EDT_productPrice;
    private AppCompatEditText add_EDT_productDescription;
    private TextInputLayout add_TIL_productCategory;
    private MaterialButton add_BTN_addProdImg;
    private ImageView add_IMG_productImage;
    private MaterialButton add_BTN_addProduct;
    private ArrayList<String> categories = new ArrayList<>();
    private AutoCompleteTextView auto_complete_text;
    private CategoryAutoCompleteTextAdapter categoryAutoCompleteTextAdapter;
    private ProgressBar mProgressBar;
    private Uri mImageUri;
    private StorageTask mUploadTask;


    private GoHomeCallback goHomeCallback;

    public void setGoHomeCallback(GoHomeCallback goHomeCallback) {
        this.goHomeCallback = goHomeCallback;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference("Categories");
        getCategoriesFromDB(categoriesRef);

        findViews(view);

        initView();

        return view;
    }

    private void productImageButton() {
        add_BTN_addProdImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }

    /**
     * Open file chooser to choose an image
     */
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /**
     * This method is called when the user selects an image, and put the image in the ImageView
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            add_IMG_productImage.setImageURI(data.getData()); // set the image to the image view
        }
    }


    /**
     * Get the file extension from the Uri (image)
     *
     * @param uri
     * @return the file extension
     */
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void addButtonFunction() {
        add_BTN_addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String productName = add_EDT_productName.getText().toString();
                String productPrice = add_EDT_productPrice.getText().toString();
                String productCategory = add_TIL_productCategory.getEditText().getText().toString();

                // To make sure that the user can't upload more than one image at the same time
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(getContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
                } else if (productName.isEmpty() || productPrice.isEmpty() || productCategory.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else if (!productPrice.chars().allMatch(Character::isDigit)) {
                    Toast.makeText(getContext(), "Please enter a valid price", Toast.LENGTH_SHORT).show();
                } else {
                    uploadNewProduct(productName, productPrice, productCategory);
                }
            }
        });
    }

    private void uploadNewProduct(String productName, String productPrice, String productCategory) {
        if (mImageUri != null) {
            String imageId = System.currentTimeMillis() + "" + CurrentUser.getInstance().getUser().getName().trim()
                    + "." + getFileExtension(mImageUri);
            StorageReference fileReference = FirebaseStorage.getInstance().getReference("uploads").child(imageId);

            mUploadTask = fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_LONG).show();

                            uploadToRealTime(productName, productPrice, productCategory, imageId);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });


        } else {
            Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadToRealTime(String productName, String productPrice, String productCategory, String imageId) {
        String productDescription = add_EDT_productDescription.getText().toString();

        if (productDescription.equals("Add the product description here")) {
            productDescription = "";
        }
        String newId = String.valueOf(Integer.valueOf(CurrentUser.getInstance().getLastProductId()));
        Product newProduct = new Product(newId, productName, productDescription, productPrice, productCategory,
                CurrentUser.getInstance().getUser().getName(), CurrentUser.getInstance().getUser().getEmail(), null, imageId);

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("Products");
        productsRef.getRef().child(String.valueOf(newProduct.getId())).setValue(newProduct);

        CurrentUser.getInstance().getUser().addProduct(newProduct);

        newId = String.valueOf((Integer.parseInt(newId)) + 1);
        CurrentUser.getInstance().setLastProductId(newId);
        goHomeCallback.goHome();
    }

    private void initView() {
        productImageButton();

        categoryAutoCompleteTextAdapter = new CategoryAutoCompleteTextAdapter(getContext(), categories);

        auto_complete_text.setAdapter(categoryAutoCompleteTextAdapter);

        addButtonFunction();
    }

    /**
     * This method is called when the fragment is created, and find the views in the fragment
     *
     * @param view == the view of the fragment
     */
    private void findViews(View view) {
        add_EDT_productName = view.findViewById(R.id.add_EDT_productName);
        add_EDT_productPrice = view.findViewById(R.id.add_EDT_productPrice);
        add_EDT_productDescription = view.findViewById(R.id.add_EDT_productDescription);
        add_TIL_productCategory = view.findViewById(R.id.add_TIL_productCategory);
        add_IMG_productImage = view.findViewById(R.id.add_IMG_productImage);
        add_BTN_addProdImg = view.findViewById(R.id.add_BTN_addProdImg);
        add_BTN_addProduct = view.findViewById(R.id.add_BTN_addProduct);

        auto_complete_text = view.findViewById(R.id.auto_complete_text);

        mProgressBar = view.findViewById(R.id.add_progress_bar);
//        mProgressBar.getProgressDrawable().setColorFilter(
//                Color.RED, PorterDuff.Mode.SRC_IN);
        mProgressBar.setBackgroundColor(Color.GRAY);
        mProgressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
//        mProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bg));
    }

    public void getCategoriesFromDB(DatabaseReference reference) {
        reference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    categories.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (!ds.getValue().toString().equals("All"))
                            categories.add(ds.getValue().toString());
                    }
                }
            }
        });
    }
}
