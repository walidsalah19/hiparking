package com.example.hibarking.garage_manager;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.hibarking.R;
import com.example.hibarking.mechanical.add_mechanical_data;
import com.example.hibarking.mechanical.main_mechanical;


public class AddGarageManData extends AppCompatActivity {
    private EditText usename,national_id,phone;
    private String id,email;
    private ImageButton licence;
    private Button send;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_garage_man_data);
        intialization_tool();
        create_account_method();
    }
    private void create_account_method()
    {
        send=findViewById(R.id.add_garage_manager_info);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddGarageManData.this, main_garage_manager.class));

            }
        });
    }
    private void intialization_tool()
    {
        usename=findViewById(R.id.add_garage_manager_username);
        national_id=findViewById(R.id.add_manager_national_id);
        phone=findViewById(R.id.add_manager_phone);
        
    }
}