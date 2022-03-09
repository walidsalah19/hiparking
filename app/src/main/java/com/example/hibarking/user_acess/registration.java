package com.example.hibarking.user_acess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hibarking.R;
import com.example.hibarking.account.create_account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class registration extends AppCompatActivity {
    private Button registration;
    private EditText editText_email,editText_password,editText_confirm_password;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_Dark);
        }else {
            setTheme(R.style.Theme_Light);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //uuuu

        registration_method();

    }
    private void registration_method()
    {
       registration=findViewById(R.id.registration);
       registration.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               get_user_data();
           }
       });
    }
    private void get_user_data()
    {
        editText_email=findViewById(R.id.email_registration);
        editText_password=findViewById(R.id.password_registration);
        editText_confirm_password=findViewById(R.id.confirm_registration);
        String email_string,password_string,conferm_string;
        email_string=editText_email.getText().toString().trim();
        password_string=editText_password.getText().toString().trim();
        conferm_string=editText_confirm_password.getText().toString().trim();
        if(TextUtils.isEmpty(email_string)||TextUtils.isEmpty(password_string)||TextUtils.isEmpty(conferm_string))
        {
            Toast.makeText(this, "Please Enter Your Data Correctly", Toast.LENGTH_SHORT).show();
        }
        else if(password_string.length()<8)
        {
            Toast.makeText(this, "Your password should be more than 8 character", Toast.LENGTH_SHORT).show();
        }
        else if(! password_string.equals(conferm_string))
        {
            Toast.makeText(this, "Password not match", Toast.LENGTH_SHORT).show();
        }

        else
        {
            sining(email_string,password_string);
        }
    }

    private void sining(String email_string, String password_string) {

        auth=FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email_string, password_string)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(registration.this, "You Create new account", Toast.LENGTH_SHORT).show();
                    go_to_create_account(email_string);
                }
            }
        });
    }
    private void go_to_create_account(String email)
    {
        Intent account=new Intent(this,create_account.class);
        account.putExtra("email",email);
        startActivity(account);
    }


}