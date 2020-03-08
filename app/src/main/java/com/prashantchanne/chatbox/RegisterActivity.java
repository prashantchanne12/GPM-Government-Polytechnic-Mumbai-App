package com.prashantchanne.chatbox;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private EditText nameText;
    private EditText emailText;
    private EditText passwordText;
    private EditText confirmPassword;

    private Button signUpButton;
   // private Button alreadyHaveAcButton;
    private TextView login;

    private CircleImageView circleImageView;
    private Uri imageUri;

    private StorageReference firebaseStorage;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private ProgressBar progressBar;
    private ProgressBar progressBarImage;

    private String tokenId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

       // nameText = findViewById(R.id.nameEditText);
        emailText = findViewById(R.id.emailIdText);
        passwordText = findViewById(R.id.passwordEditText);
        confirmPassword = findViewById(R.id.confirmPassEditText);
        signUpButton = findViewById(R.id.createNewAccountButton);
        // alreadyHaveAcButton = findViewById(R.id.alreadyHaveAcButton);
        login = findViewById(R.id.loginTextview);

        circleImageView = findViewById(R.id.notificationUserImageview);
        imageUri = null;

        firebaseStorage = FirebaseStorage.getInstance().getReference().child("images");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        progressBar = findViewById(R.id.Progress);
        progressBarImage = findViewById(R.id.progressBarImage);
        /*alreadyHaveAcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                // Intent logInActivityIntent = new Intent(RegisterActivity.this, LogInActivity.class);
                // startActivity(logInActivityIntent);
            }
        });
*/
        TextView resetPassword = (TextView) findViewById(R.id.forgot_password);
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordResetDialog dialog = new PasswordResetDialog();
                dialog.show(getSupportFragmentManager(), "dialog_password_reset");
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Email = emailText.getText().toString();
                String Pass = passwordText.getText().toString();
                String ConfirmPass = confirmPassword.getText().toString();



                if(!TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Pass) && !TextUtils.isEmpty(ConfirmPass)){

                    if(Pass.equals(ConfirmPass)){

                        progressBar.setVisibility(View.VISIBLE);

                    firebaseAuth.createUserWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                final String userId = firebaseAuth.getCurrentUser().getUid();

                                Map<String,String> map = new HashMap<>();
                                map.put("token_id",tokenId);

                                firebaseFirestore.collection("Users").document(userId).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()){

                                            progressBar.setVisibility(View.INVISIBLE);
                                            sendVerificationEmail();
                                            sendToSetup();

                                        }else{

                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(RegisterActivity.this, "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });


                            }else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(RegisterActivity.this, "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }


                        }
                    });

                    }else{
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(RegisterActivity.this, "Password do not match", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(RegisterActivity.this, "Fill all the details!", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }




    private void sendVerificationEmail() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Verification mail sent", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(RegisterActivity.this, "Something wrong "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

    }

    private void sendToSelect() {
        Intent intent = new Intent(RegisterActivity.this,Singup_Student_Teacher.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void sendToMain() {

        Intent MainActivityIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(MainActivityIntent);
        finish();

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(RegisterActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                tokenId = instanceIdResult.getToken();
            }
        });
    }

    public void sendToSetup(){

        Intent intent = new Intent(RegisterActivity.this,AvatarActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }
}

