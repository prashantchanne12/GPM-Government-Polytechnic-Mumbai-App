package com.prashantchanne.chatbox;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Teacher_Signup_key extends AppCompatActivity {

    private EditText key;
    private Button submit;
    private FirebaseFirestore firebaseFirestore;
    private String result;
    private String userId;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;

    private int count =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_signup_key);

        key = findViewById(R.id.keyEditText);
        submit = findViewById(R.id.keySubmitButton);
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBarKey);

        firebaseAuth = FirebaseAuth.getInstance();

        userId = firebaseAuth.getCurrentUser().getUid();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String k = key.getText().toString().trim();

                if(!TextUtils.isEmpty(k)) {
                    firebaseFirestore.collection("Key").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            progressBar.setVisibility(View.VISIBLE);

                            for (DocumentSnapshot doc : queryDocumentSnapshots) {

                                String Key = doc.getString("key");
                                if (key!=null)
                                    if (Key.equals(k)) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        count++;
                                        goToSelectDept();
                                        break;
                                    }

                            }

                            if(count == 0){
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(Teacher_Signup_key.this, "Invalid Key", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Teacher_Signup_key.this, "Please enter the key", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });




    }

    private void goToSelectDept() {

        firebaseFirestore.collection("Users").document(userId).update("teacher",true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(Teacher_Signup_key.this,TeacherSelectDepartmentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });


    }
}