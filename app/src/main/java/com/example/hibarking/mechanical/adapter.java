package com.example.hibarking.mechanical;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hibarking.R;
import com.example.hibarking.data_class.Driver;
import com.example.hibarking.data_class.DriverMechanicalRegister;

import java.util.ArrayList;


public class adapter extends RecyclerView.Adapter<adapter.helper>{
    ArrayList<Driver> arrayList;
    Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemUnRegister(String name ,String uid , String date);

    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
    public adapter(ArrayList<Driver> ArrayList, Context context)
    {
        this.arrayList = ArrayList;
        this.context = context;
    }
    @NonNull
    @Override
    public helper onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.mechanical_recycler, parent, false);
        return new helper(v);
    }

    @Override
    public void onBindViewHolder(@NonNull helper holder, int position) {
        holder.name.setText(arrayList.get(position).getName());
        String Uid = arrayList.get(position).getId();
        String Name = arrayList.get(position).getName();
        String date = arrayList.get(position).getDate();
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemUnRegister(Name,Uid,date);
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
