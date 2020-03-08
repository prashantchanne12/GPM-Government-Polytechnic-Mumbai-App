package com.prashantchanne.chatbox;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResendVerificationDialog  extends DialogFragment {

    //widgets
    private EditText mConfirmPassword, mConfirmEmail;

    //vars
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_resend_verification_dialog, container, false);
        mConfirmPassword =  view.findViewById(R.id.confirm_password);
        mConfirmEmail =  view.findViewById(R.id.confirm_email);
        mContext = getActivity();

        TextView confirmDialog =  view.findViewById(R.id.dialogConfirm);
        confirmDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isEmpty(mConfirmEmail.getText().toString())
                        && !isEmpty(mConfirmPassword.getText().toString())){

                    //temporarily authenticate and resend verification email
                    authenticateAndResendEmail(mConfirmEmail.getText().toString(),
                            mConfirmPassword.getText().toString());
                }else{
                    Toast.makeText(mContext, "all fields must be filled out", Toast.LENGTH_LONG).show();
                }


            }
        });


        TextView cancelDialog = view.findViewById(R.id.dialogCancel);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    private void authenticateAndResendEmail(String email, String password) {
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendVerificationEmail();
                            FirebaseAuth.getInstance().signOut();
                            getDialog().dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, "Invalid Credentials. \nReset your password and try again", Toast.LENGTH_LONG).show();
                getDialog().dismiss();
            }
        });
    }


    public void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(mContext, "Sent Verification Email", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(mContext, "couldn't send email", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

    }


    private boolean isEmpty(String string){
        return string.equals("");
    }
}

/*   <TextView
        android:id="@+id/dontHaveAnAcTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentStart="true"
        android:layout_alignParentEnd="true"
        android:layout_alignParentBottom="true"
        android:layout_marginStart="45dp"
        android:layout_marginEnd="109dp"
        android:layout_marginBottom="16dp"
        android:fontFamily="@font/roboto_medium"
        android:text="Dont have an account"
        android:textAlignment="center"
        android:textSize="15sp"
        android:textStyle="bold"
        tools:text="Dont have an account" />

    <TextView
        android:id="@+id/singupTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentStart="true"
        android:layout_alignParentBottom="true"
        android:layout_marginStart="260dp"
        android:layout_marginBottom="16dp"
        android:fontFamily="@font/roboto_medium"
        android:text="Sign up"
        android:textAlignment="center"
        android:textColor="@color/colorPrimary"
        android:textSize="16sp"
        android:textStyle="bold"
        tools:text="Sign up" />*/