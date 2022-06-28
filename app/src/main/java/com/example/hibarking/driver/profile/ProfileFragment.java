package com.example.hibarking.driver.profile;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hibarking.MainActivity;
import com.example.hibarking.R;
import com.example.hibarking.driver.user_account.create_account;
import com.example.hibarking.driver.user_account.create_account_class;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hibarking.R;

public class ProfileFragment extends Fragment {


    CircleImageView imageView;
    EditText name ,id,license ,date ,phone,email ,card;
    ImageButton editProfile;
    Uri imageUri;
    CardView cardView;
    UploadTask uploadTask;
    String UrlDeleted;
    ProgressBar progressBar;
    StorageReference storageReference;
    FirebaseDatabase database =FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    FirebaseFirestore db =FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    String currentUser_id , Email,DATE;
    AppCompatButton btuSave;
    create_account_class create;
    private static final int PICK_IMAGE=1;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = getActivity().findViewById(R.id.image_pro);
        cardView = getActivity().findViewById(R.id.add_image);
        editProfile = getActivity().findViewById(R.id.image_edit_btn);
        name = getActivity().findViewById(R.id.pr_name);
        id =getActivity().findViewById(R.id.pr_id);
        license = getActivity().findViewById(R.id.pr_license);
        date = getActivity().findViewById(R.id.pr_date);
        phone = getActivity().findViewById(R.id.pr_web);
        email= getActivity().findViewById(R.id.pr_email);
        card = getActivity().findViewById(R.id.pr_card);
        btuSave = getActivity().findViewById(R.id.btu_save);
        progressBar = getActivity().findViewById(R.id.update_progress);
        create= new create_account_class();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardView.setVisibility(View.VISIBLE);
                imageView.setClickable(true);
                name.setEnabled(true);
                phone.setEnabled(true);
                license.setEnabled(true);
                card.setEnabled(true);
                btuSave.setVisibility(View.VISIBLE);
            }
        });
        btuSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile();
            }
        });
        cardView.setOnClickListener(new View.OnClickListener() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
    public void editProfile(){
        FirebaseAuth auth=FirebaseAuth.getInstance();
        currentUser_id=auth.getCurrentUser().getUid();

        documentReference=db.collection("User").document(currentUser_id);
        storageReference= FirebaseStorage.getInstance().getReference("Profile images");
        databaseReference=database.getReference("All Users");

        if(!TextUtils.isEmpty(name.getText().toString()) && !TextUtils.isEmpty(card.getText().toString()) && !TextUtils.isEmpty(license.getText().toString()) && !TextUtils.isEmpty(phone.getText().toString()) && imageUri!=null)
        {
            String image_current = System.currentTimeMillis()+"."+getFileExt(imageUri);
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference reference=storageReference.child(image_current);
            uploadTask= reference.putFile(imageUri);

            final StorageReference reference2=storageReference.child(UrlDeleted);
            reference2.delete();

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
                        profile.put("name",name.getText().toString());
                        profile.put("id",card.getText().toString());
                        if (downloadUri != null) {
                            profile.put("uri",downloadUri.toString());
                        }
                        profile.put("email",Email);
                        profile.put("license",license.getText().toString());
                        profile.put("phone",phone.getText().toString());
                        profile.put("uid",currentUser_id);
                        profile.put("image",image_current);
                        profile.put("date",DATE);

                        create.setUser_name(name.getText().toString());
                        create.setUser_id(card.getText().toString());
                        create.setPhone_number(phone.getText().toString());
                        create.setLicense(license.getText().toString());
                        create.setDataofcreation(DATE);
                        if (downloadUri != null) {
                            create.setImage_uri(downloadUri.toString());
                        }

                        documentReference.set(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getContext(), R.string.update_profile, Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(getContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        databaseReference.child(currentUser_id).setValue(create).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }
            });

        }else
        {
            Toast.makeText(getContext(), R.string.fill_all, Toast.LENGTH_SHORT).show();
        }




    }
    private String getFileExt(Uri uri)
    {
        ContentResolver contentResolver= getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_IMAGE || resultCode == Activity.RESULT_OK || data != null || data.getData() != null) {
                imageUri = data.getData();
                create.setImage_uri(imageUri.toString()); 
                Picasso.get().load(imageUri).into(imageView);
            }
        }catch (Exception ex)
        {
            Toast.makeText(getContext(), "error"+ex, Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId= null;
        if (user != null) {
            currentUserId = user.getUid();
        }
        DocumentReference reference;
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();

        if (currentUserId != null) {
            reference = firestore.collection("User").document(currentUserId);
            reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    try {
                        if (task.getResult().exists()) {
                            String names = task.getResult().getString("name");
                            String ids = task.getResult().getString("id");
                            String licences = task.getResult().getString("license");
                            String phones = task.getResult().getString("phone");
                            String dates = task.getResult().getString("date");
                            String urls = task.getResult().getString("uri");
                            String emails = task.getResult().getString("email");

                            ////
                            UrlDeleted = task.getResult().getString("image");
                            Email =task.getResult().getString("email");
                            DATE = task.getResult().getString("date");

                            /////
                            Picasso.get().load(urls).into(imageView);
                            name.setText(names);
                            id.setText(ids);
                            email.setText(emails);
                            license.setText(licences);
                            phone.setText(phones);
                            date.setText(dates);
                            card.setText(ids);


                        } else {
                            Intent intent = new Intent(getActivity(), create_account.class);
                            startActivity(intent);
                        }
                    } catch (NullPointerException nullPointerException) {
                        Toast.makeText(getActivity(), "" + nullPointerException.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            getActivity().finish();
            System.exit(0);
        }
    }
}