package com.example.hibarking.user_acess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.hibarking.R;
import com.example.hibarking.driver.user_account.create_account;
import com.example.hibarking.garage_manager.AddGarageManData;
import com.example.hibarking.mechanical.add_mechanical_data;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class registration extends AppCompatActivity {
    private Button registration;
    private EditText editText_email,editText_password,editText_confirm_password;
    private FirebaseAuth auth;
    private String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String  passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
    private String email_string,password_string;
    private RadioButton driver,mechanical,garage_manager;
    private SweetAlertDialog pDialogLoading,pDialogSuccess,pDialogerror;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_Dark);
        }else {
            setTheme(R.style.Theme_Light);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        driver=findViewById(R.id.r_driver);
        mechanical=findViewById(R.id.r_mechanical);
        garage_manager=findViewById(R.id.r_garage_manager);
        driver.setChecked(true);
        registration_method();
        sweetalert();

    }
    private void sweetalert()
    {
        //loading

        pDialogLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setCancelable(false);

        //error
        pDialogerror= new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        pDialogerror.setConfirmText(getString(R.string.dialog_ok));
        pDialogerror.setConfirmClickListener(sweetAlertDialog -> {
            pDialogerror.dismiss();
        });
        pDialogerror.setCancelable(true);

        //Success
        pDialogSuccess= new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        pDialogSuccess.setConfirmText(getString(R.string.dialog_ok));
        pDialogSuccess.setConfirmClickListener(sweetAlertDialog -> {
            pDialogSuccess.dismiss();
        });
        pDialogSuccess.setCancelable(true);
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
         if(TextUtils.isEmpty(editText_password.getText().toString())||TextUtils.isEmpty(editText_confirm_password.getText().toString()))
         {
             Toast.makeText(this, "Please Enter Your Data Correctly", Toast.LENGTH_SHORT).show();
         }
         else if(editText_password.getText().toString().length()<8)
         {
             Toast.makeText(this, "You'r password should be more than 8 character", Toast.LENGTH_SHORT).show();
         }
         else if(editText_password.getText().toString().matches(passwordPattern))
         {
             Toast.makeText(this, "You'r password should contain small , large character and number ", Toast.LENGTH_SHORT).show();
         }
         else if(!editText_password.getText().toString().equals(editText_confirm_password.getText().toString()))
         {
             editText_confirm_password.setError("not match the password ");
         }

         else
         {
             pDialogLoading.setTitleText("registration");
             pDialogLoading.show();
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
                    pDialogLoading.dismiss();
                    pDialogSuccess.setTitleText("registration successful");
                    pDialogSuccess.show();
                    go_to_create_account();
                }
                else {
                    pDialogLoading.dismiss();
                    pDialogerror.setTitleText("error occur");
                    pDialogerror.show();
                }
            }
        });
    }
    private void go_to_create_account()
    {

         if (driver.isChecked()) {
             Intent i=new Intent(this,create_account.class);
             i.putExtra("Email",editText_email.getText().toString());
             startActivity(i);
         }
        else if (mechanical.isChecked()) {
             Intent intent = new Intent(this, add_mechanical_data.class);
             intent.putExtra("Email",email_string);
             startActivity(intent);
        }
        else if(garage_manager.isChecked())
         {
             startActivity(new Intent(this, AddGarageManData.class));
         }
    }


}