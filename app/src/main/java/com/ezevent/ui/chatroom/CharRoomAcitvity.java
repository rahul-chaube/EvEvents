package com.ezevent.ui.chatroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
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

public class CharRoomAcitvity extends AppCompatActivity {
    RecyclerView recyclerViewGameList;
    GameListAdapter gameListAdapter;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_char_room_acitvity);
        recyclerViewGameList=findViewById(R.id.gameList);
        layoutManager=new LinearLayoutManager(this);
        recyclerViewGameList.setLayoutManager(layoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Getting data ...");
        dialog.show();
        dialog.setCancelable(false);
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
                dialog.dismiss();
                Log.e("Game List Updated "," "+gameDescriptionList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Something  ","Wrong while retring "+databaseError.getMessage());
                dialog.dismiss();
                databaseError.toException().printStackTrace();
            }
        });
    }

    private void setRecyclerView(ArrayList<GameDescription> gameDescriptionList) {
        gameListAdapter=new GameListAdapter(this,gameDescriptionList);
        recyclerViewGameList.setAdapter(gameListAdapter);

    }

    public void createNewUser(View view) {
        startActivity(new Intent(this, CreateGameActivity.class));
    }
}
