package com.ezevent.ui.chatroom;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ezevent.R;
import com.ezevent.controller.Constants;
import com.ezevent.ui.creategame.GameDescription;
import com.ezevent.ui.gameDetails.GameDetailsActivity;

import java.util.ArrayList;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.ViewHolder> {

    Context context;
    ArrayList<GameDescription> data;

    public GameListAdapter(Context context, ArrayList<GameDescription> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.game_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final GameDescription gameDescription=data.get(position);
        holder.textViewDescription.setText(gameDescription.getDescription());
        holder.textViewNoOfPlayer.setText("No of  Player : "+gameDescription.getNumberOfPlayer());
        holder.textViewPrize.setText("Prize \u20B9 "+gameDescription.getPrice());
        holder.textViewGameName.setText(gameDescription.getTitle());
        if (gameDescription.isPubg())
            Glide.with(context).load(R.drawable.pubg).into(holder.imageViewIcon);
        else
            Glide.with(context).load(R.drawable.cs).into(holder.imageViewIcon);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, GameDetailsActivity.class);
                intent.putExtra(Constants.GAME_DETAILS,gameDescription);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView  imageViewIcon;
        TextView textViewGameName,textViewPrize,textViewDate,textViewNoOfPlayer,textViewDescription;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.main);
            imageViewIcon=itemView.findViewById(R.id.gameIcon);
            textViewGameName=itemView.findViewById(R.id.gameName);
            textViewPrize=itemView.findViewById(R.id.prize);
            textViewNoOfPlayer=itemView.findViewById(R.id.NoOfPlayer);
            textViewDate=itemView.findViewById(R.id.startDate);
            textViewDescription=itemView.findViewById(R.id.gameDescription);
        }
    }
}
