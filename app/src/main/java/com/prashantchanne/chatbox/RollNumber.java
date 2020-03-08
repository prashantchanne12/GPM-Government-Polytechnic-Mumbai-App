package com.prashantchanne.chatbox;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.StatusHints;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RollNumber extends AppCompatActivity {

    private EditText rollNumber;
    private Button submit;
    private TextView departMentTextView;
    private TextView shifTextView;
    private TextView yearTextView;
    private Spinner yearSpinner;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    String Department;
    String Shift;
    String addYear;
    String rollNumberId;
    String Year;
    String tempShift;
    String tempDept;

    boolean tfws;

    private String currentUser;
    String temp;
    int count = 0;

    Map<String, Object> rollNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_number);

        rollNumber = findViewById(R.id.rollNumberTextView);
        submit = findViewById(R.id.submitButton);
        submit.setEnabled(false);
        departMentTextView = findViewById(R.id.deptText);
        shifTextView = findViewById(R.id.shiftText);
        yearTextView = findViewById(R.id.yearText);
        //yearSpinner = findViewById(R.id.rollYearSpinner);

        progressBar = findViewById(R.id.rollNumberProgress);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Shift = getIntent().getStringExtra("ShiftText"); // First Second
        tempShift = Shift;
        addYear = getIntent().getStringExtra("YearText"); //16
        Department = getIntent().getStringExtra("DeptText"); //IT
        tempDept = Department;
        Year = getIntent().getStringExtra("year"); //First Second Third

        tfws = getIntent().getExtras().getBoolean("tfws");

        if(Department.equals("IT")){
            Department = "IF";
        }

        if(Shift.equals("First")){
            Shift = "FS";
        }
        else{
            Shift = "SS";
        }

        if(tfws){
            Shift = "FW";
        }

        rollNumberId = Shift + addYear + Department;

        //rollNum = new HashMap<>();

        currentUser = firebaseAuth.getCurrentUser().getUid();

        departMentTextView.setText(Department);
        shifTextView.setText(Shift);
        yearTextView.setText(addYear);

        rollNumber.setText("0");


        rollNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.toString().trim().length() == 3){
                    submit.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               // Toast.makeText(RollNumber.this, addYear+"\n"+tempDept+"\n"+tempShift, Toast.LENGTH_SHORT).show();

                final String currentUser = firebaseAuth.getCurrentUser().getUid();
                String rollNumberText = rollNumber.getText().toString().trim();

                final String finalRollNumber = Shift + addYear + Department + rollNumberText;
                //rollNum.put("roll",finalRollNumber);


                firebaseFirestore.collection("Roll")
                        .document(addYear)      //16
                        .collection(tempDept) //IT
                        .document(tempShift)    //First
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                RollNumberModel rollNumberModel = documentSnapshot.toObject(RollNumberModel.class);

                                for(String rolls: rollNumberModel.getRoll()) {

                                    if (finalRollNumber.equals(rolls)) {
                                        temp = rolls;
                                        Toast.makeText(RollNumber.this, "Already exits!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                if(finalRollNumber.equals(temp)){
                                    Toast.makeText(RollNumber.this, "Already Exits Can't Add", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    addRollNumber(finalRollNumber,currentUser);
                                    updateArray(finalRollNumber,Department);
                                    sendToMain();
                                }


                            }
                        });




            }
        });



    }

    //Array
    private void updateArray(String finalRollNumber,String Department) {


        firebaseFirestore.collection("Roll")
                .document(addYear)      //16
                .collection(tempDept) //IT
                .document(tempShift)    //First
                .update("roll", FieldValue.arrayUnion(finalRollNumber))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                       // Toast.makeText(RollNumber.this, "Array Updated!", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(RollNumber.this, "Error while Updating! "+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }






    //FS16IF012 adding to Users collection
    private void addRollNumber(String finalRollNumber,String currentUser) {

        firebaseFirestore.collection("Users")
                .document(currentUser)
                .update("roll",finalRollNumber)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                //Toast.makeText(RollNumber.this, "Roll number Added Successfully", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(RollNumber.this, "Error! "+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void sendToMain() {
        firebaseAuth.signOut();
        Intent intent = new Intent(RollNumber.this,LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Account is Created!\n Sign in", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {


        final AlertDialog.Builder builder = new AlertDialog.Builder(RollNumber.this);
        builder.setMessage("Please fill the details");
        builder.setCancelable(true);


        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });



        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }



}
