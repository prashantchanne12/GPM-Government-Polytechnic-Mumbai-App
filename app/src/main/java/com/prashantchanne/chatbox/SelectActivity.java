package com.prashantchanne.chatbox;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectActivity extends AppCompatActivity {

    private Spinner categorySpinner;
    private Spinner shiftSpinner;
    private Spinner yearSpinner;
    private Spinner yearTextSpinnner;

    private CheckBox tfws;

    private Button selectButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String currentUser;

    private ProgressBar progressBar;

    String deptText;
    String shiftText;
    String yearText;
    String currentYearText;

    String rollNumber;

    boolean isTfws = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        shiftSpinner = findViewById(R.id.spinnerT);
        yearSpinner = findViewById(R.id.spinnerYear);
        yearTextSpinnner = findViewById(R.id.spinnerYearText);

        tfws = findViewById(R.id.tfwsCheckBox);

        selectButton = findViewById(R.id.selectButton);
        categorySpinner = findViewById(R.id.spinnerSelect);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

         currentUser = firebaseAuth.getCurrentUser().getUid();
         progressBar = findViewById(R.id.Progress);

        //------------ Select Department -----------//
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SelectActivity.this,
               android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.names));

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(arrayAdapter);


        //------------ Select Shift -----------//
        ArrayAdapter<String> shiftArrayAdapter = new ArrayAdapter<>(SelectActivity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.shifts));

        shiftArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        shiftSpinner.setAdapter(shiftArrayAdapter);

        //------------ Select Year -----------//
        ArrayAdapter<String> yearArrayAdapter = new ArrayAdapter<>(SelectActivity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.year));

        yearArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        yearSpinner.setAdapter(yearArrayAdapter);

        //------------ Select Admission Year -----------//
        ArrayAdapter<String> yearTextArrayAdapter = new ArrayAdapter<>(SelectActivity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.yearText));

        yearTextArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        yearTextSpinnner.setAdapter(yearTextArrayAdapter);


                selectButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (tfws.isChecked()){
                            isTfws = true;
                        }

                        deptText = categorySpinner.getSelectedItem().toString();
                        shiftText = shiftSpinner.getSelectedItem().toString();
                        yearText = yearSpinner.getSelectedItem().toString();
                        currentYearText = yearTextSpinnner.getSelectedItem().toString();

                        String shift, dept = "";

                        if (deptText.equals("IT")) {
                            dept = "IF";
                        }

                        if (shiftText.equals("First")) {
                            shift = "FS";
                        } else {
                            shift = "SS";
                        }

                        rollNumber = shift + currentYearText + dept;

                        Toast.makeText(SelectActivity.this, "Roll " + rollNumber, Toast.LENGTH_SHORT).show();


                        if (deptText.contentEquals("Select your Department")
                                || shiftText.contentEquals("Select your Shift")
                                || yearText.contentEquals("Select your Year")
                                || currentYearText.contentEquals("Select your admission year")) {

                            Toast.makeText(SelectActivity.this, "Please select all the fields", Toast.LENGTH_SHORT).show();
                            return;

                        } else {


                            CheckDetailsDialog dialog = new CheckDetailsDialog();
                            Bundle bundle = new Bundle();
                            bundle.putString("dept", deptText);
                            bundle.putString("shift", shiftText);
                            bundle.putString("year", yearText);
                            bundle.putString("add_year", currentYearText);
                            bundle.putString("temp_roll", rollNumber);
                            bundle.putBoolean("teacher", false);
                            bundle.putBoolean("tfws", isTfws);
                            dialog.setArguments(bundle);

                            dialog.show(getSupportFragmentManager(), "dialog_check_details");


                            // progressBar.setVisibility(View.VISIBLE);


                            //Toast.makeText(SelectActivity.this, "Selected! " + deptText, Toast.LENGTH_SHORT).show();

                           /* final Map<String, Object> deptMap = new HashMap<>();
                            deptMap.put("dept", deptText);
                            deptMap.put("shift", shiftText);
                            deptMap.put("year", yearText);
                            deptMap.put("add_year", currentYearText);
                            deptMap.put("temp_roll", rollNumber);
                            deptMap.put("teacher",false);
                            deptMap.put("tfws",isTfws);

                            firebaseFirestore.collection("Users").document(currentUser).update(deptMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    progressBar.setVisibility(View.INVISIBLE);
                                    rollNumberFunction();
                                   // sendToRoll();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(SelectActivity.this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                        }*/
                        }
                    }

                });

            }

   /* private void rollNumberFunction() {

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
        Intent RollActivityIntent = new Intent(SelectActivity.this, RollNumber.class);
        RollActivityIntent.putExtra("ShiftText",shiftText); // FS
        RollActivityIntent.putExtra("YearText",currentYearText); //16
        RollActivityIntent.putExtra("DeptText",deptText); // IT
        RollActivityIntent.putExtra("year",yearText); //First Second Third
        RollActivityIntent.putExtra("tfws",isTfws);
        startActivity(RollActivityIntent);
        finish();

    }*/



}
