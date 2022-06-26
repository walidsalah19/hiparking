package com.example.hibarking.garage_manager.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hibarking.R;
import com.example.hibarking.garage_manager.main_garage_manager;
import com.example.hibarking.garage_manager.show_garage_info;

import java.util.ArrayList;

public class garage_show_adapter extends RecyclerView.Adapter<garage_show_adapter.holder>{
    ArrayList<recycler_show_garage_info> arrayList;
   Fragment main;

    public garage_show_adapter(ArrayList<recycler_show_garage_info> arrayList, Fragment main) {
        this.arrayList = arrayList;
        this.main = main;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.garage_manager_show_recyclview, parent, false);
        return new holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
       holder.name.setText(arrayList.get(position).getName());
        holder.city.setText(arrayList.get(position).getCity());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_garage_info show=new show_garage_info();
                Bundle b=new Bundle();
                b.putString("garage_id",arrayList.get(position).garage_id);
                show.setArguments(b);
              main.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.grarage_manager_frameLayout,show).addToBackStack(null).commitAllowingStateLoss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class holder extends RecyclerView.ViewHolder
    {
         TextView name ,city;
        public holder(@NonNull View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.grage_manager_gargae_name_recycler);
            city=(TextView)itemView.findViewById(R.id.grage_manager_gargae_city_recycler);
        }
    }
}
