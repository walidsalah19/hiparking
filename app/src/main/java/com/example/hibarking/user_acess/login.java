package com.example.hibarking.user_acess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hibarking.MainActivity;
import com.example.hibarking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class login extends AppCompatActivity {
    private TextView registration,forgit_password;
    private Button login_btn,phone_btn;
    private EditText email_login,password_login;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private String username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_Dark);
        }else {
            setTheme(R.style.Theme_Light);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        login_Button_method();
        registration_method();

    }
    private void login_Button_method() {
        login_btn=findViewById(R.id.email_login);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_method();
            }
        });
    }
    private void registration_method()
    {
        registration=findViewById(R.id.new_account);
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this, com.example.hibarking.user_acess.registration.class));
            }
        });

    }

    private void login_method()
    {
        text_initialized();
        auth=FirebaseAuth.getInstance();
        if(TextUtils.isEmpty(username)&&TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Enter you'r data correctly", Toast.LENGTH_SHORT).show();
        }
        else{
               auth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful())
                       {
                           Toast.makeText(login.this, "Login is Successful ", Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(login.this, MainActivity.class));
                       }
                       else
                       {
                           Toast.makeText(login.this, "Error", Toast.LENGTH_LONG).show();
                       }
                   }
               });

        }
    }
    private void text_initialized()
    {
       email_login=findViewById(R.id.email_edittext);
       password_login=findViewById(R.id.email_password_text);
       username=email_login.getText().toString().trim();
       password=password_login.getText().toString().trim();
    }
}