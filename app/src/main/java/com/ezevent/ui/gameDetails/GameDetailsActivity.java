package com.ezevent.ui.gameDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ezevent.R;
import com.ezevent.controller.Constants;
import com.ezevent.controller.PrefManager;
import com.ezevent.ui.chatroom.ChatMessage;
import com.ezevent.ui.creategame.CreateGameActivity;
import com.ezevent.ui.creategame.GameCreator;
import com.ezevent.ui.creategame.GameDescription;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class GameDetailsActivity extends AppCompatActivity {
    TextView textViewgameName, textViewgameDescription, textViewgamePrize, textViewgameType, textViewgroupType, textViewgameCreator,
            textViewgameNumberOfPlayer, textViewgameNumberOfPlayerRemain;
    Button buttonJoinGame;
    RecyclerView recyclerViewPlayerList;

    LinearLayoutManager layoutManager;

    GameDescription gameDescription;
    PlayerListAdapter playerListAdapter;

    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);
        prefManager = new PrefManager(this);
        textViewgameName = findViewById(R.id.gameName);
        textViewgameDescription = findViewById(R.id.gameDescription);
        textViewgamePrize = findViewById(R.id.gamePrize);
        textViewgroupType = findViewById(R.id.gameType);
        textViewgameType = findViewById(R.id.groupType);
        textViewgameCreator = findViewById(R.id.gameCreator);
        textViewgameNumberOfPlayer = findViewById(R.id.gameNumberOfPlayer);
        textViewgameNumberOfPlayerRemain = findViewById(R.id.gameNumberOfPlayerRemain);
        buttonJoinGame = findViewById(R.id.joinGame);
        recyclerViewPlayerList = findViewById(R.id.recyclerViewPlayerList);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPlayerList.setLayoutManager(layoutManager);

        gameDescription = (GameDescription) getIntent().getSerializableExtra(Constants.GAME_DETAILS);
        if (gameDescription != null) {
            setUI(gameDescription);

            if (gameDescription.getNumberOfPlayer() == gameDescription.getGamerList().size()) {
                buttonJoinGame.setBackgroundColor(getResources().getColor(R.color.red));
                buttonJoinGame.setTextColor(getResources().getColor(R.color.white));
            }
        }

        buttonJoinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameDescription.getNumberOfPlayer() == gameDescription.getGamerList().size()) {
                    Toast.makeText(GameDetailsActivity.this, "No Room , Sorry ! Game Room is filled with max number of player", Toast.LENGTH_SHORT).show();
                } else {
                    if (gameDescription.isPrivate()) {
                        createCustomDialog(gameDescription);
                    } else
                        joinPlayer(gameDescription);
                }
            }
        });
    }

    private void createCustomDialog(final GameDescription gameDescription) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        builder.setCancelable(false);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        View view = inflater.inflate(R.layout.custom_password, null);

        final EditText editTextUserName = view.findViewById(R.id.username);
        final EditText editTextPassword = view.findViewById(R.id.password);
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...

                        if (editTextUserName.getText().toString().equalsIgnoreCase(gameDescription.getGameCredentials().getGameName()) && editTextPassword.getText().toString().equals(gameDescription.getGameCredentials().getPassword())) {
                            joinPlayer(gameDescription);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(GameDetailsActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    private void joinPlayer(final GameDescription gameDescription) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ... ");
        progressDialog.show();
        progressDialog.setCancelable(false);
        final GameCreator gameCreator = new GameCreator();
        gameCreator.setUserId(prefManager.getUserId());
        gameCreator.setMobileNumber(prefManager.getUserMobile());
        gameCreator.setUserName(prefManager.getUserName());

        final List<GameCreator> gameCreators = gameDescription.getGamerList();
        gameCreators.add(gameCreator);

        Log.e("Game id ", "  " + gameDescription.getGameId());
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.GAME_NODE);
        databaseReference.child(gameDescription.getGameId()).child(Constants.GAMER_LIST).setValue(gameCreators).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    GameDetailsActivity.this.gameDescription.setGamerList(gameCreators);
                    setUI(gameDescription);

                    addToUserNode(gameDescription);
                } else {
                    Toast.makeText(GameDetailsActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

        });


    }

    private void addToUserNode(final GameDescription gameDescription) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait ... ");
        progressDialog.show();
        progressDialog.setCancelable(false);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.USER_NODE);
        databaseReference.child(prefManager.getUserId()).child(Constants.My_GAMELSIT).child(gameDescription.getGameId()).setValue(gameDescription).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                addToChatNode(gameDescription);
                Toast.makeText(GameDetailsActivity.this, "User Node Updated ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToChatNode(GameDescription gameDescription) {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait ... ");
        progressDialog.show();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        ChatMessage chatMessage=new ChatMessage();
        chatMessage.setMessage(prefManager.getUserName()+" Join  "+gameDescription.getTitle() +" playground");
        chatMessage.setSenderId(prefManager.getUserId());
        chatMessage.setSenderName(prefManager.getUserName());
        chatMessage.setMessageTime(System.currentTimeMillis());
        chatMessage.setMessageType(Constants.WELCOME_MESSAGE);
        String massegekey=databaseReference.push().getKey();
        databaseReference.child(Constants.CHAT_ROOM).child(gameDescription.getGameId()).child(massegekey).setValue(chatMessage).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                Toast.makeText(GameDetailsActivity.this, "Chat Room is Created ", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void setUI(GameDescription gameDescription) {

        textViewgameName.setText(gameDescription.getTitle());
        textViewgameDescription.setText(gameDescription.getDescription());
        textViewgamePrize.setText(gameDescription.getPrice());
        textViewgameNumberOfPlayerRemain.setText(String.format(" %d", gameDescription.getNumberOfPlayer() - gameDescription.getGamerList().size()));
        textViewgameNumberOfPlayer.setText(String.format(" %d", gameDescription.getNumberOfPlayer()));
        textViewgameCreator.setText(gameDescription.getCreator().getUserName());
        textViewgroupType.setText(gameDescription.isPrivate() ? "Private" : "Public");
        textViewgameType.setText(gameDescription.isPubg() ? "PUBG" : "COUNTER STRIKE");

        playerListAdapter = new PlayerListAdapter(this, gameDescription.getGamerList());
        recyclerViewPlayerList.setAdapter(playerListAdapter);
        boolean isMember = false;
        for (GameCreator gameCreator : gameDescription.getGamerList()) {
            if (prefManager.getUserId().equalsIgnoreCase(gameCreator.getUserId())) {
                isMember = true;
            }
        }
        if (isMember) {
            findViewById(R.id.joinLayout).setVisibility(View.GONE);
            buttonJoinGame.setVisibility(View.GONE);
        } else {
            findViewById(R.id.joinLayout).setVisibility(View.VISIBLE);
            buttonJoinGame.setVisibility(View.VISIBLE);
        }

    }


}
