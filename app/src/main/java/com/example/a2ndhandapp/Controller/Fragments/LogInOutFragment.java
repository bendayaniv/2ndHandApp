package com.example.a2ndhandapp.Controller.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.a2ndhandapp.Interfaces.AfterLogInCallback;
import com.example.a2ndhandapp.Models.Product;
import com.example.a2ndhandapp.Models.User;
import com.example.a2ndhandapp.R;
import com.example.a2ndhandapp.Utils.CurrentUser;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogInOutFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference mDatabase;
    private AfterLogInCallback afterLogInCallback;
    private ArrayList<Product> products = new ArrayList<>();

    public void setAfterLogInCallback(AfterLogInCallback afterLogInCallback) {
        this.afterLogInCallback = afterLogInCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        mDatabase = db.getReference();


        if (user == null) {
            login();
        } else {
            alreadyIn();
        }

        return view;
    }


    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        Log.d("TAGTAGTAGTAGTAGTAG", "77777777777777777777");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * This method will be invoked any time the data on the database changes.
             * Additionally, it will be invoked as soon as we connect the listener, so that we can
             * get an initial snapshot of the data on the database.
             * @param snapshot
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User newUser = new User()
                        .setName(mAuth.getCurrentUser().getDisplayName())
                        .setEmail(mAuth.getCurrentUser().getEmail())
                        .setUid(mAuth.getCurrentUser().getUid());
                if (!snapshot.child("Users").child(mAuth.getCurrentUser().getUid()).exists()) {
                    mDatabase.child("Users").child(newUser.getUid()).setValue(newUser);

                } else {
                    alreadyIn();
                }
                Log.d("TAGTAGTAGTAGTAGTAG", "111111111111111111111");
                CurrentUser.getInstance().setUser(newUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (afterLogInCallback != null) {
            afterLogInCallback.afterLogIn();
        }

    }


    private void login() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build());
        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
    }

    /**
     * In case that there is already user that logged in
     * and/or the user is already in the DB
     * <p>
     * //@param mDatabase
     */
    private void alreadyIn(/*DatabaseReference mDatabase*/) {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

                                                     @Override
                                                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                         User newUser = new User()
                                                                 .setName(mAuth.getCurrentUser().getDisplayName())
                                                                 .setEmail(mAuth.getCurrentUser().getEmail())
                                                                 .setUid(mAuth.getCurrentUser().getUid());

                                                         if (snapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("myFavorites").exists()) {
                                                             ArrayList<Product> favorites = new ArrayList<>();
                                                             for (DataSnapshot ds : snapshot.child("Users").child(mAuth
                                                                     .getCurrentUser().getUid()).child("myFavorites").getChildren()) {
                                                                 Product product = ds.getValue(Product.class);
                                                                 favorites.add(product);
                                                             }
                                                             newUser.setMyFavorites(favorites);
                                                         }
                                                         if (snapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("MyProducts").exists()) {
                                                             ArrayList<Product> myProducts = new ArrayList<>();
                                                             for (DataSnapshot ds : snapshot.child("Users").child(mAuth
                                                                     .getCurrentUser().getUid()).child("MyProducts").getChildren()) {
                                                                 Product product = ds.getValue(Product.class);
                                                                 myProducts.add(product);
                                                             }
                                                             newUser.setMyProducts(myProducts);
                                                         }
                                                         CurrentUser.getInstance().setUser(newUser);
                                                     }

                                                     @Override
                                                     public void onCancelled(@NonNull DatabaseError error) {

                                                     }


                                                 }
        );
        if (afterLogInCallback != null) {
            afterLogInCallback.afterLogIn();
        }
    }
}
