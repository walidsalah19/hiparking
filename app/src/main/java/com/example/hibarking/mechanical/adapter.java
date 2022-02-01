package com.example.hibarking.mechanical;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hibarking.R;

import java.util.ArrayList;


public class adapter extends RecyclerView.Adapter<adapter.helper>{
    Context context ;
    ArrayList<String> arrayList;
    main_mechanical main;
    public adapter(Context context, ArrayList<String> ArrayList,main_mechanical ma)
    {
        this.context = context;
        this.arrayList = ArrayList;
        this.main = ma;
    }
    @NonNull
    @Override
    public helper onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.mechanical_recycler, parent, false);
        return new helper(v);
    }

    @Override
    public void onBindViewHolder(@NonNull helper holder, int position) {
        holder.name.setText(arrayList.get(position));
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // main.getSupportFragmentManager().beginTransaction()
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class helper extends RecyclerView.ViewHolder
    {
         TextView name;
        public helper(@NonNull View itemView) {
            super(itemView);
            name=(TextView) itemView.findViewById(R.id.mechanical_recycler_customer_name);
        }
    }
}
