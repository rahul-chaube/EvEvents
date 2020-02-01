package com.ezevent.ui.gameDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ezevent.R;
import com.ezevent.controller.Constants;
import com.ezevent.controller.PrefManager;
import com.ezevent.ui.creategame.GameCreator;
import com.ezevent.ui.creategame.GameDescription;

public class GameDetailsActivity extends AppCompatActivity {
    TextView textViewgameName,textViewgameDescription,textViewgamePrize,textViewgameType,textViewgroupType,textViewgameCreator,
    textViewgameNumberOfPlayer,textViewgameNumberOfPlayerRemain;
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
        prefManager=new PrefManager(this);
        textViewgameName=findViewById(R.id.gameName);
        textViewgameDescription=findViewById(R.id.gameDescription);
        textViewgamePrize=findViewById(R.id.gamePrize);
        textViewgroupType=findViewById(R.id.gameType);
        textViewgameType=findViewById(R.id.groupType);
        textViewgameCreator=findViewById(R.id.gameCreator);
        textViewgameNumberOfPlayer=findViewById(R.id.gameNumberOfPlayer);
        textViewgameNumberOfPlayerRemain=findViewById(R.id.gameNumberOfPlayerRemain);
        buttonJoinGame=findViewById(R.id.joinGame);
        recyclerViewPlayerList=findViewById(R.id.recyclerViewPlayerList);
        layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewPlayerList.setLayoutManager(layoutManager);

        gameDescription= (GameDescription) getIntent().getSerializableExtra(Constants.GAME_DETAILS);
        if (gameDescription!=null) {
          setUI(gameDescription);

            if (gameDescription.getNumberOfPlayer()==gameDescription.getGamerList().size())
            {
                buttonJoinGame.setBackgroundColor(getResources().getColor(R.color.red));
                buttonJoinGame.setTextColor(getResources().getColor(R.color.white));
            }
        }

        buttonJoinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameDescription.getNumberOfPlayer()==gameDescription.getGamerList().size())
                {
                    Toast.makeText(GameDetailsActivity.this, "No Room , Sorry ! Game Room is filled with max number of player", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(GameDetailsActivity.this, "Join Button is CLicked ", Toast.LENGTH_SHORT).show();
                }
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
        textViewgroupType.setText(gameDescription.isPrivate()?"Private":"Public");
        textViewgameType.setText(gameDescription.isPubg()?"PUBG":"COUNTER STRIKE");

        playerListAdapter=new PlayerListAdapter(this,gameDescription.getGamerList());
        recyclerViewPlayerList.setAdapter(playerListAdapter);
        boolean isMember=false;
        for (GameCreator gameCreator:gameDescription.getGamerList())
        {
            if (prefManager.getUserId().equalsIgnoreCase(gameCreator.getUserId()))
            {
                isMember=true;
            }
        }
        if (isMember)
        {
            findViewById(R.id.joinLayout).setVisibility(View.GONE);
            buttonJoinGame.setVisibility(View.GONE);
        }
        else
        {
            findViewById(R.id.joinLayout).setVisibility(View.VISIBLE);
            buttonJoinGame.setVisibility(View.VISIBLE);
        }

    }


}
