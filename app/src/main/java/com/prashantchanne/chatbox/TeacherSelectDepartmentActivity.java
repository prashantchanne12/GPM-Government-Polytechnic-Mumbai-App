package com.prashantchanne.chatbox;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class TeacherSelectDepartmentActivity extends AppCompatActivity {

    private Spinner spinner;
    private Button next;
    private String dept;
    private String userId;
    private ProgressBar progressBar;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_select_department);

        spinner = findViewById(R.id.techerDeptSpinner);
        next = findViewById(R.id.nextButton);
        progressBar = findViewById(R.id.progressBarTecher);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        userId = firebaseAuth.getCurrentUser().getUid();

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,R.array.teacher_dept,android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dept = spinner.getSelectedItem().toString();

                if (dept.equals("Select Department")) {
                    Toast.makeText(TeacherSelectDepartmentActivity.this, "Please Select the Department", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    progressBar.setVisibility(View.VISIBLE);

                    firebaseFirestore.collection("Users").document(userId).update("dept", dept).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Intent intent = new Intent(TeacherSelectDepartmentActivity.this,MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();

                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(TeacherSelectDepartmentActivity.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });



    }

}
