package com.prashantchanne.chatbox;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerification extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button verify;
    private TextView resendEmail;

    private TextView text1;
    private TextView text2;
    private TextView emailVerifiedOrNotText;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private boolean verifiedOrNot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        progressBar = findViewById(R.id.verifyProgressBar);
        verify = findViewById(R.id.verifyEmailButton);
        resendEmail = findViewById(R.id.resendEmailTextView);

        text1 = findViewById(R.id.checkTextview);
        text2 = findViewById(R.id.weTextview);
        emailVerifiedOrNotText = findViewById(R.id.emailVerifiedTextview);

        firebaseAuth = FirebaseAuth.getInstance();



        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EmailVerification.this,SelectActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


            }
        });

        resendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(EmailVerification.this, "Email is sent!", Toast.LENGTH_SHORT).show();

                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(EmailVerification.this, "Error resending " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });



    }

    private void checkUserRegisterOrNot() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser!=null){
           // Toast.makeText(this, ""+firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
            if(firebaseUser.isEmailVerified()){
                Toast.makeText(this, "Email is Verified!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EmailVerification.this,SelectActivity.class));
                finish();
            }else{
                Toast.makeText(this, "Email "+firebaseUser.getUid(), Toast.LENGTH_SHORT).show();
               // Toast.makeText(this, "Not Verified!", Toast.LENGTH_SHORT).show();
            }

        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser!=null){
            if(firebaseUser.isEmailVerified()){
                text1.setVisibility(View.INVISIBLE);
                text2.setVisibility(View.INVISIBLE);
                verify.setVisibility(View.VISIBLE);
                emailVerifiedOrNotText.setText("Email is Verified!");
            }else{
                verify.setVisibility(View.INVISIBLE);
                emailVerifiedOrNotText.setText("Email is not Verified!");
            }
        }else{
            Toast.makeText(this, "Null", Toast.LENGTH_SHORT).show();
        }

    }
}



