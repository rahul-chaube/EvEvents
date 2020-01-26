package com.ezevent.controller;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public PrefManager(Context context) {
        this.context = context;
        sharedPreferences=context.getSharedPreferences(Constants.PREF_NAME,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }
}
