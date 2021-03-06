package com.ezevent.ui.chatScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ezevent.R;
import com.ezevent.controller.Constants;
import com.ezevent.controller.PrefManager;
import com.ezevent.ui.chatroom.ChatMessage;
import com.ezevent.ui.creategame.CreateGameActivity;
import com.ezevent.ui.creategame.GameCreator;
import com.ezevent.ui.creategame.GameDescription;
import com.ezevent.ui.gameDetails.GameDetailsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatScreenActivity extends AppCompatActivity {
    GameDescription gameDescription;
    PrefManager prefManager;
    ImageView imageViewIcon;
    TextView gameName;
    RecyclerView recyclerViewChat;
    LinearLayoutManager layoutManager;
    EditText editTextSendMessage;
    Button button;
    CardView cardView;
    ChatAdapter chatAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        prefManager=new PrefManager(this);
        imageViewIcon=findViewById(R.id.gameIcon);
        gameName=findViewById(R.id.gameName);
        recyclerViewChat=findViewById(R.id.recyclerViewChat);
        layoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,true);
        recyclerViewChat.setLayoutManager(layoutManager);
        cardView=findViewById(R.id.cardView);
        editTextSendMessage=findViewById(R.id.edittextMessage);
        button=findViewById(R.id.sendMessage);

        gameDescription = (GameDescription) getIntent().getSerializableExtra(Constants.GAME_DETAILS);
        if (gameDescription != null) {
            setUI(gameDescription);


        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextSendMessage.getText().toString().isEmpty())
                {
                    sendmessage(editTextSendMessage.getText().toString());
                    editTextSendMessage.setText("");
                }
            }
        });
    }

    private void sendmessage(String message) {

        GameCreator senderDetails=new GameCreator();
        senderDetails.setUserName(prefManager.getUserName());
        senderDetails.setMobileNumber(prefManager.getUserMobile());
        senderDetails.setUserId(prefManager.getUserId());
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        ChatMessage chatMessage=new ChatMessage();
        chatMessage.setMessage(message);
        chatMessage.setSenderId(prefManager.getUserId());
        chatMessage.setSenderName(prefManager.getUserName());
        chatMessage.setMessageTime(System.currentTimeMillis());
        chatMessage.setMessageType(Constants.NORMAL_MESSAGE);
        chatMessage.setSenderDetails(senderDetails);
        String massegekey=databaseReference.push().getKey();
        databaseReference.child(Constants.CHAT_ROOM).child(gameDescription.getGameId()).child(massegekey).setValue(chatMessage).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });


    }

    private void setUI(final GameDescription gameDescription) {
        gameName.setText(gameDescription.getTitle());

        if (gameDescription.isPubg())
            Glide.with(this).load(R.drawable.pubg).into(imageViewIcon);
        else
            Glide.with(this).load(R.drawable.cs).into(imageViewIcon);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChatScreenActivity.this, GameDetailsActivity.class);
                intent.putExtra(Constants.GAME_DETAILS,gameDescription);
                startActivity(intent);
            }
        });

        if (gameDescription.getGameStatus()==Constants.GAME_ACTIVE)
        {
            editTextSendMessage.setEnabled(true);
        }
        else
            editTextSendMessage.setEnabled(false);

//        if (gameDescription.getCreator().getUserId().equalsIgnoreCase(prefManager.getUserId()))
//            editTextSendMessage.setEnabled(true);
//        else
//            editTextSendMessage.setEnabled(false);
        
        getMesage(gameDescription.getGameId());
                
    }

    private void getMesage(String gameId) {
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Getting messages ...");
        dialog.show();
        dialog.setCancelable(false);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child(Constants.CHAT_ROOM)
                .child(gameId);
        databaseReference.orderByChild(Constants.MessageTIME).limitToLast(100).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ChatMessage> gameDescriptionList= new ArrayList<>();
                for (DataSnapshot data:dataSnapshot.getChildren()
                ) {
                    gameDescriptionList.add(data.getValue(ChatMessage.class));
                }
                setRecyclerView(gameDescriptionList);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Something  ","Wrong while retring "+databaseError.getMessage());
                dialog.dismiss();
                databaseError.toException().printStackTrace();
            }
        });
        
    }

    private void setRecyclerView(ArrayList<ChatMessage> messages) {

        messages=reverseArrayList(messages);
        chatAdapter=new ChatAdapter(this,messages);
        recyclerViewChat.setAdapter(chatAdapter);
    }
    public ArrayList reverseArrayList(ArrayList alist)
    {
        // Arraylist for storing reversed elements
        ArrayList revArrayList = new ArrayList();
        for (int i = alist.size() - 1; i >= 0; i--) {

            // Append the elements in reverse order
            revArrayList.add(alist.get(i));
        }

        // Return the reversed arraylist
        return revArrayList;
    }
}
