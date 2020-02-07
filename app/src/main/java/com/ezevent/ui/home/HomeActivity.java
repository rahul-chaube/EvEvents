package com.ezevent.ui.home;

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
import com.ezevent.controller.PrefManager;
import com.ezevent.ui.chatroom.CharRoomAcitvity;
import com.ezevent.ui.chatroom.GameListAdapter;
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
    RecyclerView recyclerViewchat;
    LinearLayoutManager layoutManager;
    ChatRoomAdapter gameListAdapter;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerViewchat = findViewById(R.id.recyclerGroupChat);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewchat.setLayoutManager(layoutManager);
        prefManager = new PrefManager(this);
    }

    public void createGame(View view) {
        Log.e("Button is Called ", "test ");
        startActivity(new Intent(this, CharRoomAcitvity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Getting data ...");
        dialog.show();
        dialog.setCancelable(false);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.USER_NODE)
                .child(prefManager.getUserId()).child(Constants.My_GAMELSIT);
        databaseReference.keepSynced(true);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<GameDescription> gameDescriptionList = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()
                ) {
                    gameDescriptionList.add(data.getValue(GameDescription.class));
                }

                setRecyclerView(gameDescriptionList);
                dialog.dismiss();
                Log.e("Game List Updated ", " " + gameDescriptionList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Something  ", "Wrong while retring " + databaseError.getMessage());
                dialog.dismiss();
                databaseError.toException().printStackTrace();
            }
        });
    }

    private void setRecyclerView(ArrayList<GameDescription> gameDescriptionList) {
        gameListAdapter = new ChatRoomAdapter(this, gameDescriptionList);
        recyclerViewchat.setAdapter(gameListAdapter);

    }


}
