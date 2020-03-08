package com.prashantchanne.chatbox;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class UploadDocuments extends AppCompatActivity {

    private Button selectDoc;
    private Button uploadDoc;
    private TextView textView;
    private EditText fileTitleText;

    private FirebaseStorage storage;
    private FirebaseDatabase firebaseDatabase;

    private Uri pdfUri;
    private Uri downloadUri;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;
    String title;

    private FirebaseFirestore firebaseFirestore;

    private String year;
    private String dept;

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_documents);

        selectDoc = findViewById(R.id.selecFIle);
        uploadDoc = findViewById(R.id.uploadFile);
        textView  = findViewById(R.id.uploadNameText);

        storage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        fileTitleText = findViewById(R.id.selectTitleText);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();

        dept = getIntent().getStringExtra("dept");
        year = getIntent().getStringExtra("year");


        selectDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = fileTitleText.getText().toString();

                if(ContextCompat.checkSelfPermission(UploadDocuments.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                        selectFile();
                }
                else {
                    ActivityCompat.requestPermissions(UploadDocuments.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
                }



            }
        });


        uploadDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(pdfUri != null){

                    uploadFile(pdfUri);

                }else {
                    Toast.makeText(UploadDocuments.this, "Select File First!", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void uploadFile(Uri pdfUri) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading File");
        progressDialog.setProgress(0);
        progressDialog.show();


        final String currentUser = firebaseAuth.getCurrentUser().getUid();


        final String fileName = System.currentTimeMillis()+".pdf";
        final String fileName1 = System.currentTimeMillis()+"";

        StorageReference storageReference = storage.getReference();
        final StorageReference ref =  storageReference.child("Uploads").child(title);


        final UploadTask uploadTask = ref.putFile(pdfUri);


      /*  uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadDocuments.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/


         uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        Toast.makeText(UploadDocuments.this, "Url  "+ref.getDownloadUrl(), Toast.LENGTH_SHORT).show();
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UploadDocuments.this, "Uri 2 "+task.getResult(), Toast.LENGTH_SHORT).show();
                             //downloadUri = task.getResult();


                            //now we are gooing to store this Uri in database..

                            //--------- Uploading Download URL to the Firestore -----------

                            firebaseFirestore.collection("Notes").document(dept).collection(year)
                                    .add(new NotesModel(title,task.getResult().toString(),name)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    Toast.makeText(UploadDocuments.this, "Link Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UploadDocuments.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            Toast.makeText(UploadDocuments.this, "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(UploadDocuments.this, "Error! "+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {     //helps to show the status of the file which is getting uploaded.
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                //Track the process of our upload..


                int currentProgress = (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
                if(currentProgress==100){
                    progressDialog.dismiss();
                    fileTitleText.setText(" ");
                    textView.setText(" ");
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            selectFile();
        }
        else{
            Toast.makeText(UploadDocuments.this, "Please provide permission", Toast.LENGTH_SHORT).show();
        }

    }

    private void selectFile() {

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,3);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 3 && resultCode == RESULT_OK && data != null){

            pdfUri = data.getData();
            textView.setText(title);



        }else {
            Toast.makeText(UploadDocuments.this, "Please Select a File", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseFirestore.collection("Users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        name = documentSnapshot.getString("name");

                    }
                });

    }
}
