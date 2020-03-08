package com.prashantchanne.chatbox;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class SubjectAndPractical extends AppCompatActivity {

    private TextView subjecTextView;
    private AutoCompleteTextView subjectText;
    private Spinner lectureOrPrac;
    private Button submit;
    private FirebaseFirestore firebaseFirestore;

    private String dept,shift,year,addYear;
    private String tempShift;
    private String tempDept;

    private int count = 0;

    private ArrayList<String> subList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_and_practical);

        firebaseFirestore = FirebaseFirestore.getInstance();

        subList = new ArrayList<>();

        dept = getIntent().getStringExtra("dept");
        shift = getIntent().getStringExtra("shift");
        year = getIntent().getStringExtra("year");
        addYear = getIntent().getStringExtra("addmissionYear");

        firebaseFirestore.collection("Subjects")
                .document(dept)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        SubjectsArrayModel arrayModel = documentSnapshot.toObject(SubjectsArrayModel.class);

                        subList.addAll(arrayModel.getSub());

                    }
                });


        subjectText = findViewById(R.id.subjectEditText);

        //--------- AutoComplete TextView ---------//
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,subList);
        subjectText.setAdapter(adapter);

        lectureOrPrac = findViewById(R.id.lopSpinner);
        submit = findViewById(R.id.subjectAndPracButton);

        //--------- Lecture or Practical ---------//
        ArrayAdapter<String> locAdapter = new ArrayAdapter<>(SubjectAndPractical.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.loc));
        locAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lectureOrPrac.setAdapter(locAdapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String subject = subjectText.getText().toString();
                String lop = lectureOrPrac.getSelectedItem().toString();

                if (subject.isEmpty() || lop.equals("Lecture or Practical")) {
                    Toast.makeText(SubjectAndPractical.this, "Please fill all details", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    for (String s : subList) {
                        if (s.equals(subject)) {
                            count++;
                        }
                    }

                    if (count == 0) {
                        Toast.makeText(SubjectAndPractical.this, "Invalid Subject", Toast.LENGTH_SHORT).show();
                    } else {


                        Intent numberActivity = new Intent(SubjectAndPractical.this, NumberOfLectures.class);
                        numberActivity.putExtra("dept", dept);
                        numberActivity.putExtra("shift", shift);
                        numberActivity.putExtra("year", year);
                        numberActivity.putExtra("addYear", addYear);
                        numberActivity.putExtra("subject", subject);
                        numberActivity.putExtra("lop", lop);
                        startActivity(numberActivity);

                    }
                }
            }
        });

    }
}
