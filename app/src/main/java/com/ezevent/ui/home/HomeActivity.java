package com.ezevent.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ezevent.R;
import com.ezevent.controller.Constants;
import com.ezevent.ui.creategame.CreateGameActivity;
import com.ezevent.ui.creategame.GameDescription;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    RecyclerView recyclerViewGameList;
    GameListAdapter gameListAdapter;
    LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerViewGameList=findViewById(R.id.gameList);
        layoutManager=new LinearLayoutManager(this);
        recyclerViewGameList.setLayoutManager(layoutManager);


    }

    public void createGame(View view) {
        Log.e("Button is Called ","test ");
        startActivity(new Intent(this, CreateGameActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child(Constants.GAME_NODE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<GameDescription> gameDescriptionList= new ArrayList<>();
                for (DataSnapshot data:dataSnapshot.getChildren()
                     ) {
                    gameDescriptionList.add(data.getValue(GameDescription.class));
                }

                setRecyclerView(gameDescriptionList);
                Log.e("Game List Updated "," "+gameDescriptionList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e("Something  ","Wrong while retring "+databaseError.getMessage());
            }
        });
    }

    private void setRecyclerView(ArrayList<GameDescription> gameDescriptionList) {
        gameListAdapter=new GameListAdapter(this,gameDescriptionList);
        recyclerViewGameList.setAdapter(gameListAdapter);

    }
}
