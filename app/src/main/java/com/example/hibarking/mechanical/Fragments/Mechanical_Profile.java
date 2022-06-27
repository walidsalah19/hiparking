package com.example.hibarking.mechanical.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.hibarking.R;
import com.example.hibarking.driver.user_account.create_account;
import com.example.hibarking.garage_manager.garage_data.map;
import com.example.hibarking.garage_manager.garage_data.move_location;
import com.example.hibarking.mechanical.add_mechanical_data;
import com.example.hibarking.mechanical.main_mechanical;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class Mechanical_Profile extends Fragment {

    CircleImageView imageView;
    Uri imageUri,imageuri;
    UploadTask uploadTask;
    EditText id , name , email , phone , date ;
    FrameLayout paper, location, paperEdit,locationEdit ;
    ImageButton edit;
    CardView cardView;
    ProgressBar progressBar;
    StorageReference storageReference;
    FirebaseFirestore db =FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    SweetAlertDialog pDialogLoading , pDialogerror,pDialogSuccess;
    private static final int PICK_IMAGE=22;
    AppCompatButton save;
    String currentUser_id , Email,DATE,UrlDeleted, longitude,latitude,UrlPaper,paper_str, gg="";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        initialization();
        getDateMechanical();
        sweetalert();
        show_location();
        download_file();


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setClickable(true);
                name.setEnabled(true);
                cardView.setVisibility(View.VISIBLE);
                phone.setEnabled(true);
                paper.setVisibility(View.GONE);
                location.setVisibility(View.GONE);
                paperEdit.setVisibility(View.VISIBLE);
                locationEdit.setVisibility(View.VISIBLE);
                save.setVisibility(View.VISIBLE);
            }
        });
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
        paperEdit.setOnClickListener(new View.OnClickListener() {
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
        locationEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.machanical_framelayout, new map()).commit();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditData();
            }
        });
    }

    public void initialization(){
        imageView = getActivity().findViewById(R.id.image_mechanical);
        id = getActivity().findViewById(R.id.mec_id_pr);
        name =getActivity().findViewById(R.id.mec_name_pr);
        email = getActivity().findViewById(R.id.mec_email_pr);
        phone = getActivity().findViewById(R.id.mec_phone_pr);
        date = getActivity().findViewById(R.id.mec_date_pr);
        paper = getActivity().findViewById(R.id.mec_paper_pr);
        paperEdit = getActivity().findViewById(R.id.mec_paper_pr_edit);
        save = getActivity().findViewById(R.id.btu_save_pr);
        cardView = getActivity().findViewById(R.id.add_image_mec);
        location = getActivity().findViewById(R.id.mec_location_pr);
        locationEdit = getActivity().findViewById(R.id.mec_location_pr_edit);
        edit = getActivity().findViewById(R.id.image_edit_btn_mec_pr);
        progressBar= getActivity().findViewById(R.id.update_progress_pr);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        currentUser_id = firebaseAuth.getCurrentUser().getUid();


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mechanical_profile, container, false);
    }

  public void getDateMechanical(){
      FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
      String currentUserId= null;
      if (user != null) {
          currentUserId = user.getUid();
      }
      DocumentReference reference;
      FirebaseFirestore fireStore=FirebaseFirestore.getInstance();

      if (currentUserId != null) {
          reference = fireStore.collection("Mechanical").document(currentUserId);
          reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
              @Override
              public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                  try {
                      if (task.getResult().exists()) {
                          String names = task.getResult().getString("name");
                          String ids = task.getResult().getString("national_id");
                          String phones = task.getResult().getString("phone");
                          String dates = task.getResult().getString("date");
                          String urls = task.getResult().getString("uri");
                          String emails = task.getResult().getString("email");
                           longitude = task.getResult().getString("longitude");
                           latitude = task.getResult().getString("latitude");
                           UrlPaper = task.getResult().getString("paper");

                          ////
                          UrlDeleted = task.getResult().getString("image");
                          Email =task.getResult().getString("email");
                          DATE = task.getResult().getString("date");

                          /////
                          if(!urls.equals("null"))
                                Picasso.get().load(urls).into(imageView);
                          else {
                              imageView.setImageResource(R.drawable.profile_image);
                          }
                          name.setText(names);
                          id.setText(ids);
                          email.setText(emails);
                          phone.setText(phones);
                          date.setText(dates);


                      } else {
                          Intent intent = new Intent(getActivity(), add_mechanical_data.class);
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

    private void show_location() {
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move_location.setLatitude(latitude);
                move_location.setLongitude(longitude);
                move_location.setType("shaw");
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.machanical_framelayout, new map()).addToBackStack(null).commit();
            }
        });
    }

    private void download_file() {
        paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UrlPaper!=null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(UrlPaper));
                    getActivity().startActivity(intent);
                }
            }
        });
    }

    private void sweetalert()
    {
        //loading
        pDialogLoading = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#30a852"));
        pDialogLoading.setCancelable(true);
        //error
        pDialogerror= new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
        pDialogerror.getProgressHelper().setBarColor(Color.parseColor("#30a852"));
        pDialogerror.setConfirmText("ok");
        pDialogerror.setConfirmClickListener(sweetAlertDialog -> {
            pDialogerror.dismiss();
        });
        pDialogerror.setCancelable(true);

        //Success
        pDialogSuccess= new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
        pDialogSuccess.getProgressHelper().setBarColor(Color.parseColor("#30a852"));
        pDialogSuccess.setConfirmText("ok");
        pDialogSuccess.setConfirmClickListener(sweetAlertDialog -> {
            pDialogSuccess.dismiss();
        });
        pDialogSuccess.setCancelable(true);
    }

    private String getFileExt(Uri uri)
    {
        ContentResolver contentResolver= getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    ProgressDialog dialog;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (gg.equals("im") && requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageUri = data.getData();
                Picasso.get().load(imageUri).into(imageView);
            }else if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

                // Here we are initialising the progress dialog box
                dialog = new ProgressDialog(getContext());
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
            Toast.makeText(getContext(), "error"+ex, Toast.LENGTH_SHORT).show();
        }

    }
    private void EditData() {
        String mec_name =name.getText().toString();
        String mec_phone=phone.getText().toString();
        longitude = move_location.getLongitude();
        latitude = move_location.getLatitude();
        String sub=phone.getText().toString().substring(0,3);
        if (TextUtils.isEmpty(phone.getText().toString())&&phone.getText().toString().length()!=11)
        {
            phone.setError("please enter correct phone number");
        }
        else if (!sub.equals("010")&&!sub.equals("011")&&!sub.equals("012")&&!sub.equals("015"))
        {
            phone.setError("please enter correct phone number");
        }
        documentReference=db.collection("Mechanical").document(currentUser_id);
        storageReference= FirebaseStorage.getInstance().getReference("Profile images");

        if(!TextUtils.isEmpty(mec_name) &&!TextUtils.isEmpty(longitude) &&!TextUtils.isEmpty(latitude) &&  !TextUtils.isEmpty(mec_phone) && imageUri!=null )
        {
            String imageChild =System.currentTimeMillis()+"."+getFileExt(imageUri);
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference reference=storageReference.child(imageChild);
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
                        profile.put("name",mec_name);
                        profile.put("national_id",id.getText().toString());
                        if (downloadUri != null) {
                            profile.put("uri",downloadUri.toString());
                        }else {
                            profile.put("uri","null");
                        }
                        profile.put("longitude",longitude);
                        profile.put("latitude",latitude);
                        profile.put("email",Email);
                        profile.put("paper",paper_str);
                        profile.put("phone",mec_phone);
                        profile.put("uid",currentUser_id);
                        profile.put("image",imageChild);
                        profile.put("date",DATE);


                        documentReference.set(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressBar.setVisibility(View.INVISIBLE);
                                imageView.setClickable(false);
                                name.setEnabled(false);
                                phone.setEnabled(false);
                                paper.setVisibility(View.VISIBLE);
                                location.setVisibility(View.VISIBLE);
                                paperEdit.setVisibility(View.GONE);
                                cardView.setVisibility(View.GONE);
                                locationEdit.setVisibility(View.GONE);
                                save.setVisibility(View.GONE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
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
            Toast.makeText(getContext(), "fill all fields", Toast.LENGTH_SHORT).show();
        }

    }

}