package com.prashantchanne.chatbox;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.LoginFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

public class LogInActivity extends AppCompatActivity {

    private EditText emailText;
    private EditText passwordText;

    private Button logInButton;
    private TextView signup;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private ProgressBar progressBar;

    private String tokenId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = findViewById(R.id.emailEditText);
        passwordText = findViewById(R.id.passwordEditText);
        logInButton = findViewById(R.id.logInButton);
        //needNewAcButton = findViewById(R.id.newAcButton);

        signup = findViewById(R.id.singupTextView);

        progressBar = findViewById(R.id.Progress);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore  = FirebaseFirestore.getInstance();

        /*needNewAcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Intent registerActivityIntent = new Intent(LogInActivity.this,RegisterActivity.class);
                startActivity(registerActivityIntent);

            }
        });*/

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerActivityIntent = new Intent(LogInActivity.this,RegisterActivity.class);
                startActivity(registerActivityIntent);
            }
        });


        TextView resendEmailVerification = findViewById(R.id.resend_verification_email);
        resendEmailVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResendVerificationDialog dialog = new ResendVerificationDialog();
                dialog.show(getSupportFragmentManager(), "dialog_resend_email_verification");
            }
        });


        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

                    progressBar.setVisibility(View.VISIBLE);

                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if (!user.isEmailVerified()) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(LogInActivity.this, "Email is not Verified! \n Check Your Inbox", Toast.LENGTH_SHORT).show();
                                    return;

                                }else {
                                    String currentUser = firebaseAuth.getCurrentUser().getUid();

                                    Map<String, Object> tokenMap = new HashMap<>();
                                    tokenMap.put("token_id", tokenId);

                                    firebaseFirestore.collection("Users").document(currentUser).update(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            progressBar.setVisibility(View.INVISIBLE);
                                            sendToMain();
                                            Toast.makeText(LogInActivity.this, "You are successfully loged in", Toast.LENGTH_SHORT).show();


                                        }

                                    });
                                }



                            }
                            else{
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(LogInActivity.this, "Error! "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }else{
                    Toast.makeText(LogInActivity.this, "Please fill the details", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }
        });



    }



    @Override
    protected void onStart() {
        super.onStart();


        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if(currentUser != null){

            sendToMain();
        }



        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LogInActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                tokenId = instanceIdResult.getToken();
            }
        });




    }

    private void sendToMain() {
        Intent mainActvityIntent = new Intent(LogInActivity.this, MainActivity.class);
        startActivity(mainActvityIntent);
        finish();

    }


    @Override
    public void onBackPressed() {


        final AlertDialog.Builder builder = new AlertDialog.Builder(LogInActivity.this);
        builder.setMessage("Are you sure you want to exit ? ");
        builder.setCancelable(true);


        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

            }
        });



        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


}
