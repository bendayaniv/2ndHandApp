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
import android.widget.TextView;

import com.example.a2ndhandapp.Controller.Fragments.AddFragment;
import com.example.a2ndhandapp.Controller.Fragments.FavoritesFragment;
import com.example.a2ndhandapp.Controller.Fragments.HomeFragment;
import com.example.a2ndhandapp.Controller.Fragments.LogInOutFragment;
import com.example.a2ndhandapp.Controller.Fragments.MyFragment;
import com.example.a2ndhandapp.Controller.Fragments.SearchFragment;
import com.example.a2ndhandapp.Controller.Fragments.SingleProductFragment;
import com.example.a2ndhandapp.Interfaces.GoHomeCallback;
import com.example.a2ndhandapp.Interfaces.GetCategoryCallback;
import com.example.a2ndhandapp.Interfaces.GetProductCallback;
import com.example.a2ndhandapp.Models.Product;
import com.example.a2ndhandapp.R;
import com.example.a2ndhandapp.Utils.CurrentUser;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer; // DrawerLayout is a ViewGroup that allows for interactive "drawer" views to be pulled out from one or both vertical edges of the window.
    private Toolbar toolbar; // Toolbar is a ViewGroup that displays a horizontal bar containing actions and views such as title and navigation buttons.
    private SingleProductFragment singleProductFragment;
    private HomeFragment homeFragment;
    private MyFragment myFragment;
    private FavoritesFragment favoritesFragment;
    private SearchFragment searchFragment;
    private AddFragment addFragment;
    private LogInOutFragment logInOutFragment;
    private Menu menu;
    private NavigationView navigationView;
    private TextView textView_userName;
    private TextView textView_userEmail;
    private ActionBarDrawerToggle toggle;
    /**
     * This callback is to inform the activity that the user has clicked on a product
     * to see its details
     *
     * @param product - the product that was clicked
     */
    GetProductCallback getProductCallback = new GetProductCallback() {
        @Override
        public void getProduct(Product product) {
            // TODO - send to "ProductFragment" the specific product
            if (singleProductFragment != null) {
                singleProductFragment.setCurrentProduct(product);
                getSupportActionBar().setTitle(product.getName()); // Set the title of the toolbar
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, singleProductFragment).commit(); // Set the slideshow fragment
            }
        }
    };

    /**
     * This callback is to inform the activity that the user has clicked on a category
     * to see specific types of products
     *
     * @param category - the category that was clicked
     * @param position - the position of the category in the list
     */
    GetCategoryCallback categoryCallback = new GetCategoryCallback() {

        @Override
        public void categoryClicked(String category, int position) {
            CurrentUser.getInstance().setCurrentCategory(category);
            navigationView.setCheckedItem(R.id.nav_home);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, homeFragment).commit(); // Set the home fragment
            getSupportActionBar().setTitle(CurrentUser.getInstance().getCurrentCategory()); // Set the title of the toolbar
        }
    };

    /**
     * This callback is to inform the activity that the user has logged/signed in
     * and to start the splash activity
     * It is for after the user has logged/signed in or deleted onw of his products
     */
    GoHomeCallback goHomeCallback = new GoHomeCallback() {
        @Override
        public void goHome() {
            startActivity(new Intent(MainActivity.this, SplashActivity.class));
            finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set direction on all devices from LEFT to RIGHT
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        findView();

        initView();

        createFragments();

        // Check if there is a user logged in and set the menu items accordingly
        // Else, set the default fragment to be the "LogInOutFragment"
        if (savedInstanceState == null && FirebaseAuth.getInstance().getCurrentUser() != null) {
            menu.findItem(R.id.nav_home).setVisible(true);
            menu.findItem(R.id.nav_favorites).setVisible(true);
            menu.findItem(R.id.nav_Search).setVisible(true);
            menu.findItem(R.id.nav_my).setVisible(true);
            menu.findItem(R.id.nav_add).setVisible(true);
            menu.findItem(R.id.nav_logOut).setTitle("Log Out");
        }
        if (CurrentUser.getInstance().getUser() == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container
                    , logInOutFragment).commit(); // Set the login fragment
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, homeFragment).commit(); // Set the home fragment as the default fragment
            getSupportActionBar().setTitle(CurrentUser.getInstance().getCurrentCategory()); // Set the title of the toolbar
            navigationView.setCheckedItem(R.id.nav_home); // Set the home item as the default item
        }
        CurrentUser.getInstance().setCurrentCategory("All");
    }

    private void initView() {
        setSupportActionBar(toolbar); // Set the toolbar as the action bar

        navigationView.setNavigationItemSelectedListener(this); // Set the navigation view listener
        menu = navigationView.getMenu();

        if (CurrentUser.getInstance().getUser() != null) {
            textView_userName.setText(CurrentUser.getInstance().getUser().getName());
            textView_userEmail.setText(CurrentUser.getInstance().getUser().getEmail());
        }

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close); // Create a toggle for the drawer
        drawer.addDrawerListener(toggle); // Add the toggle to the drawer
        toggle.syncState(); // Sync the state of the drawer
    }

    private void findView() {
        toolbar = findViewById(R.id.main_toolbar); // Get the toolbar
        drawer = findViewById(R.id.drawer_layout); // Get the drawer layout

        navigationView = findViewById(R.id.main_nav_view); // Get the navigation view

        textView_userName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textView_userName);
        textView_userEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textView_userEmail);
    }


    /**
     * Create all the fragments
     */
    private void createFragments() {
        singleProductFragment = new SingleProductFragment();
        singleProductFragment.setGoHomeCallback(goHomeCallback);

        homeFragment = new HomeFragment();
        homeFragment.setGetProductCallback(getProductCallback);

        favoritesFragment = new FavoritesFragment();
        favoritesFragment.setGetProductCallback(getProductCallback);

        searchFragment = new SearchFragment();
        searchFragment.setCategoryCallback(categoryCallback);

        myFragment = new MyFragment();
        myFragment.setGetProductCallback(getProductCallback);

        addFragment = new AddFragment();
        addFragment.setGoHomeCallback(goHomeCallback);

        logInOutFragment = new LogInOutFragment();
        logInOutFragment.setGoHomeCallback(goHomeCallback);
    }

    /**
     * This method is called when we click on one of the items in the navigation view
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                navigationView.setCheckedItem(R.id.nav_home);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, homeFragment).commit(); // Set the home fragment
                getSupportActionBar().setTitle(CurrentUser.getInstance().getCurrentCategory()); // Set the title of the toolbar
                break;
            case R.id.nav_favorites:
                navigationView.setCheckedItem(R.id.nav_favorites);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, favoritesFragment).commit(); // Set the gallery fragment
                getSupportActionBar().setTitle("Favorites"); // Set the title of the toolbar
                break;
            case R.id.nav_Search:
                navigationView.setCheckedItem(R.id.nav_Search);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, searchFragment).commit(); // Set the slideshow fragment
                getSupportActionBar().setTitle("Search"); // Set the title of the toolbar
                break;
            case R.id.nav_my:
                navigationView.setCheckedItem(R.id.nav_my);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, myFragment).commit(); // Set the slideshow fragment
                getSupportActionBar().setTitle("My"); // Set the title of the toolbar
                break;
            case R.id.nav_add:
//                addFragment.resetAutoCompleteText();
                navigationView.setCheckedItem(R.id.nav_add);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, addFragment).commit(); // Set the slideshow fragment
                getSupportActionBar().setTitle("Add"); // Set the title of the toolbar
                break;
            case R.id.nav_logOut:
                CurrentUser.getInstance().removeUser();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, SplashActivity.class));
                finish();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}