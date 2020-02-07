package com.ezevent.ui.chatScreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezevent.R;
import com.ezevent.controller.Constants;
import com.ezevent.controller.PrefManager;
import com.ezevent.ui.chatroom.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<ChatMessage> data;
    int MESSAGE_SEND = 1, MESSAGE_RECEIVED = 2,MESSAGE_INFO=3;
    String userId;
    PrefManager prefManager;

    public ChatAdapter(Context context, ArrayList<ChatMessage> data) {
        this.context = context;
        this.data = data;
        prefManager = new PrefManager(context);
        userId = prefManager.getUserId();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if (viewType == MESSAGE_INFO)
       {
           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_info, parent, false);
           return new ViewHolderInfo(view);
       }
        else if (viewType == MESSAGE_SEND) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_send_item, parent, false);
            return new ViewHolderSender(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_received_item, parent, false);
            return new ViewHolderReceiver(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage chatMessage = data.get(position);
        if (holder.getItemViewType() == MESSAGE_INFO) {
            ViewHolderInfo viewHolderInfo = (ViewHolderInfo) holder;
            viewHolderInfo.textViewMessageInfo.setText(chatMessage.getMessage());
//            Date messageTime = new Date(chatMessage.getMessageTime());
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
//            viewHoderSender.textViewTime.setText(sdf.format(messageTime));

        }
       else if (holder.getItemViewType() == MESSAGE_SEND) {
            ViewHolderSender viewHoderSender = (ViewHolderSender) holder;
            viewHoderSender.textViewMessage.setText(chatMessage.getMessage());
            viewHoderSender.senderName.setText(chatMessage.getSenderDetails().getUserName());
            Date messageTime = new Date(chatMessage.getMessageTime());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
            viewHoderSender.textViewTime.setText(sdf.format(messageTime));

        } else {

            ViewHolderReceiver viewHolderReceiver = (ViewHolderReceiver) holder;
            viewHolderReceiver.senderName.setText(chatMessage.getSenderDetails().getUserName());
            viewHolderReceiver.textViewMessage.setText(chatMessage.getMessage());
            Date messageTime = new Date(chatMessage.getMessageTime());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
            viewHolderReceiver.textViewTime.setText(sdf.format(messageTime));
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = data.get(position);
        if (chatMessage.getMessageType()== Constants.WELCOME_MESSAGE)
        {
            return MESSAGE_INFO;
        }
       else if (userId.equalsIgnoreCase(chatMessage.getSenderId()))
            return MESSAGE_SEND;

        else
            return MESSAGE_RECEIVED;
//        return super.getItemViewType(position);
    }
    class ViewHolderInfo extends RecyclerView.ViewHolder {
        TextView textViewMessageInfo;
        public ViewHolderInfo(@NonNull View itemView) {
            super(itemView);
            textViewMessageInfo=itemView.findViewById(R.id.messageInfo);
        }
    }

    class ViewHolderSender extends RecyclerView.ViewHolder {
        TextView senderName, textViewTime, textViewMessage;

        public ViewHolderSender(@NonNull View itemView) {
            super(itemView);
            senderName = itemView.findViewById(R.id.sender);
            textViewTime = itemView.findViewById(R.id.messageTime);
            textViewMessage = itemView.findViewById(R.id.message);
        }
    }

    class ViewHolderReceiver extends RecyclerView.ViewHolder {
        TextView senderName, textViewTime, textViewMessage;

        public ViewHolderReceiver(@NonNull View itemView) {

            super(itemView);
            senderName = itemView.findViewById(R.id.sender);
            textViewTime = itemView.findViewById(R.id.messageTime);
            textViewMessage = itemView.findViewById(R.id.message);
        }
    }
}
