package com.example.hibarking.garage_manager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hibarking.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;


public class AddGarageManData extends AppCompatActivity {
    private EditText username,national_id,phone;
    private String id,email,str_image="",image_status="";
    private CircleImageView image;
    private Button send;
    private FirebaseFirestore database;
    private FirebaseAuth auth;
    private SweetAlertDialog pDialogLoading,pDialogSuccess,pDialogerror;
    private static final int PICK_IMAGE=1;
    private  Uri imageUri;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_garage_man_data);
        intialization_tool();
        sweetalert();
        create_account_method();
        select_image();
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
    private void intialization_tool()
    {
        username=findViewById(R.id.add_garage_manager_username);
        national_id=findViewById(R.id.add_manager_national_id);
        phone=findViewById(R.id.add_manager_phone);
    }
    private void create_account_method()
    {
        send=findViewById(R.id.add_garage_manager_info);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             check_data();
            }
        });
    }

    private void check_data() {
        String sub=phone.getText().toString().substring(0,3);
        if (TextUtils.isEmpty(username.getText().toString()))
        {
            username.setError("please enter you'r name");
        }
       else if (TextUtils.isEmpty(phone.getText().toString())&&phone.getText().toString().length()!=11)
        {
            phone.setError("please enter you'r correct phone");
        }
        else if (!sub.equals("010")&&!sub.equals("011")&&!sub.equals("012")&&!sub.equals("015"))
        {
            phone.setError("please enter you'r correct phone");
        }
        else if (TextUtils.isEmpty(national_id.getText().toString())||national_id.getText().toString().length()!=14) {
            national_id.setError("please enter you'r national id");
        }
        else {
            pDialogLoading.setTitleText("update garage manager data");
            pDialogLoading.show();
            auth=FirebaseAuth.getInstance();
            database=FirebaseFirestore.getInstance();
            id=auth.getCurrentUser().getUid().toString();
            email=auth.getCurrentUser().getEmail().toString();
            HashMap<String, String>map=new HashMap<String, String>();
            map.put("username",username.getText().toString());
            map.put("email",email);
            map.put("national_id",national_id.getText().toString());
            map.put("id",id);
            map.put("phone",phone.getText().toString());
            if (image_status.toString().equals("")) {

                add_to_database(map);
            }
            else {
                upload_image();
            }
        }

    }

    private void add_to_database(HashMap<String, String> map) {
        database.collection("garage_manager").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
           if (task.isSuccessful())
           {
               pDialogLoading.dismiss();
               pDialogSuccess.setTitleText("add garage manager data successful");
               pDialogSuccess.show();
               startActivity(new Intent(AddGarageManData.this, main_garage_manager.class));
           }else {
               pDialogLoading.dismiss();
               pDialogerror.setTitleText("error occur in loading data ");
               pDialogerror.show();
           }
            }
        });
    }


    private void select_image()
    {
        image=findViewById(R.id.manager_image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_IMAGE || resultCode == Activity.RESULT_OK || data != null || data.getData() != null) {
                imageUri = data.getData();
                image_status="selected";
                Picasso.get().load(imageUri).into(image);
            }
        }catch (Exception ex)
        {
            Toast.makeText(this, "error"+ex, Toast.LENGTH_SHORT).show();
        }

    }
    private void upload_image() {
        HashMap<String, String>map=new HashMap<String, String>();
        map.put("username",username.getText().toString());
        map.put("email",email);
        map.put("national_id",national_id.getText().toString());
        map.put("id",id);
        map.put("phone",phone.getText().toString());
            String image_id = UUID.randomUUID().toString();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference reference = storage.getReference("images_users/").child(image_id);
            StorageTask task = reference.putFile(imageUri);
            task.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri i = task.getResult();
                        str_image = i.toString();
                        map.put("image",str_image);
                       add_to_database(map);
                    } else {
                        pDialogLoading.dismiss();
                        pDialogerror.setTitleText("error occur in loading image ");
                        pDialogerror.show();
                    }
                }
            });
        }
    }
