package com.ezevent;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class EvEvents extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
