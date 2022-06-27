package com.example.hibarking.driver.user_account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hibarking.MainActivity;
import com.example.hibarking.R;
import com.example.hibarking.SharedPref;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class create_account extends AppCompatActivity {
    EditText user_name,user_id,user_phone_number,user_license;
    CircleImageView imageView;
    Uri imageUri;
    UploadTask uploadTask;
    StorageReference storageReference;
    FirebaseDatabase database =FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    FirebaseFirestore db =FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    ProgressBar cpPar;
    private Button create_account_btn;
    String Email,date_of_creation,currentUser_id;
    create_account_class create;
    private static final int PICK_IMAGE=1;
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(this);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_Dark);
        }else {
            setTheme(R.style.Theme_Light);
        }
        setContentView(R.layout.activity_create_account);
        intialize();


    }
    private void intialize()
    {
        user_name=findViewById(R.id.create_account_username);
        user_id=findViewById(R.id.create_account_id);
        user_phone_number=findViewById(R.id.create_account_phone);
        user_license=findViewById(R.id.create_account_licence);
        create_account_btn=findViewById(R.id.create_account_btn);
        imageView = findViewById(R.id.create_account_image);
        cpPar=findViewById(R.id.cp_progress);
        create= new create_account_class();

        date_of_creation= new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        Email=getIntent().getExtras().get("Email").toString();

        FirebaseAuth auth=FirebaseAuth.getInstance();
        currentUser_id=auth.getCurrentUser().getUid();

        create_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UploadData();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE);
            }
        });

    }
    private String getFileExt(Uri uri)
    {
        ContentResolver contentResolver= getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_IMAGE || resultCode == RESULT_OK || data != null || data.getData() != null) {
                imageUri = data.getData();
                Picasso.get().load(imageUri).into(imageView);
            }
        }catch (Exception ex)
        {
            Toast.makeText(this, "error"+ex, Toast.LENGTH_SHORT).show();
        }

    }

    private void UploadData() {
        String name =user_name.getText().toString();
        String id=user_id.getText().toString();
        String license=user_license.getText().toString();
        String phone=user_phone_number.getText().toString();

        documentReference=db.collection("User").document(currentUser_id);
        storageReference= FirebaseStorage.getInstance().getReference("Profile images");
        databaseReference=database.getReference("All Users");

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(id) && !TextUtils.isEmpty(license) && !TextUtils.isEmpty(phone) && imageUri!=null)
        {
            String imageChild =System.currentTimeMillis()+"."+getFileExt(imageUri);
            cpPar.setVisibility(View.VISIBLE);
            final StorageReference reference=storageReference.child(imageChild);
            uploadTask= reference.putFile(imageUri);

            Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Uri downloadUri=task.getResult();
                        Map<String ,String> profile=new HashMap<>();
                        profile.put("name",name);
                        profile.put("id",id);
                        if (downloadUri != null) {
                            profile.put("uri",downloadUri.toString());
                        }
                        profile.put("email",Email);
                        profile.put("license",license);
                        profile.put("phone",phone);
                        profile.put("uid",currentUser_id);
                        profile.put("image",imageChild);
                        profile.put("date",date_of_creation);

                        create.setUser_name(name);
                        create.setUser_id(id);
                        create.setPhone_number(phone);
                        create.setLicense(license);
                        create.setDataofcreation(date_of_creation);
                        if (downloadUri != null) {
                            create.setImage_uri(downloadUri.toString());
                        }

                        documentReference.set(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                cpPar.setVisibility(View.INVISIBLE);
                                Toast.makeText(create_account.this, "Profile created", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(create_account.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                cpPar.setVisibility(View.INVISIBLE);
                                Toast.makeText(create_account.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        databaseReference.child(currentUser_id).setValue(create).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                cpPar.setVisibility(View.INVISIBLE);
                                Toast.makeText(create_account.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }
            });

        }else
        {
            Toast.makeText(this, "fill all fields", Toast.LENGTH_SHORT).show();
        }

    }
}