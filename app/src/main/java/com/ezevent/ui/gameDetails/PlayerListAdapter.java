package com.ezevent.ui.gameDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezevent.R;
import com.ezevent.ui.creategame.GameCreator;

import java.util.ArrayList;
import java.util.List;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.ViewHolder> {

    Context context;
    List<GameCreator> data;

    public PlayerListAdapter(Context context, List<GameCreator> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.game_member_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GameCreator gameCreator=data.get(position);
        holder.textViewPlayerName.setText(gameCreator.getUserName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class  ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewPlayerName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPlayerName=itemView.findViewById(R.id.playerName);
        }
    }
}
