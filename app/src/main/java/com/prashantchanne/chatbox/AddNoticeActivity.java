package com.prashantchanne.chatbox;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class AddNoticeActivity extends AppCompatActivity {

    private EditText title;
    private EditText description;
    //private NumberPicker numberPicker;
    private Button post;

    private String userId;
    private FirebaseAuth firebaseAuth;
    private String userName;

    private ProgressBar progressBar;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notice);

        title = findViewById(R.id.titleEditText);
        description = findViewById(R.id.descEditText);
        //numberPicker = findViewById(R.id.priorityPicker);

//        numberPicker.setMinValue(1);
       // numberPicker.setMaxValue(10);

        progressBar = findViewById(R.id.progressBarNotice);
        post = findViewById(R.id.postNoticeButton);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNotice();
            }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null)
        userId = firebaseAuth.getCurrentUser().getUid();

        Toolbar toolbar;

        //setting up the toolbar
        toolbar = findViewById(R.id.toolbarNotice);
//        toolbar.setTitle("Add Notice");
        setSupportActionBar(toolbar);

      getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.firebaseui_save,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){


            case R.id.saveNote :
                saveNotice();
                return true; //this means we consume this menu item click

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void saveNotice() {


        final String titleText = title.getText().toString();
        final String descText = description.getText().toString();
        //int prio = numberPicker.getValue();

        if (titleText.trim().isEmpty() || descText.trim().isEmpty()) {

            Toast.makeText(this, "Please fill al the details", Toast.LENGTH_SHORT).show();
            return;
        }

       // progressBar.setVisibility(View.VISIBLE);
        //add(new NoticeModel(titleText, descText,null,userName,firebaseAuth.getCurrentUser().getUid()))

        DepartmentDialog dialog = new DepartmentDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title",titleText);
        bundle.putString("desc",descText);
        bundle.putString("userName",userName);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "dialog_department");

        /*
        firebaseFirestore.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(DocumentSnapshot doc: queryDocumentSnapshots){

                    firebaseFirestore.collection("Users").document(doc.getId()).collection("Notice")
                            .add(new NoticeModel(titleText, descText,null,userName,firebaseAuth.getCurrentUser().getUid()))
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {

                            if(task.isSuccessful()){

                                //Toast.makeText(AddNoticeActivity.this, "Notice posted!", Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(AddNoticeActivity.this, "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(AddNoticeActivity.this, "Failed to post "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                Toast.makeText(AddNoticeActivity.this, "Notice Posted!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);

            }
        });*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        userId = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore.collection("Users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                 userName = documentSnapshot.getString("name");
                //Toast.makeText(AddNoticeActivity.this, "Name: "+name, Toast.LENGTH_SHORT).show();
            }
        });

    }
}