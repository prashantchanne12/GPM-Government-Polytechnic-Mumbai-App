package com.prashantchanne.chatbox;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetDialog extends DialogFragment {

    //widgets
    private EditText mEmail;

    //vars
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_password_reset_dialog, container, false);
        mEmail = view.findViewById(R.id.email_password_reset);
        mContext = getActivity();

        TextView confirmDialog =  view.findViewById(R.id.dialogConfirm);
        confirmDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmpty(mEmail.getText().toString())){
                    sendPasswordResetEmail(mEmail.getText().toString());
                    getDialog().dismiss();
                }

            }
        });

        return view;
    }


    public void sendPasswordResetEmail(String email){
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mContext, "Password Reset Link Sent to Email",
                                    Toast.LENGTH_LONG).show();
                        }else{

                            Toast.makeText(mContext, "No User is Associated with that Email",
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private boolean isEmpty(String string){
        return string.equals("");
    }
}
