package com.example.hibarking.mechanical;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import com.example.hibarking.R;
import com.example.hibarking.garage_manager.garage_data.map;
import com.example.hibarking.garage_manager.garage_data.move_location;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class add_mechanical_data extends AppCompatActivity {

    CircleImageView imageView;
    EditText name , phone , nationalID;
    Uri imageUri,imageuri;
    ProgressBar progressBar;
    UploadTask uploadTask;
    StorageReference storageReference;
    FirebaseFirestore db =FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    SweetAlertDialog pDialogLoading , pDialogerror,pDialogSuccess;
    String date_of_creation, email ,currentUser_id ,paper_str, gg="";
    AppCompatButton create_account;
    String longitude, latitude;
    AppCompatImageButton paper , location;
    private static final int PICK_IMAGE=22;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_mechanical_data);
        initialization();
        add_location();
        add_paper();
        sweetalert();
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
        paper = findViewById(R.id.add_mechanical_licence_paper);
        location = findViewById(R.id.add_mechanical_location);
        date_of_creation = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        email=getIntent().getExtras().get("Email").toString();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        currentUser_id = firebaseAuth.getCurrentUser().getUid();



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gg="im";
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
            if (gg.equals("im") && requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageUri = data.getData();
                Picasso.get().load(imageUri).into(imageView);
            }else if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

                // Here we are initialising the progress dialog box
                dialog = new ProgressDialog(this);
                dialog.setMessage("Uploading");
                dialog.show();
                imageuri = data.getData();
                StorageReference storageReference = FirebaseStorage.getInstance().getReference("Mechanical_paper");
                String file_id= UUID.randomUUID().toString();
                final StorageReference filepath = storageReference.child(file_id + "." + "pdf");
                filepath.putFile(imageuri).continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            // After uploading is done it progress
                            // dialog box will be dismissed
                            dialog.dismiss();
                            Uri uri = task.getResult();
                            paper_str= uri.toString();

                            pDialogSuccess.setTitleText("successful upload file ");
                            pDialogSuccess.show();

                        } else {
                            dialog.dismiss();
                            pDialogerror.setTitleText("Failed to upload file ");
                        }
                    }
                });
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
        String sub=phone.getText().toString().substring(0,3);
        if (TextUtils.isEmpty(phone.getText().toString())&&phone.getText().toString().length()!=11)
        {
            phone.setError("please enter correct phone number");
        }
        else if (!sub.equals("010")&&!sub.equals("011")&&!sub.equals("012")&&!sub.equals("015"))
        {
            phone.setError("please enter correct phone number");
        }
        else if (TextUtils.isEmpty(nationalID.getText().toString())||nationalID.getText().toString().length()!=14) {
            nationalID.setError("please enter your national id");
        }
        longitude = move_location.getLongitude();
        latitude = move_location.getLatitude();

        documentReference=db.collection("Mechanical").document(currentUser_id);
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
                        profile.put("national_id",mec_nationalId);
                        if (downloadUri != null) {
                            profile.put("uri",downloadUri.toString());
                        }else {
                            profile.put("uri","null");
                        }
                        profile.put("longitude",longitude);
                        profile.put("latitude",latitude);
                        profile.put("email",email);
                        profile.put("paper",paper_str);
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
    private void add_paper() {
        paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialogLoading.setTitleText("Notification : file should be pdf ");
                pDialogLoading.setCancelText("No");
                pDialogLoading.setConfirmText("yes");
                pDialogLoading.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        pDialogLoading.dismiss();
                    }
                });
                pDialogLoading.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        pDialogLoading.dismiss();
                        Intent galleryIntent = new Intent();

                        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                        // We will be redirected to choose pdf
                        galleryIntent.setType("application/pdf");
                        startActivityForResult(galleryIntent, 1);
                    }
                });
                pDialogLoading.show();

            }
        });
    }
    ProgressDialog dialog;
    private void add_location() {
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().add(R.id.frame_map, new map()).commit();
            }
        });
    }

    private void sweetalert()
    {
        //loading
        pDialogLoading = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#30a852"));
        pDialogLoading.setCancelable(true);
        //error
        pDialogerror= new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        pDialogerror.getProgressHelper().setBarColor(Color.parseColor("#30a852"));
        pDialogerror.setConfirmText("ok");
        pDialogerror.setConfirmClickListener(sweetAlertDialog -> {
            pDialogerror.dismiss();
        });
        pDialogerror.setCancelable(true);

        //Success
        pDialogSuccess= new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        pDialogSuccess.getProgressHelper().setBarColor(Color.parseColor("#30a852"));
        pDialogSuccess.setConfirmText("ok");
        pDialogSuccess.setConfirmClickListener(sweetAlertDialog -> {
            pDialogSuccess.dismiss();
        });
        pDialogSuccess.setCancelable(true);
    }

}