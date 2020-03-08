package com.prashantchanne.chatbox;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActvity extends AppCompatActivity {

    private EditText username;
    private CircleImageView circleImageView;
    private Button save;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;

    private Uri mainImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_actvity);


        username = findViewById(R.id.UsernameEditText);
        circleImageView = findViewById(R.id.circleImageViewProfile);
        save = findViewById(R.id.SetupSaveButton);
        progressBar = findViewById(R.id.imageProgressBar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();


        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if(ContextCompat.checkSelfPermission(SetupActvity.this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                        ActivityCompat.requestPermissions(SetupActvity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                        Toast.makeText(SetupActvity.this, "Please click on the Profile again", Toast.LENGTH_SHORT).show();

                    }else{

                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1,1)
                                .start(SetupActvity.this);

                    }

                }else{
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1,1)
                            .start(SetupActvity.this);
                }

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String name = username.getText().toString();

                if(!TextUtils.isEmpty(name) && mainImageUri!=null){

                    progressBar.setVisibility(View.VISIBLE);

                    final String userId = firebaseAuth.getCurrentUser().getUid();


                    //------------ Uploading Image to the Firebase Storage --------------


                    StorageReference storageReference = firebaseStorage.getReference();
                    final StorageReference imagefile = storageReference.child("images").child(userId + ".jpg");

                    UploadTask uploadTask = imagefile.putFile(mainImageUri);

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                            if (!task.isSuccessful()) {
                                throw Objects.requireNonNull(task.getException());
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(SetupActvity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                            //Continue with the task to get the download Uri
                            return imagefile.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Uri downloadUri = task.getResult();
                            String finalDownloadUri = downloadUri.toString();

                            HashMap<String, Object> values = new HashMap<>();
                            values.put("name",name);
                            values.put("image",finalDownloadUri);

                            firebaseFirestore.collection("Users").document(userId).update(values).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(SetupActvity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                    sendToSelect();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(SetupActvity.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });



                }else {

                    progressBar.setVisibility(View.INVISIBLE);

                    if (mainImageUri == null) {
                        Toast.makeText(SetupActvity.this, "Please select Image", Toast.LENGTH_SHORT).show();
                    }else if(name.trim().length() == 0){
                        Toast.makeText(SetupActvity.this, "", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                //isChanged = true;
                mainImageUri = result.getUri();
                Glide.with(this).load(mainImageUri).into(circleImageView);
                //circleImageView.setImageURI(mainImageUri);



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Toast.makeText(this, "Error "+error, Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void sendToSelect() {
        Intent intent = new Intent(SetupActvity.this,Singup_Student_Teacher.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {


        final AlertDialog.Builder builder = new AlertDialog.Builder(SetupActvity.this);
        builder.setMessage("Please fill the details");
        builder.setCancelable(true);


        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });



        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
