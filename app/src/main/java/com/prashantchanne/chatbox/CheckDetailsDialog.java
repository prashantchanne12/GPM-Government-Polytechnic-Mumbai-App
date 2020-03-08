package com.prashantchanne.chatbox;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CheckDetailsDialog extends DialogFragment {

    private TextView dept;
    private TextView shift;
    private TextView year;
    private TextView adYear;
    private TextView tfws;

    private TextView confirm;
    private TextView edit;

    private ProgressBar progressBar;

    private String deptText;
    private String shiftText;
    private  String yearText;
    private String currentYearText;
    private String rollNumber;
    private boolean isTfws;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_check_details_dialog,container,false);

        dept = view.findViewById(R.id.details_dept);
        shift = view.findViewById(R.id.details_shift);
        year = view.findViewById(R.id.details_year);
        adYear = view.findViewById(R.id.details_adYear);
        tfws = view.findViewById(R.id.details_tfws);

        confirm = view.findViewById(R.id.dialog_confirm_details);
        edit = view.findViewById(R.id.dialog_edit);

        progressBar = view.findViewById(R.id.progressBarDetails);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        Bundle bundle = getArguments();

        if(bundle!=null){

            deptText = bundle.getString("dept");
            shiftText = bundle.getString("shift");
            yearText = bundle.getString("year");
            currentYearText = bundle.getString("add_year");
            rollNumber = bundle.getString("temp_roll");
            isTfws = bundle.getBoolean("tfws");

        }

        dept.setText("Department : "+deptText);
        shift.setText("Shift : "+shiftText);
        year.setText("Year : "+yearText);
        adYear.setText("Admission Year : "+currentYearText);
        if(isTfws)
            tfws.setText("TFWS : YES");
        else
            tfws.setText("TFWS : NO");

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                final Map<String, Object> deptMap = new HashMap<>();
                deptMap.put("dept", deptText);
                deptMap.put("shift", shiftText);
                deptMap.put("year", yearText);
                deptMap.put("add_year", currentYearText);
                deptMap.put("temp_roll", rollNumber);
                deptMap.put("teacher",false);
                deptMap.put("tfws",isTfws);

                firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).update(deptMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        rollNumberFunction();
                        // sendToRoll();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }

    private void rollNumberFunction() {

        DocumentReference documentReference = firebaseFirestore.collection("Roll")
                .document(currentYearText) //16
                .collection(deptText)      //IF
                .document(shiftText);      //First

        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()){

                            DocumentSnapshot documentSnapshot = task.getResult();

                            if(documentSnapshot.exists()) {

                                sendToRoll();

                            }else{
                                List<String> roll = Arrays.asList("00");

                                RollNumberModel rollNumberModel = new RollNumberModel(deptText,shiftText,yearText,currentYearText,rollNumber,false,roll);

                                firebaseFirestore.collection("Roll")
                                        .document(currentYearText) //16
                                        .collection(deptText)      //IF
                                        .document(shiftText)      //First
                                        .set(rollNumberModel);

                                sendToRoll();

                            }

                        }

                    }
                });



    }

    private void sendToRoll() {
        Intent RollActivityIntent = new Intent(getContext(), RollNumber.class);
        RollActivityIntent.putExtra("ShiftText",shiftText); // FS
        RollActivityIntent.putExtra("YearText",currentYearText); //16
        RollActivityIntent.putExtra("DeptText",deptText); // IT
        RollActivityIntent.putExtra("year",yearText); //First Second Third
        RollActivityIntent.putExtra("tfws",isTfws);
        progressBar.setVisibility(View.INVISIBLE);
        startActivity(RollActivityIntent);
        getActivity().finish();

    }


}
