package com.ezevent.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ezevent.R;
import com.ezevent.ui.creategame.CreateGameActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void createGame(View view) {
        Log.e("Button is Called ","test ");
        startActivity(new Intent(this, CreateGameActivity.class));
    }
}
