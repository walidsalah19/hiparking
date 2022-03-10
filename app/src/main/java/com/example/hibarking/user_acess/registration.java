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
import android.widget.Toast;

import com.example.hibarking.MainActivity;
import com.example.hibarking.R;
import com.example.hibarking.driver.user_account.create_account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class registration extends AppCompatActivity {
    private Button registration;
    private EditText editText_email,editText_password,editText_confirm_password;
    private FirebaseAuth auth;
    private String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String  passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
    private String email_string,password_string;
    private RadioButton driver,mechanical,garage_manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_Dark);
        }else {
            setTheme(R.style.Theme_Light);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        registration_method();

    }

    private void registration_method()
    {
       registration=findViewById(R.id.registration);
       registration.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              // get_user_data();
               go_to_create_account();
           }
       });
    }
    private void get_user_data()
    {
        editText_email=findViewById(R.id.email_registration);
        editText_password=findViewById(R.id.password_registration);
        editText_confirm_password=findViewById(R.id.confirm_registration);
        email_string=editText_email.getText().toString();
        password_string=editText_password.getText().toString();
        check_email();
    }
    private void check_email()
    {
        if (TextUtils.isEmpty(editText_email.getText()))
        {
            Toast.makeText(this, "Your password should be more than 8 character", Toast.LENGTH_SHORT).show();
        }
        else if(!editText_email.getText().toString().matches(emailpattern))
        {
            editText_email.setError("Email must valid Contain @ like ola@gmail.com");
        }
        else
        {
            check_usename_password();
        }
    }
     private void check_usename_password()
     {
         if(TextUtils.isEmpty(editText_password.getText())||TextUtils.isEmpty(editText_confirm_password.getText()))
         {
             Toast.makeText(this, "Please Enter Your Data Correctly", Toast.LENGTH_SHORT).show();
         }
         else if(editText_password.getText().length()<8)
         {
             Toast.makeText(this, "You'r password should be more than 8 character", Toast.LENGTH_SHORT).show();
         }
         else if(editText_password.getText().toString().matches(passwordPattern))
         {
             Toast.makeText(this, "You'r password should contain small , large character and number ", Toast.LENGTH_SHORT).show();
         }
         else if(! editText_password.getText().equals(editText_confirm_password.getText()))
         {
             editText_confirm_password.setError("not match the password ");
         }

         else
         {
             sining();
         }
     }
    private void sining() {

        auth=FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email_string, password_string)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(registration.this, "You Create new account", Toast.LENGTH_SHORT).show();
                    go_to_create_account();
                }
            }
        });
    }
    private void go_to_create_account()
    {
        driver=findViewById(R.id.r_driver);
        mechanical=findViewById(R.id.r_mechanical);
        garage_manager=findViewById(R.id.r_garage_manager);
         if (driver.isChecked()) {
             startActivity(new Intent(this,create_account.class));
         }
        else if (mechanical.isChecked()) {
             startActivity(new Intent(this,add_mechanical_data.class));
        }
        else if(garage_manager.isChecked())
         {
             startActivity(new Intent(this,AddGarageManData.class));
         }
    }


}