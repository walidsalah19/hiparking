package com.example.hibarking.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.hibarking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ContactFragment extends Fragment {
    private EditText editText;
    private FirebaseAuth auth;
    private FirebaseFirestore database;
    private String user_id;
    private Button send;
    private SweetAlertDialog pDialogSuccess;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_contact, container, false);
        pDialogSuccess= new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
        pDialogSuccess.getProgressHelper().setBarColor(Color.parseColor("#30a852"));
        pDialogSuccess.setConfirmText("ok");
        pDialogSuccess.setConfirmClickListener(sweetAlertDialog -> {
            pDialogSuccess.dismiss();
            editText.setText("");
        });
        pDialogSuccess.setCancelable(false);
        database=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        user_id=auth.getCurrentUser().getUid().toString();
        editText=v.findViewById(R.id.text_contact_et);
        send=v.findViewById(R.id.send_contact_btn);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editText.getText().toString()))
                {
                    editText.setError("please enter you'r complaint");
                }
                else {
                    HashMap <String, String> map=new HashMap<String, String>();
                    map.put("user_id",user_id);
                    map.put("complaint",editText.getText().toString());
                    database.collection("complaintes").document(user_id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                pDialogSuccess.setTitleText("Complaint has been sent successfully");
                                pDialogSuccess.show();
                            }
                        }
                    });
                }
            }
        });

        return v;
    }
}