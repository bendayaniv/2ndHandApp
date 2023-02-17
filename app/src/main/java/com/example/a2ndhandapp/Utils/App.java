package com.example.a2ndhandapp.Utils;

import android.app.Application;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CurrentUser.initCurrentUser(this);
    }
}
