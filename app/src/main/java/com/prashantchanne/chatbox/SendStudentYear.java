package com.prashantchanne.chatbox;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SendStudentYear extends AppCompatActivity {


    private Spinner yearSpinner;
    private Spinner shiftSpinner;
    private Spinner addYearSpinner;
    private Button generate;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String department;
    private String year;
    private String shift;
    private String addYear;

    private MainActivity mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_student_year);

        yearSpinner = findViewById(R.id.sendStudentYearSpinner);
        shiftSpinner = findViewById(R.id.sendStudentShiftSpinner);
        addYearSpinner = findViewById(R.id.sendStudentAddYearSpinner);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        mainActivity = new MainActivity();

        generate = findViewById(R.id.generateStudentButton);

        //------------ Select Year -----------//
        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.year2,android.R.layout.simple_list_item_1);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        yearSpinner.setAdapter(yearAdapter);

        //------------ Select Shift -----------//
        ArrayAdapter<CharSequence> shiftAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.shifts2,android.R.layout.simple_list_item_1);
        shiftAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        shiftSpinner.setAdapter(shiftAdapter);

        //------------ Select Admission Year -----------//
        ArrayAdapter<CharSequence> addYearArrayAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.yearNumber2,android.R.layout.simple_list_item_1);
        addYearArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        addYearSpinner.setAdapter(addYearArrayAdapter);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 year = yearSpinner.getSelectedItem().toString();
                 shift = shiftSpinner.getSelectedItem().toString();
                 addYear = addYearSpinner.getSelectedItem().toString();

                if(year.equals("Select year") || shift.equals("Select shift") || addYear.equals("Select Admission Year")){
                    Toast.makeText(SendStudentYear.this, "Please Select all fields", Toast.LENGTH_SHORT).show();
                    return;
                }else{


                    firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {



                            Toast.makeText(getApplicationContext(), ""+documentSnapshot.getString("dept"), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),SendStudentToNextYear.class);
                            intent.putExtra("year",year);
                            intent.putExtra("shift",shift);
                            intent.putExtra("dept",documentSnapshot.getString("dept"));
                            intent.putExtra("add_year",addYear);
                            startActivity(intent);

                        }
                    });

                    //sendToGenerate();
                }


            }
        });
    }

    private void sendToGenerate() {
       // startActivity(intent);
    }
}

