package com.example.hibarking.garage_manager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.hibarking.R;
import com.example.hibarking.SharedPref;
import com.example.hibarking.driver.google_map.MapsFragment;
import com.example.hibarking.garage_manager.garage_data.map;
import com.example.hibarking.garage_manager.garage_data.move_location;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class add_garage_info extends Fragment {

    private EditText garage_name,city,unit,price;
    private ImageButton location,paper;
    private FirebaseFirestore database;
    private FirebaseAuth auth;
    private String user_id,longitude,latitude,paper_str="";
    private Button add_data;
    private SweetAlertDialog pDialogLoading,pDialogSuccess,pDialogerror;
    Uri imageuri = null;
    SharedPref sharedPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(getActivity());
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            getActivity().setTheme(R.style.Theme_Dark);
        }else {
            getActivity().setTheme(R.style.Theme_Light);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_add_garage_info, container, false);
        intialization_tool(v);
        firebase_tool();
        sweetalert();
        add_location();
        add_paper();
        add_data(v);
        return v;
    }
    private void sweetalert()
    {
        //loading

        pDialogLoading = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#30a852"));
        pDialogLoading.setCancelable(false);
        //error
        pDialogerror= new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
        pDialogerror.getProgressHelper().setBarColor(Color.parseColor("#30a852"));
        pDialogerror.setConfirmText("ok");
        pDialogerror.setConfirmClickListener(sweetAlertDialog -> {
            pDialogerror.dismiss();
        });
        pDialogerror.setCancelable(false);

        //Success
        pDialogSuccess= new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
        pDialogSuccess.getProgressHelper().setBarColor(Color.parseColor("#30a852"));
        pDialogSuccess.setConfirmText("ok");
        pDialogSuccess.setConfirmClickListener(sweetAlertDialog -> {
            pDialogSuccess.dismiss();
        });
        pDialogSuccess.setCancelable(false);
    }
    private void add_data(View v) {
        add_data=v.findViewById(R.id.add_garage_info);
        add_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialogLoading.setTitleText(getString(R.string.loading));
                pDialogLoading.show();
                check_data();
            }
        });
    }

    private void check_data() {
        longitude= move_location.getLongitude();
        latitude=move_location.getLatitude();

       if (TextUtils.isEmpty(garage_name.getText().toString()))
        {
            garage_name.setError(getString(R.string.garage_name_enter));
        }
        else if (TextUtils.isEmpty(city.getText().toString()))
        {
            city.setError(getString(R.string.enter_city));
        }
        else if (TextUtils.isEmpty(unit.getText().toString()))
        {
            unit.setError(getString(R.string.units_garage));
        }
        else if (TextUtils.isEmpty(price.getText().toString()))
        {
            price.setError(getString(R.string.unit_per_hour));
        }
        else if (TextUtils.isEmpty(latitude)&& TextUtils.isEmpty(longitude))
        {
            Toast.makeText(getActivity(), R.string.select_garage_location,Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(paper_str))
        {
            Toast.makeText(getActivity(), R.string.add_garage_paper,Toast.LENGTH_LONG).show();
        }
        else
       {
           add_to_database();
       }
    }

    private void add_to_database() {
        String garage_id= UUID.randomUUID().toString();
        HashMap<String, String>map=new HashMap<String, String>();
        map.put("manager_id",user_id);
        map.put("garage_id",garage_id);
        map.put("garage_name",garage_name.getText().toString());
        map.put("unit_num",unit.getText().toString());
        map.put("hour_price",price.getText().toString());
        map.put("longitude",longitude);
        map.put("latitude",latitude);
        map.put("garage_paper",paper_str);
        map.put("city",city.getText().toString());
        database.collection("garage_request").document(garage_id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                   if (task.isSuccessful())
                   {
                       pDialogLoading.dismiss();
                       pDialogSuccess.setTitleText(getString(R.string.update_date));
                       pDialogSuccess.show();
                   }
                   else
                   {
                       pDialogLoading.dismiss();
                       pDialogerror.setTitleText(getString(R.string.error));
                       pDialogerror.show();
                   }
            }
        });
    }

    private void add_paper() {
        paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialogLoading.setTitleText("Notification : file should be pdf ");
                pDialogLoading.setCancelText("No");
                pDialogLoading.setConfirmText("Yes");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 || resultCode == Activity.RESULT_OK || data != null || data.getData() != null) {

            // Here we are initialising the progress dialog box
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Uploading");
            dialog.show();
            imageuri = data.getData();
            String file_id=UUID.randomUUID().toString();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference reference = storage.getReference("garage_paper").child(file_id+ "." + "pdf");
            StorageTask task = reference.putFile(imageuri);
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
                        dialog.dismiss();
                        Uri i = task.getResult();
                        paper_str= i.toString();
                    } else {
                        dialog.dismiss();
                       // pDialogLoading.dismiss();
                        pDialogerror.setTitleText("error occur in loading image ");
                        pDialogerror.show();
                    }
                }
            });
        }
    }
    private void add_location() {
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.grarage_manager_frameLayout, new map()).commit();
            }
        });
    }

    private void firebase_tool() {
        database=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        user_id=auth.getCurrentUser().getUid().toString();
    }

    private void intialization_tool(View v)
    {
        garage_name=v.findViewById(R.id.add_garage_username);
        city=v.findViewById(R.id.add_garage_city);
        unit=v.findViewById(R.id.add_garage_unit);
        price=v.findViewById(R.id.add_credit_price);
        location=v.findViewById(R.id.add_garage_location);
        paper=v.findViewById(R.id.add_garage_paper);

    }
}