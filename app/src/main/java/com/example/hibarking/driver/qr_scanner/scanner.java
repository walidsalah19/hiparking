package com.example.hibarking.driver.qr_scanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.hibarking.R;
import com.example.hibarking.data_class.garage_rate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.Result;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

public class scanner extends Fragment {
    private SweetAlertDialog pDialogLoading,pDialogSuccess,pDialogerror;
    private CodeScanner mCodeScanner;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private String user_id,booking_id,garage_id;
    private FirebaseFirestore database;
    private HashMap<String, String> dataset=new HashMap<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_scanner, container, false);
       permassion();
       sweetalert();
        CodeScannerView scannerView = root.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(getActivity(), scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       pDialogLoading.setTitleText("loading..");
                        pDialogLoading.show();

                        sure_book(result.getText());
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
        return root;
    }
     private void sure_book(String s)
     {


         FirebaseAuth auth=FirebaseAuth.getInstance();
         user_id=auth.getCurrentUser().getUid().toString();
         database= FirebaseFirestore.getInstance();
         database.collection("booking").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
             @Override
             public void onComplete(@NonNull Task<QuerySnapshot> task) {
                 if (task.isSuccessful()) {
                     boolean found=false;
                     for (QueryDocumentSnapshot document : task.getResult()) {
                         String id=document.get("id").toString();
                         String state=document.get("status").toString();
                         booking_id=document.get("id").toString();
                         if (s.equals(id)&&state.equals("not arrived"))
                         {
                             dataset.put("duration",document.get("duration").toString());
                             dataset.put("id",document.get("id").toString());
                             dataset.put("name",document.get("name").toString());
                             dataset.put("date",document.get("date").toString());
                             dataset.put("time",document.get("time").toString());
                             dataset.put("garage_id",document.get("garage_id").toString());
                             dataset.put("status","arrived");
                             dataset.put("arrival_time","0");
                             dataset.put("booking_id",booking_id);
                             found=true;
                             break;
                         }
                         else if(s.equals(id)&&state.equals("arrived"))
                         {
                             garage_id=document.get("garage_id").toString();
                             delete_booking();
                             break;
                         }
                     }
                     if (found)
                     {
                        add_update_booking();
                     }
                     else
                     {
                         pDialogLoading.dismiss();
                         pDialogerror.setTitleText("this isn't you'r parking");
                         pDialogerror.show();
                     }
                 }
             }
         });
     }

    private void delete_booking() {
        database.collection("booking").document(booking_id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              if (task.isSuccessful())
              {
                  pDialogLoading.dismiss();
                  pDialogSuccess.setTitleText("you can leave now");
                  pDialogSuccess.show();
                  rating_method();
              }
            }
        });
    }

    private void rating_method() {
        View v=getLayoutInflater().inflate(R.layout.fragment_rating,null);
        new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme)
                .setTitle("Rating")
                .setView(v)
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RatingBar r=(RatingBar) v.findViewById(R.id.ratingBar);
                        add_rating_database(r.getRating());
                    }
                })
                .create()
                .show();
    }

    private void add_rating_database(float rating) {
        garage_rate rate=new garage_rate(rating,garage_id);
        database.collection("garage_rate").add(rate).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                   if (task.isSuccessful())
                   {
                       pDialogSuccess.setTitleText("Successfully evaluated");
                       pDialogSuccess.show();
                   }
            }
        });
    }

    private void add_update_booking() {
        database.collection("booking").document(booking_id).set(dataset).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        pDialogLoading.dismiss();
                        pDialogSuccess.setTitleText("this is you'r parking");
                        pDialogSuccess.show();
                    }
            }
        });
    }

    private void sweetalert()
    {
        //loading

        pDialogLoading = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setCancelable(false);

        //error
        pDialogerror= new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
        pDialogerror.setConfirmText(getString(R.string.dialog_ok));
        pDialogerror.setConfirmClickListener(sweetAlertDialog -> {
            pDialogerror.dismiss();
        });
        pDialogerror.setCancelable(true);

        //Success
        pDialogSuccess= new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
        pDialogSuccess.setConfirmText(getString(R.string.dialog_ok));
        pDialogSuccess.setConfirmClickListener(sweetAlertDialog -> {
            pDialogSuccess.dismiss();
        });
        pDialogSuccess.setCancelable(true);
    }
    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
    private void permassion()
    {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)) {


            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_PERMISSION_CODE);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getActivity(), "camera permission granted", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getActivity(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}