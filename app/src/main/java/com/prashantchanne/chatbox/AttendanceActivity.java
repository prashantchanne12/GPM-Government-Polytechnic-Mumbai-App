package com.prashantchanne.chatbox;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AttendanceActivity extends AppCompatActivity {

    private Spinner categorySpinner;
    private Spinner shiftSpinner;
    private Spinner yearSpinner;
    private Spinner yearTextSpinnner;

    private Button selectButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String currentUser;

    private ProgressBar progressBar;

    private String deptText;
    private String shiftText;
    private String yearText;
    private String addMissionYear;

    private String tempShift;
    private String tempDept;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        shiftSpinner = findViewById(R.id.spinnerT);
        yearSpinner = findViewById(R.id.spinnerYear);
        yearTextSpinnner = findViewById(R.id.spinnerYearText);

        selectButton = findViewById(R.id.selectButton);
        categorySpinner = findViewById(R.id.spinnerSelectDept);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        currentUser = firebaseAuth.getCurrentUser().getUid();
        progressBar = findViewById(R.id.Progress);


        //--------- Department Spinner --------//
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AttendanceActivity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.names2));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(arrayAdapter);

        //--------- Shift Spinner --------//
        final ArrayAdapter<String> shiftArrayAdapter = new ArrayAdapter<>(AttendanceActivity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.shifts2));
        shiftArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        shiftSpinner.setAdapter(shiftArrayAdapter);

        //--------- Year Spinner --------//
        ArrayAdapter<String> yearArrayAdapter = new ArrayAdapter<>(AttendanceActivity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.year2));
        yearArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        yearSpinner.setAdapter(yearArrayAdapter);

        //--------- Admission Year Spinner --------//
        ArrayAdapter<String> yearTextArrayAdapter = new ArrayAdapter<>(AttendanceActivity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.yearText2));
        yearTextArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        yearTextSpinnner.setAdapter(yearTextArrayAdapter);


        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deptText = categorySpinner.getSelectedItem().toString();
                shiftText = shiftSpinner.getSelectedItem().toString();
                yearText = yearSpinner.getSelectedItem().toString();
                addMissionYear = yearTextSpinnner.getSelectedItem().toString();


                if(deptText.contentEquals("Select your Department")
                        || shiftText.contentEquals("Select your Shift")
                        || yearText.contentEquals("Select your Year")
                        || addMissionYear.contentEquals("Select admission year"))

                {
                    Toast.makeText(AttendanceActivity.this, "Please select all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);

                    tempDept = deptText;
                    tempShift = shiftText;

                    if(deptText.equals("IT")){
                        tempDept="IF";
                    }

                    if(shiftText.equals("First")){
                        tempShift = "FS";
                    }

                    if(shiftText.equals("Second")){
                        tempShift = "SS";
                    }


                    Intent studentListIntent = new Intent(AttendanceActivity.this, SubjectAndPractical.class);
                    studentListIntent.putExtra("dept", deptText);
                    studentListIntent.putExtra("shift", shiftText);
                    studentListIntent.putExtra("year", yearText);
                    studentListIntent.putExtra("addmissionYear", addMissionYear);
                    progressBar.setVisibility(View.INVISIBLE);
                    startActivity(studentListIntent);


                }
            }
        });

    }


}
