package com.prashantchanne.chatbox;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AvatarActivity extends AppCompatActivity {

    private ImageButton captainAmerica;
    private ImageButton wonderWoman;
    private ImageButton flash;
    private ImageButton spiderman;
    private ImageButton incredibles;

    private EditText username;
    private Button save;

    private ProgressBar progressBar;

    private String avatar;

    private float alpha = (float) 0.4;
    private float defaultAlpha = (float) 10.0;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);

        captainAmerica = findViewById(R.id.captainButton);
        wonderWoman = findViewById(R.id.wwButton);
        flash = findViewById(R.id.flashButton);
        spiderman = findViewById(R.id.spidermanButton);
        incredibles = findViewById(R.id.incrediblesButton);

        username = findViewById(R.id.avatarUsername);
        save = findViewById(R.id.avatarSave);

        progressBar = findViewById(R.id.avatarProgress);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        captainAmerica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                avatar = "captain";

                resetAlpha();

                wonderWoman.setAlpha(alpha);
                flash.setAlpha(alpha);
                spiderman.setAlpha(alpha);
                incredibles.setAlpha(alpha);
            }
        });

        wonderWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                avatar = "ww";

                resetAlpha();

                captainAmerica.setAlpha(alpha);
                flash.setAlpha(alpha);
                spiderman.setAlpha(alpha);
                incredibles.setAlpha(alpha);

            }
        });

        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                avatar = "flash";

                resetAlpha();

                captainAmerica.setAlpha(alpha);
                wonderWoman.setAlpha(alpha);
                spiderman.setAlpha(alpha);
                incredibles.setAlpha(alpha);

            }
        });

        spiderman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                avatar = "spiderman";

                resetAlpha();

                captainAmerica.setAlpha(alpha);
                wonderWoman.setAlpha(alpha);
                flash.setAlpha(alpha);
                incredibles.setAlpha(alpha);
            }
        });

        incredibles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                avatar = "incredibles";

                resetAlpha();

                captainAmerica.setAlpha(alpha);
                wonderWoman.setAlpha(alpha);
                flash.setAlpha(alpha);
                spiderman.setAlpha(alpha);

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(username.getText().toString()) && !TextUtils.isEmpty(avatar)) {

                    progressBar.setVisibility(View.VISIBLE);

                    String name = username.getText().toString();

                    //Toast.makeText(AvatarActivity.this, "Name: " + name + "\nAvatar: " + avatar, Toast.LENGTH_SHORT).show();

                    Map<String,Object> avatarMap = new HashMap<>();
                    avatarMap.put("name",name);
                    avatarMap.put("image",avatar);

                    if(firebaseAuth.getCurrentUser()!=null)
                    firebaseFirestore.collection("Users")
                            .document(firebaseAuth.getCurrentUser().getUid())
                            .update(avatarMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    sendToSelect();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(AvatarActivity.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }

                else{
                    Toast.makeText(AvatarActivity.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void resetAlpha() {

        captainAmerica.setAlpha(defaultAlpha);
        wonderWoman.setAlpha(defaultAlpha);
        flash.setAlpha(defaultAlpha);
        spiderman.setAlpha(defaultAlpha);
        incredibles.setAlpha(defaultAlpha);

    }

    private void sendToSelect() {
        Intent intent = new Intent(AvatarActivity.this,Singup_Student_Teacher.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {


        final AlertDialog.Builder builder = new AlertDialog.Builder(AvatarActivity.this);
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


