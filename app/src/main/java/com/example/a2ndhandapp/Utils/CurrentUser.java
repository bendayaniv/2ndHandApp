package com.example.a2ndhandapp.Utils;

import android.content.Context;

import com.example.a2ndhandapp.Models.User;

public class CurrentUser {

    private static CurrentUser instance = null;
    private User user;

    private CurrentUser() {
    }

    public static void initCurrentUser(Context context) {
        if (instance == null) {
            instance = new CurrentUser();
        }
    }

    public static CurrentUser getInstance() {
        return instance;
    }

    public User getUser() {
        return user;
    }

    public CurrentUser setUser(User user) {
        this.user = user;
        return this;
    }

    public CurrentUser removeUser() {
        this.user = null;
        return this;
    }
}
