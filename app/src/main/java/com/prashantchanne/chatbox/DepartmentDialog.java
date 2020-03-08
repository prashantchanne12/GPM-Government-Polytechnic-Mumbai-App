package com.prashantchanne.chatbox;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DepartmentDialog extends DialogFragment {

    private CheckBox all,it,me,ce,co,ee,is,ec;
    private TextView confirm,cancel;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String title;
    private String desc;
    private String userName;


    private ProgressBar progressBar;

    private String departments = "";

    private int count = 1;

    private ArrayList<String> arrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_department_dialog, container, false);

        /*

        ViewGroup - ViewGroup is a special view that contains other views
                    Basically it holds views
                    ex - LinearLayout ConstraintLayout etc.

        View - all UI components
               Buttons,texts etc

         */

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        all = view.findViewById(R.id.allDeptCheck);
        it = view.findViewById(R.id.itCheck);
        me = view.findViewById(R.id.meCheck);
        ce = view.findViewById(R.id.ceCheck);
        co = view.findViewById(R.id.coCheck);
        ee = view.findViewById(R.id.eeCheck);
        is = view.findViewById(R.id.isCheck);
        ec = view.findViewById(R.id.ecCheck);

        confirm = view.findViewById(R.id.dialogConfirm_notice);
        cancel = view.findViewById(R.id.dialogCancel_notice);

        progressBar = view.findViewById(R.id.progressBarDialog);

        arrayList = new ArrayList<>();

        Bundle bundle = getArguments();

        if(bundle!=null) {
            title = bundle.getString("title");
            desc = bundle.getString("desc");
            userName = bundle.getString("userName");
        }

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(all.isChecked()){


                    it.setChecked(true);
                    me.setChecked(true);
                    ce.setChecked(true);
                    co.setChecked(true);
                    ee.setChecked(true);
                    is.setChecked(true);
                    ec.setChecked(true);
                }else{

                    it.setChecked(false);
                    me.setChecked(false);
                    ce.setChecked(false);
                    co.setChecked(false);
                    ee.setChecked(false);
                    is.setChecked(false);
                    ec.setChecked(false);
                }
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                if(all.isChecked()){
                    departments = "All Depts";
                    gotoAllDept(title,desc,userName,departments);
                    return;
                }


                if(it.isChecked()){
                    departments = "IT";
                    sendNoticeToParticularDepartments(title,desc,userName,"IT",departments);
                }

                if(co.isChecked()){
                    departments = "CO";
                    sendNoticeToParticularDepartments(title,desc,userName,"CO",departments);
                }

                if(me.isChecked()){
                    departments = "ME";
                    sendNoticeToParticularDepartments(title,desc,userName,"ME",departments);
                }

                if(ce.isChecked()){
                    departments = "CE";
                    sendNoticeToParticularDepartments(title,desc,userName,"CE",departments);
                }

                if(ee.isChecked()){
                    departments = "EE";
                    sendNoticeToParticularDepartments(title,desc,userName,"EE",departments);
                }

                if(is.isChecked()){
                    departments = "IS";
                    sendNoticeToParticularDepartments(title,desc,userName,"IS",departments);
                }

                if(ec.isChecked()){
                    departments = "EC";
                    sendNoticeToParticularDepartments(title,desc,userName,"EC",departments);
                }

                progressBar.setVisibility(View.INVISIBLE);

                getDialog().dismiss();
                Toast.makeText(getContext(), "Notice Posted", Toast.LENGTH_SHORT).show();


            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    private void sendNoticeToParticularDepartments(final String title, final String desc, final String userName, String dept, final String departments) {

            firebaseFirestore.collection("Users")
                    .whereEqualTo("dept",dept)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(DocumentSnapshot doc: queryDocumentSnapshots){

                                firebaseFirestore.collection("Users").document(doc.getId()).collection("Notice")
                                        .add(new NoticeModel(title, desc,null,userName,firebaseAuth.getCurrentUser().getUid(),false))
                                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {

                                                if(task.isSuccessful()){

                                                    if(count==1) {
                                                        count++;

                                                        firebaseFirestore.collection("Users")
                                                                .document(firebaseAuth.getCurrentUser().getUid())
                                                                .collection("Notice")
                                                                .add(new NoticeModel(title, desc, null, departments, firebaseAuth.getCurrentUser().getUid(),true));
                                                    }
                                                }else{
                                                    Toast.makeText(getContext(), "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //  progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getContext(), "Failed to post "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                    });

    }


    private void gotoAllDept(final String title, final String desc, final String userName,final String departments) {

        firebaseFirestore.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(DocumentSnapshot doc: queryDocumentSnapshots){

                    firebaseFirestore.collection("Users").document(doc.getId()).collection("Notice")
                            .add(new NoticeModel(title, desc,null,userName,firebaseAuth.getCurrentUser().getUid(),false))
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {

                                    if(task.isSuccessful()){

                                        if(count==1) {
                                            count++;

                                            firebaseFirestore.collection("Users")
                                                    .document(firebaseAuth.getCurrentUser().getUid())
                                                    .collection("Notice")
                                                    .add(new NoticeModel(title, desc, null, departments, firebaseAuth.getCurrentUser().getUid(),true));
                                        }

                                    }else{
                                        Toast.makeText(getContext(), "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                          //  progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getContext(), "Failed to post "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                Toast.makeText(getContext(), "Notice Posted!", Toast.LENGTH_SHORT).show();
                //progressBar.setVisibility(View.INVISIBLE);

            }
        });
    }
}
