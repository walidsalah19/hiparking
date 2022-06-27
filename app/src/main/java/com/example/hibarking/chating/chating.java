package com.example.hibarking.chating;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.hibarking.R;
import com.example.hibarking.data_class.message_model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.example.hibarking.SharedPref;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class chating extends Fragment {

    private FirebaseAuth auth;
    private FirebaseFirestore database;
    private RecyclerView recycler;
    private ArrayList<message_model> message_arr;
    private String user_image,user_id,send_time,chat_type,chat_id,user_name;
    private FloatingActionButton send;
    private EditText message_text;
    SharedPref sharedPref;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(getActivity());
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            getActivity().setTheme(R.style.Theme_Dark);
        }else {
            getActivity().setTheme(R.style.Theme_Light);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_chating, container, false);
        chat_type=getArguments().getString("type").toString();
        chat_id=getArguments().getString("id").toString();
        firebase_tool();
        recycler_view_method(v);
        get_driver_image(chat_type);
        send_message(v);

        return v;
    }
    private void get_from_chat()
    {
        DatabaseReference data=FirebaseDatabase.getInstance().getReference();
        data.child("chat").child(user_id).child(chat_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    message_arr.clear();
                    for (DataSnapshot snap:snapshot.getChildren())
                    {
                        String message=snap.child("message").getValue().toString();
                        String user_image=snap.child("user_image").getValue().toString();
                        String user_id=snap.child("user_id").getValue().toString();
                        String message_date=snap.child("message_date").getValue().toString();
                        String user_name=snap.child("user_name").getValue().toString();
                        message_arr.add(new message_model(message,user_image,user_id,message_date,user_name));

                    }
                    chat_recycler_adapter adapter=new chat_recycler_adapter(message_arr, chating.this,user_id);
                    recycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    recycler.smoothScrollToPosition(recycler.getAdapter().getItemCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void send_message(View v) {
        message_text=v.findViewById(R.id.teacher_send_message_text);
        send=v.findViewById(R.id.teacher_send_message);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chech_message();
            }
        });
    }

    private void chech_message() {
        if (TextUtils.isEmpty(message_text.getText().toString()))
        {
            message_text.setError("enter the message");
        }
        else
        {
            send_to_database();
        }
    }
    private void get_driver_image(String type) {

        database.collection(type).document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    if (task.getResult().exists())
                    {
                        user_image=task.getResult().get("uri").toString();
                        user_name=task.getResult().get("name").toString();
                    }
                }
            }
        });
    }
    private void get_date()
    {
        send_time=  new SimpleDateFormat("hh.mm").format(Calendar.getInstance().getTime());
    }
    private void recycler_view_method(View v) {
        recycler=v.findViewById(R.id.teacher_chat_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        message_arr=new ArrayList<>();
        get_from_chat();
    }

    private void firebase_tool() {
        auth=FirebaseAuth.getInstance();
        database= FirebaseFirestore.getInstance();
        user_id=auth.getCurrentUser().getUid().toString();
    }
    private void send_to_database() {
        get_date();
        DatabaseReference data=FirebaseDatabase.getInstance().getReference();
        message_model model=new message_model(message_text.getText().toString(),user_image,user_id,send_time,user_name);
        data.child("chat").child(user_id).child(chat_id).push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    data.child("chat").child(chat_id).child(user_id).push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                message_text.setText("");

                            }
                        }
                    });

                }
            }
        });
    }


}