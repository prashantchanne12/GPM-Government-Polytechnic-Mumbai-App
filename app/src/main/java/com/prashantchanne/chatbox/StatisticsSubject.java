package com.prashantchanne.chatbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class StatisticsSubject extends AppCompatActivity {

    private AutoCompleteTextView subjectText;
    private Spinner lectureOrPrac;
    private Button submit;
    private ProgressBar progressBar;

    String Dept;
    String Shift;
    String Year_;
    String addYear;

    String tempDept;
    int count = 0;

    private FirebaseFirestore firebaseFirestore;
    private ArrayList<String> subList;

    private static final String[] subjects = new String[]{
            "Software Engineering", "Information Security", "Python", "Advanced Java", "Linux", "AWT"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_subject);


        Dept = getIntent().getStringExtra("dept");
        Shift = getIntent().getStringExtra("shift");
        Year_ = getIntent().getStringExtra("year");
        addYear = getIntent().getStringExtra("addYear");

        progressBar = findViewById(R.id.progressBarStisticsSubject);

        firebaseFirestore = FirebaseFirestore.getInstance();
        subList = new ArrayList<>();

        firebaseFirestore.collection("Subjects")
                .document(Dept)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        SubjectsArrayModel s = documentSnapshot.toObject(SubjectsArrayModel.class);
                        subList.addAll(s.getSub());
                    }
                });


        if(Dept.equals("IT")){
            Dept = "IF";
        }

        String text = Dept +"\n"+ Shift +"\n"+ Year_+"\n"+ addYear;

        Toast.makeText(this, ""+text, Toast.LENGTH_SHORT).show();

        subjectText = findViewById(R.id.StaticsubjectEditText);
        lectureOrPrac = findViewById(R.id.StaticlopSpinner);
        submit = findViewById(R.id.StaticButton);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,subList);
        subjectText.setAdapter(adapter);

        ArrayAdapter<CharSequence> locAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.loc,
                android.R.layout.simple_spinner_dropdown_item);
        locAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        lectureOrPrac.setAdapter(locAdapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                final String lop = lectureOrPrac.getSelectedItem().toString();
                final String subject = subjectText.getText().toString().trim();

                if (lop.equals("Lecture or Practical") || subject.isEmpty()){

                    Toast.makeText(StatisticsSubject.this, "Please fill all details", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;

                }else {

                    for (String s : subList) {
                        if (s.equals(subject)) {
                            count++;
                        }
                    }


                    if (count == 0) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(StatisticsSubject.this, "Invalid Subject", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        FirebaseFirestore.getInstance().collection("Attendance")
                                .document(Dept)
                                .collection(Year_)
                                .document(Shift)
                                .collection(addYear)
                                .document(subject)
                                .collection(lop)
                                .document("total")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                        String total = documentSnapshot.getString("total_lectures");

                                       if (total == null){
                                            progressBar.setVisibility(View.INVISIBLE);
                                           Toast.makeText(StatisticsSubject.this, "No data available", Toast.LENGTH_SHORT).show();

                                       }else {

                                           Intent intent = new Intent(getApplicationContext(), StatisticsActivity.class);
                                           intent.putExtra("lop", lop);
                                           intent.putExtra("subject", subject);
                                           intent.putExtra("dept", Dept);
                                           intent.putExtra("shift", Shift);
                                           intent.putExtra("year", Year_);
                                           intent.putExtra("addYear", addYear);
                                           intent.putExtra("total", total);
                                           progressBar.setVisibility(View.INVISIBLE);
                                           startActivity(intent);

                                       }
                                    }
                                });


                    }

                }

            }
        });

    }

}
