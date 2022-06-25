package com.example.hibarking.mechanical;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hibarking.MainActivity;
import com.example.hibarking.R;
import com.example.hibarking.driver.user_account.create_account;

public class add_mechanical_data extends AppCompatActivity {


    private Button create_account;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_mechanical_data);
        create_account_method();
    }
    private void create_account_method()
    {
        create_account=findViewById(R.id.add_mechanical_data);
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(add_mechanical_data.this, main_mechanical.class));

            }
        });
    }

}