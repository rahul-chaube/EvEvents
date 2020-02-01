package com.ezevent.ui.creategame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ezevent.R;
import com.ezevent.controller.Constants;
import com.ezevent.controller.PrefManager;
import com.ezevent.ui.chatroom.ChatMessage;
import com.ezevent.ui.gameDetails.GameDetailsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CreateGameActivity extends AppCompatActivity {
    LinearLayout linearLayoutGameCredentials;
    RadioButton radioButtonPublic,radioButtonPrivate,radioButtonPubg,radioButtonCS;
    RadioGroup radioGroup_GameType, radioGroup_GoupType;
    TextInputLayout textInputLayoutGameTitle,textInputLayoutNoOfPlayer,textInputLayoutGameDescription,textInputLayoutPrice,textInputLayoutGameUserId,textInputLayoutGamePassword;
    TextInputEditText textInputEditTextGameTitle,textInputEditTextNoOfPlayer,textInputEditTextGameDescription,textInputEditTextPrice,textInputEditTextGameUserId,textInputEditTextGamePassword;
    Button buttonCreateGame;

    GameDescription gameDescription;
    GameCreator gameCreator;
    PrefManager prefManager;
    List<GameCreator> playerList;

    boolean isPubg =true, isPrivate =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        buttonCreateGame=findViewById(R.id.createButton);
        linearLayoutGameCredentials=findViewById(R.id.gameCredentials);
        radioButtonCS=findViewById(R.id.counterStrike);
        radioButtonPubg=findViewById(R.id.pubg);
        radioButtonPrivate=findViewById(R.id.radioPrivate);
        radioButtonPublic=findViewById(R.id.radioPublic);

        radioGroup_GoupType =findViewById(R.id.groupType);
        radioGroup_GameType =findViewById(R.id.gameType);

        textInputLayoutGameTitle=findViewById(R.id.textInputLayoutGameTitle);
        textInputLayoutNoOfPlayer=findViewById(R.id.textInputLayoutGameNoOFPlayer);
        textInputLayoutGameDescription=findViewById(R.id.textInputLayoutGameDescription);
        textInputLayoutPrice=findViewById(R.id.textInputLayoutGamePrice);
        textInputLayoutGameUserId=findViewById(R.id.textInputLayoutGameUserId);
        textInputLayoutGamePassword=findViewById(R.id.textInputLayoutGameUserPassword);

        textInputEditTextGameTitle=findViewById(R.id.editTextGameTitle);
        textInputEditTextNoOfPlayer=findViewById(R.id.editTextGameNoOFPlayer);
        textInputEditTextGameDescription=findViewById(R.id.editTextGameDescription);
        textInputEditTextPrice=findViewById(R.id.editTextGamePrice);
        textInputEditTextGameUserId=findViewById(R.id.editTextGameUserId);
        textInputEditTextGamePassword=findViewById(R.id.editTextGameUserPassword);

        gameDescription=new GameDescription();
        gameCreator=new GameCreator();
        prefManager=new PrefManager(this);

        playerList=new ArrayList<>();

        gameCreator.setMobileNumber(prefManager.getUserMobile());
        gameCreator.setUserId(prefManager.getUserId());
        gameCreator.setUserName(prefManager.getUserName());
        playerList.add(gameCreator);


        gameDescription.setCreateAt(System.currentTimeMillis());
        gameDescription.setGamerList(playerList);
        gameDescription.setCreator(gameCreator);

        buttonCreateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        radioGroup_GoupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radioButtonPrivate.isChecked())
                {
                    linearLayoutGameCredentials.setVisibility(View.VISIBLE);
                    isPrivate =true;
                }
                else
                {
                    linearLayoutGameCredentials.setVisibility(View.GONE); // public Default
                    isPrivate =false;
                }
            }
        });

        radioGroup_GameType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radioButtonPubg.isChecked())
                    isPubg =true;
                else
                    isPubg =false;  // Counter Strike is Default
            }

        });
    }

    private void validateData() {
        boolean allSet=true;

        if (textInputEditTextGameTitle.getText().toString().isEmpty())
        {
            textInputLayoutGameTitle.setError("Enter Game Title");
            allSet=allSet && false;
        }
        else if (textInputEditTextGameTitle.getText().toString().length() <6 )
        {
            textInputLayoutGameTitle.setError("Title should be more than 6 char");
            allSet=allSet && false;
        }
        else
        {

            textInputLayoutGameTitle.setErrorEnabled(false);
            gameDescription.setTitle(textInputEditTextGameTitle.getText().toString());
            allSet=allSet && true;
        }

        if (textInputEditTextGameDescription.getText().toString().isEmpty())
        {
            textInputLayoutGameDescription.setError("Enter Game Description");
            allSet=allSet && false;
        }
        else if (textInputEditTextGameDescription.getText().toString().length() <50 )
        {
            textInputLayoutGameDescription.setError("Description  should be more than 50 char");
            allSet=allSet && false;
        }
        else
        {

            textInputLayoutGameDescription.setErrorEnabled(false);
            gameDescription.setDescription(textInputEditTextGameDescription.getText().toString());
            allSet=allSet && true;
        }





        if (textInputEditTextNoOfPlayer.getText().toString().isEmpty())
        {
            textInputLayoutNoOfPlayer.setError("Enter Number of Player");
            allSet=allSet && false;
        }

        else if (Integer.parseInt(textInputEditTextNoOfPlayer.getText().toString())<=2)
        {
            textInputLayoutNoOfPlayer.setError("No of Player Must Be grater than 2");
            allSet=allSet && false;
        }
        else
        {
            textInputLayoutNoOfPlayer.setErrorEnabled(false);
            gameDescription.setNumberOfPlayer(Integer.parseInt(textInputEditTextNoOfPlayer.getText().toString().trim()));
            allSet= allSet && true;
        }


        if (textInputEditTextPrice.getText().toString().isEmpty())
        {
            textInputLayoutPrice.setError("Enter Prize ");
            allSet=allSet &&false;
        }

        else if (Integer.parseInt(textInputEditTextPrice.getText().toString())<=99)
        {
            textInputLayoutPrice.setError("Prize must be grater than 99");
            allSet=allSet &&false;
        }
        else
        {
            textInputLayoutPrice.setErrorEnabled(false);
            gameDescription.setPrice(textInputEditTextPrice.getText().toString().trim());
            allSet=allSet && true;
        }

        gameDescription.setPubg(isPubg);
        gameDescription.setPrivate(isPrivate);

        if (isPrivate)
        {
            GameCredentials gameCredentials=new GameCredentials();
            if (textInputEditTextGameUserId.getText().toString().isEmpty())
            {
                textInputLayoutGameUserId.setError("Enter Game Host Id");
                allSet=allSet &&false;
            }
            else if (textInputEditTextGameUserId.getText().toString().length() <6 )
            {
                textInputLayoutGameUserId.setError("Description  should be more than 6 char");
                allSet=allSet && false;

            }
            else
            {
                gameCredentials.setGameName(textInputEditTextGameUserId.getText().toString());
                textInputLayoutGameUserId.setErrorEnabled(false);
//                gameDescription.setDescription(textInputEditTextGameUserId.getText().toString());
                allSet=allSet && true;

            }


            if (textInputEditTextGamePassword.getText().toString().isEmpty())
            {
                textInputLayoutGamePassword.setError("Enter Host Password");
                allSet=allSet &&false;
            }
            else if (textInputEditTextGamePassword.getText().toString().length() <6 )
            {
                textInputLayoutGamePassword.setError("Password  should be more than 6 char");
                allSet=allSet &&false;

            }
            else
            {
                gameCredentials.setPassword(textInputEditTextGamePassword.getText().toString());
                textInputLayoutGamePassword.setErrorEnabled(false);
//                gameDescription.setDescription(textInputEditTextGamePassword.getText().toString());
                allSet=allSet && true;

            }

        gameDescription.setGameCredentials(gameCredentials);

        }

        if (allSet)
        {
            Toast.makeText(this, "All is Set ", Toast.LENGTH_SHORT).show();
            saveData();
        }
        else
        {
            Toast.makeText(this, "Failed ", Toast.LENGTH_SHORT).show();
        }


    }

    private void saveData() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait ... ");
        progressDialog.show();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

        final String key=databaseReference.push().getKey();
        gameDescription.setGameId(key);
        databaseReference.child(Constants.GAME_NODE).child(key).setValue(gameDescription).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    createChatNode(key);

                }
                else
                    Toast.makeText(CreateGameActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void createChatNode(String key) {

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait ... ");
        progressDialog.show();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        ChatMessage chatMessage=new ChatMessage();
        chatMessage.setMessage(prefManager.getUserName()+" Created  "+gameDescription.getTitle() +" playground");
        chatMessage.setSenderId(prefManager.getUserId());
        chatMessage.setSenderName(prefManager.getUserName());
        chatMessage.setMessageTime(System.currentTimeMillis());
        chatMessage.setMessageType(Constants.WELCOME_MESSAGE);
        String massegekey=databaseReference.push().getKey();
        databaseReference.child(Constants.CHAT_ROOM).child(gameDescription.getGameId()).child(massegekey).setValue(chatMessage).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                Toast.makeText(CreateGameActivity.this, "Chat Room is Created ", Toast.LENGTH_SHORT).show();
                addGroupToPlayerList();
            }
        });


    }

    private void addGroupToPlayerList() {
        addToUserNode(gameDescription);

    }

    private void addToUserNode(GameDescription gameDescription) {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait ... ");
        progressDialog.show();
        progressDialog.setCancelable(false);

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(Constants.USER_NODE);
        databaseReference.child(prefManager.getUserId()).child(Constants.My_GAMELSIT).child(gameDescription.getGameId()).setValue(gameDescription).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                finish();
                Toast.makeText(CreateGameActivity.this, "User Node Updated ", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
