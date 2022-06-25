package com.example.hibarking.mechanical;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hibarking.MainActivity;
import com.example.hibarking.R;
import com.example.hibarking.driver.user_account.create_account;
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

public class add_mechanical_data extends AppCompatActivity {

    CircleImageView imageView;
    EditText name , phone , nationalID;
    Uri imageUri;
    ProgressBar progressBar;
    UploadTask uploadTask;
    StorageReference storageReference;
    FirebaseFirestore db =FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    String date_of_creation, email ,currentUser_id;
    AppCompatButton create_account;
    private static final int PICK_IMAGE=1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_mechanical_data);
        initialization();
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadData();
            }
        });
    }


    private void initialization(){
        create_account=findViewById(R.id.add_mechanical_data);
        imageView = findViewById(R.id.mechanical_image);
        name = findViewById(R.id.add_mechanical_username);
        progressBar = findViewById(R.id.mec_progress);
        phone = findViewById(R.id.add_mechanical_national_phone);
        nationalID = findViewById(R.id.add_mechanical_national_id);
        date_of_creation = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        email=getIntent().getExtras().get("Email").toString();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        currentUser_id = firebaseAuth.getCurrentUser().getUid();



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
        String mec_name =name.getText().toString();
        String mec_nationalId=nationalID.getText().toString();
        String mec_phone=phone.getText().toString();

        documentReference=db.collection("User").document(currentUser_id);
        storageReference= FirebaseStorage.getInstance().getReference("Profile images");

        if(!TextUtils.isEmpty(mec_name) && !TextUtils.isEmpty(mec_nationalId) && !TextUtils.isEmpty(mec_phone) && imageUri!=null)
        {
            String imageChild =System.currentTimeMillis()+"."+getFileExt(imageUri);
            progressBar.setVisibility(View.VISIBLE);
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
                        profile.put("name",mec_name);
                        profile.put("national_id",mec_name);
                        if (downloadUri != null) {
                            profile.put("uri",downloadUri.toString());
                        }
                        profile.put("email",email);
                        profile.put("phone",mec_phone);
                        profile.put("uid",currentUser_id);
                        profile.put("image",imageChild);
                        profile.put("date",date_of_creation);


                        documentReference.set(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(add_mechanical_data.this, "Profile created", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(add_mechanical_data.this,main_mechanical.class);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(add_mechanical_data.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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