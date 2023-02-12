package com.example.a2ndhandapp.Controller.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.a2ndhandapp.Controller.Fragments.AddFragment;
import com.example.a2ndhandapp.Controller.Fragments.FavoritesFragment;
import com.example.a2ndhandapp.Controller.Fragments.HomeFragment;
import com.example.a2ndhandapp.Controller.Fragments.MyFragment;
import com.example.a2ndhandapp.Controller.Fragments.SearchFragment;
import com.example.a2ndhandapp.Controller.Fragments.SingleProductFragment;
import com.example.a2ndhandapp.Interfaces.GetProductCallback;
import com.example.a2ndhandapp.Models.Product;
import com.example.a2ndhandapp.Models.User;
import com.example.a2ndhandapp.R;
import com.example.a2ndhandapp.Utils.CurrentUser;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer; // DrawerLayout is a ViewGroup that allows for interactive "drawer" views to be pulled out from one or both vertical edges of the window.
    private Toolbar toolbar; // Toolbar is a ViewGroup that displays a horizontal bar containing actions and views such as title and navigation buttons.

    private SingleProductFragment singleProductFragment;
    private HomeFragment homeFragment;
    private MyFragment myFragment;
    private FavoritesFragment favoritesFragment;
    private SearchFragment searchFragment;
    private AddFragment addFragment;

    private NavigationView navigationView;
    private Menu menu;


    GetProductCallback getProductCallback = new GetProductCallback() {
        @Override
        public void getProduct(Product product) {
            // TODO -  send to "ProductFragment" the specific product
//            Toast.makeText(MainActivity.this, "Description:  " + product.getDescription(), Toast.LENGTH_SHORT).show();
            if (singleProductFragment != null) {
                singleProductFragment.setCurrentProduct(product);
                getSupportActionBar().setTitle(product.getName()); // Set the title of the toolbar
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, singleProductFragment).commit(); // Set the slideshow fragment
            }
        }

        @Override
        public void backToHome() {
            startActivity(new Intent(MainActivity.this, SplashActivity.class));
            finish();
//            getSupportActionBar().setTitle("Home"); // Set the title of the toolbar
//            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, homeFragment).commit(); // Set the slideshow fragment
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set direction on all devices from LEFT to RIGHT
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        toolbar = findViewById(R.id.main_toolbar); // Get the toolbar
        setSupportActionBar(toolbar); // Set the toolbar as the action bar

        drawer = findViewById(R.id.drawer_layout); // Get the drawer layout

        NavigationView navigationView = findViewById(R.id.main_nav_view); // Get the navigation view
        navigationView.setNavigationItemSelectedListener(this); // Set the navigation view listener
        menu = navigationView.getMenu();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close); // Create a toggle for the drawer
        drawer.addDrawerListener(toggle); // Add the toggle to the drawer
        toggle.syncState(); // Sync the state of the drawer

        createFragments();

        if (savedInstanceState == null) {
//            homeFragment = new HomeFragment();
//            homeFragment.setGetProductCallback(getProductCallback);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, homeFragment).commit(); // Set the home fragment as the default fragment
            getSupportActionBar().setTitle("Home"); // Set the title of the toolbar
            navigationView.setCheckedItem(R.id.nav_home); // Set the home item as the default item
        }

//        if (CurrentUser.getInstance().getUser() == null) {
//            menu.findItem(R.id.nav_logOut).setTitle("Log In");
//            menu.findItem(R.id.nav_add).setVisible(false);
//            menu.findItem(R.id.nav_favorites).setVisible(false);
//            menu.findItem(R.id.nav_my).setVisible(false);
//        }
//        saveToDB();
    }

//    private void saveToDB() {
//        ArrayList<String> categories = new ArrayList<>();
//        categories.add("Electronics");
//        categories.add("Clothes");
//        categories.add("Shoes");
//        categories.add("Accessories");
//        categories.add("Home");
//        categories.add("Toys");
//        categories.add("Books");
//        categories.add("Sports");
//        Collections.sort(categories);
//        categories.add("Other");
//
//        FirebaseDatabase db = FirebaseDatabase.getInstance();
//
//        DatabaseReference userRef = db.getReference("Categories");
//        userRef.get();
//        userRef.setValue(categories);
//    }

    /**
     * Create all the fragments
     */
    private void createFragments() {
        singleProductFragment = new SingleProductFragment();
        homeFragment = new HomeFragment();
        myFragment = new MyFragment();
        favoritesFragment = new FavoritesFragment();
        searchFragment = new SearchFragment();
        addFragment = new AddFragment();

        homeFragment.setGetProductCallback(getProductCallback);
        myFragment.setGetProductCallback(getProductCallback);
        favoritesFragment.setGetProductCallback(getProductCallback);
        addFragment.setGetProductCallback(getProductCallback);
    }

    /**
     * This method is called when we click on one of the items in the navigation view
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, homeFragment).commit(); // Set the home fragment
                getSupportActionBar().setTitle("Home"); // Set the title of the toolbar
                break;
            case R.id.nav_favorites:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, favoritesFragment).commit(); // Set the gallery fragment
                getSupportActionBar().setTitle("Favorites"); // Set the title of the toolbar
                break;
            case R.id.nav_Search:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, searchFragment).commit(); // Set the slideshow fragment
                getSupportActionBar().setTitle("Search"); // Set the title of the toolbar
                break;
            case R.id.nav_my:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, myFragment).commit(); // Set the slideshow fragment
                getSupportActionBar().setTitle("My"); // Set the title of the toolbar
                break;
            case R.id.nav_add:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, addFragment).commit(); // Set the slideshow fragment
                getSupportActionBar().setTitle("Add"); // Set the title of the toolbar
                break;
            case R.id.nav_logOut:
//                NavigationView navigationView = findViewById(R.id.main_nav_view); // Get the navigation
//                Menu menu = navigationView.getMenu();
//                menu.findItem(R.id.nav_logOut).setTitle("Log In");
//                menu.findItem(R.id.nav_add).setVisible(false);
                Toast.makeText(this, "Log Out", Toast.LENGTH_SHORT).show(); // Show a toast message
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * This method is called when the back button is pressed
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) { // If the drawer is open
            drawer.closeDrawer(GravityCompat.START); // Close the drawer
        } else {
            super.onBackPressed(); // Otherwise, do the default behavior
        }
    }
}