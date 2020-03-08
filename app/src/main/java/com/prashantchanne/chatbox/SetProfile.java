package com.prashantchanne.chatbox;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetProfile extends AppCompatActivity {

    private CircleImageView userImageView;
    private Button userSaveButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    String userId;
    Uri mainImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);

        userImageView = findViewById(R.id.setUserImageView);
        userSaveButton = findViewById(R.id.setUserSaveButton);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();

         userId = firebaseAuth.getCurrentUser().getUid();

        userSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userId = firebaseAuth.getCurrentUser().getUid();

               //loader.setVisibility(View.VISIBLE);

                StorageReference imagePath = storageReference.child("Profile Image").child(userId + " .jpg");

                imagePath.putFile(mainImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(SetProfile.this,"Settings Are Updated",Toast.LENGTH_SHORT).show();
                            Intent selectIntent = new Intent(SetProfile.this,ProfileFragmnet.class);
                            startActivity(selectIntent);

                        } else {

                            String error = task.getException().getMessage();
                            Toast.makeText(SetProfile.this, "Error! " + error, Toast.LENGTH_SHORT).show();
                            //loader.setVisibility(View.INVISIBLE);
                        }


                    }
                });

            }
        });


        userImageView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View view) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) //this means the if the user is running marshmello or greater version then the permission is required from the user else dont
                {

                    if(ContextCompat.checkSelfPermission(SetProfile.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){  //It will check the perssion is granted or not

                        Toast.makeText(SetProfile.this,"Permission Granted",Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(SetProfile.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

                    }
                    else {

                        callImageCroper();

                    }

                }
                else{

                    callImageCroper();
                }

            }
        });
    }

    private void callImageCroper() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .setMaxCropResultSize(512,512)
                .start(SetProfile.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                mainImageUri = result.getUri();
                userImageView.setImageURI(mainImageUri);



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }

    }


}

