package com.example.hibarking.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hibarking.MainActivity;
import com.example.hibarking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class create_account extends AppCompatActivity {
    private EditText name,id,phone_number,license;
    private Button create_account_btn;
    private String Email,date_of_creation,app_id;
    private add_data_to_firebase add;
    private create_account_class create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        intialize();
                create_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 chick_user_data();
            }
        });
    }
    private void intialize()
    {
        name=findViewById(R.id.create_account_username);
        id=findViewById(R.id.create_account_id);
        phone_number=findViewById(R.id.create_account_phone);
        license=findViewById(R.id.create_account_licence);
        create_account_btn=findViewById(R.id.create_account_btn);
        dateofcreation();
        getemail();
        get_app_id();

    }
    private void dateofcreation()
    {
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY,MM,dd");
        date_of_creation=simpleDateFormat.format(calendar.getTime());
    }
    private void getemail()
    {
          Email=getIntent().getExtras().get("email").toString();
    }
    private void get_app_id()
    {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        app_id=auth.getCurrentUser().getUid();
    }
    private void chick_user_data()
    {
        if(name.getText().toString()==null||id.getText().toString()==null||phone_number.getText().toString()==null||license.getText().toString()==null)
        {
            Toast.makeText(this, "Please Enter You'r data correctly", Toast.LENGTH_SHORT).show();
        }
        else{
            create=new create_account_class(name.getText().toString(),phone_number.getText().toString(),app_id
            ,license.getText().toString(),id.getText().toString(),Email,date_of_creation);
            new add_data_to_firebase(create,create_account.this);
        }
    }
}