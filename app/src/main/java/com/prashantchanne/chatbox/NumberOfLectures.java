package com.prashantchanne.chatbox;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class NumberOfLectures extends AppCompatActivity {

    private NumberPicker numberPicker;
    private Button next;
    private ProgressBar progressBar;

    private String dept,shift,year,addYear,subject,lop;

    private String tempShift;
    private String tempDept;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_of_lectures);

        firebaseFirestore = FirebaseFirestore.getInstance();

        dept = getIntent().getStringExtra("dept");
        shift = getIntent().getStringExtra("shift");
        year = getIntent().getStringExtra("year");
        addYear = getIntent().getStringExtra("addYear");
        subject = getIntent().getStringExtra("subject");
        lop = getIntent().getStringExtra("lop");

        numberPicker = findViewById(R.id.numberPicker);
        progressBar = findViewById(R.id.progressBarNol);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(4);

        next = findViewById(R.id.nextNumberButton);

        final String subject = getIntent().getStringExtra("subject");
        final String lop = getIntent().getStringExtra("lop");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                String num = numberPicker.getValue()+"";

                Toast.makeText(NumberOfLectures.this, "Value "+num, Toast.LENGTH_SHORT).show();

                tempDept = dept;
                tempShift = shift;

                if(dept.equals("IT")){
                    tempDept="IF";
                }

                if(shift.equals("First")){
                    tempShift = "FS";
                }

                if(shift.equals("Second")){
                    tempShift = "SS";
                }


                final Map<String,Object> initialMap = new HashMap<>();
                initialMap.put("Attended","0");

                firebaseFirestore
                        .collection("Users")
                        .whereEqualTo("add_year",addYear)
                        .whereEqualTo("dept",dept)
                        .whereEqualTo("year",year)
                        .whereEqualTo("shift",shift)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                for(final DocumentSnapshot doc: queryDocumentSnapshots){

                                    firebaseFirestore.collection("Users")
                                            .document(doc.getId())
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(final DocumentSnapshot documentSnapshot) {

                                                    final String number = documentSnapshot.getString("roll");
                                                    final String profile = documentSnapshot.getString("image");
                                                    final String name = documentSnapshot.getString("name");

                                                    final DocumentReference docRef =  firebaseFirestore.collection("Attendance")
                                                            .document(tempDept)
                                                            .collection(year)
                                                            .document(shift)
                                                            .collection(addYear)
                                                            .document(subject)
                                                            .collection(lop)
                                                            .document(number);

                                                    docRef.get()
                                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                    if(task.isSuccessful()){

                                                                        DocumentSnapshot docSnap = task.getResult();

                                                                        if(!docSnap.exists()){


                                                                            initialMap.put("name",name);
                                                                            initialMap.put("profile",profile);
                                                                            initialMap.put("roll",number);
                                                                            initialMap.put("type","student");

                                                                            docRef.set(initialMap);

                                                                        }

                                                                    }else{
                                                                        Toast.makeText(getApplicationContext(), "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }

                                                                }
                                                            });


                                                }
                                            });

                                }

                            }
                        });



                Intent intent = new Intent(NumberOfLectures.this,StudentList.class);
                intent.putExtra("dept",dept);
                intent.putExtra("shift",shift);
                intent.putExtra("year",year);
                intent.putExtra("addYear",addYear);
                intent.putExtra("subject",subject);
                intent.putExtra("lop",lop);
                intent.putExtra("lectures",num);
                progressBar.setVisibility(View.INVISIBLE);
                startActivity(intent);


            }
        });


    }
}
