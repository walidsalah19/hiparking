package com.example.hibarking.chating;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hibarking.R;
import com.example.hibarking.data_class.message_model;

import java.util.ArrayList;


public class chat_recycler_adapter extends RecyclerView.Adapter<chat_recycler_adapter.help> {

    ArrayList<message_model> arrayList;
    String user_id;
    Fragment fragment;
    public chat_recycler_adapter(ArrayList<message_model> arrayList, Fragment fragment, String user_id) {
        this.arrayList = arrayList;
        this.user_id=user_id;
        this.fragment=fragment;
    }

    @NonNull
    @Override
    public help onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recycler_layout,parent,false);
        return new help(v);
    }

    @Override
    public void onBindViewHolder(@NonNull help holder, int position) {
          String message_id=arrayList.get(position).getUser_id();
          if (message_id.equals(user_id))
          {
              holder.recever_layout.setVisibility(View.INVISIBLE);
              holder.sender_layout.setVisibility(View.VISIBLE);
              holder.sender_message.setText(arrayList.get(position).getMessage());
              holder.sender_time.setText(arrayList.get(position).getMessage_date());
              holder.sender_name.setText(arrayList.get(position).getUser_name());
              Glide.with(fragment.getActivity()).load(arrayList.get(position).getUser_image()).into(holder.sender_image);
          }
          else
          {
              holder.recever_layout.setVisibility(View.VISIBLE);
              holder.sender_layout.setVisibility(View.INVISIBLE);
              holder.recever_message.setText(arrayList.get(position).getMessage());
              holder.recever_time.setText(arrayList.get(position).getMessage_date());
              holder.reciver_name.setText(arrayList.get(position).getUser_name());
              Glide.with(fragment.getActivity()).load(arrayList.get(position).getUser_image()).into(holder.recever_image);
          }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class help extends RecyclerView.ViewHolder
    {
        TextView sender_message,sender_time,recever_message,recever_time,reciver_name,sender_name;
        ImageView sender_image,recever_image;
        RelativeLayout sender_layout,recever_layout;
        public help(@NonNull View itemView) {
            super(itemView);
            sender_message=(TextView)itemView.findViewById(R.id.sender_message);
            sender_image=(ImageView) itemView.findViewById(R.id.sender_imag);
            sender_time=(TextView)itemView.findViewById(R.id.sender_time);
            sender_layout=(RelativeLayout) itemView.findViewById(R.id.sender_layout);
            sender_name=(TextView) itemView.findViewById(R.id.sender_name);

            recever_message=(TextView)itemView.findViewById(R.id.recever_message);
            recever_image=(ImageView) itemView.findViewById(R.id.reciver_image);
            recever_time=(TextView)itemView.findViewById(R.id.recever_time);
            recever_layout=(RelativeLayout) itemView.findViewById(R.id.resever_layout);
            reciver_name=(TextView) itemView.findViewById(R.id.recever_name);
        }
    }
}
