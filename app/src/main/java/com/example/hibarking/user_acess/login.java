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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hibarking.MainActivity;
import com.example.hibarking.R;
import com.example.hibarking.garage_manager.main_garage_manager;
import com.example.hibarking.mechanical.main_mechanical;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {
    private TextView registration,forgit_password;
    private Button login_btn,phone_btn;
    private EditText email_login,password_login;
    private FirebaseAuth auth;
    private FirebaseFirestore database;
    private String username,password,type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_Dark);
        }else {
            setTheme(R.style.Theme_Light);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        text_initialized();
        login_Button_method();
        registration_method();

    }

    @Override
    protected void onStart() {
        auth=FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null)
        {
             get_user_type(auth.getCurrentUser().getUid().toString());
        }
        super.onStart();
    }




    private void login_Button_method() {
        login_btn=(Button) findViewById(R.id.email_login);
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


        username=email_login.getText().toString().trim();
        password=password_login.getText().toString().trim();
        if(TextUtils.isEmpty(username)&&TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Enter your data correctly", Toast.LENGTH_SHORT).show();
        }
        else{
               auth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful())
                       {
                           get_user_type(task.getResult().getUser().getUid());
                       }
                       else
                       {
                           Toast.makeText(login.this, "No such a User", Toast.LENGTH_LONG).show();
                       }
                   }
               });

        }
    }
    boolean next_user=false;
    private void get_user_type(String id)
    {

        database=FirebaseFirestore.getInstance();

           database.collection("User").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
               @Override
               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                   if (task.isSuccessful()) {
                       if (task.getResult().exists()) {
                           next_user = true;
                           startActivity(new Intent(login.this, MainActivity.class));
                           finish();
                       }
                   }
               }
           });
           database.collection("garage_manager").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
               @Override
               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                   if (task.isSuccessful()) {
                       if (task.getResult().exists()) {
                           startActivity(new Intent(login.this, main_garage_manager.class));
                           finish();
                       }
                   }
               }
           });
          database.collection("Mechanical").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        startActivity(new Intent(login.this, main_mechanical.class));
                        finish();
                    }
                }
            }
        });
    }
    private void text_initialized()
    {
       email_login=findViewById(R.id.email_edittext);
       password_login=findViewById(R.id.email_password_text);
    }
}