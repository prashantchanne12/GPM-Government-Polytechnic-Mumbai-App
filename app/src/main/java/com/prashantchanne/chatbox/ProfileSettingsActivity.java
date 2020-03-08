package com.prashantchanne.chatbox;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSettingsActivity extends AppCompatActivity {

    private CircleImageView profile;
    private TextView username;
    private TextView id;
    private TextView dept;
    private TextView year;
    private TextView shift;
    private TextView addYear;
    private android.support.v7.widget.Toolbar toolbar;
    private Button logout;

    private FirebaseFirestore firebaseFirestore;
    private String userId;
    private FirebaseAuth firebaseAuth;

    private String name,rollnum,department,years,shifts,addyear,image;
    private Boolean teacher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        //---------- Initializing the widgets ------------

        profile = findViewById(R.id.ProfileImageView);
        username = findViewById(R.id.ProfileUserName);
        id = findViewById(R.id.ProfileIdTextView);
        dept = findViewById(R.id.ProfileDeptTextView);
        year = findViewById(R.id.ProfileYearTextView);
        shift = findViewById(R.id.ProfileShiftTextView);
        addYear = findViewById(R.id.ProfileAddYearTextView);
        toolbar = findViewById(R.id.Profiletoolbar);
        logout = findViewById(R.id.ProfileLogoutbutton);

        toolbar.setTitle("Account Settings");
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //--------- Initializing the Database constraints -----------

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        //--------- Setting the value --------------

        userId = firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore.collection("Users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                Toast.makeText(ProfileSettingsActivity.this, "Name"+documentSnapshot.getString("name"), Toast.LENGTH_SHORT).show();

                teacher = documentSnapshot.getBoolean("teacher");

                if(teacher){
                    gotoTechaer(documentSnapshot);
                }else {
                    gotoStudent(documentSnapshot);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileSettingsActivity.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String,Object> tokenRemove = new HashMap<>();
                tokenRemove.put("token_id", FieldValue.delete());

                firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).update(tokenRemove).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        firebaseAuth.signOut();

                        Intent loginIntent = new Intent(ProfileSettingsActivity.this, LogInActivity.class);
                        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(loginIntent);
                        finish();

                        Toast.makeText(ProfileSettingsActivity.this, "Successfully Logegd Out", Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });


    }

    private void gotoTechaer(DocumentSnapshot documentSnapshot) {
        String Name = documentSnapshot.getString("name");
        String Dept = documentSnapshot.getString("dept");
        String image = documentSnapshot.getString("image");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.default_image);

        if (image!=null) {

            if (image.equals("captain")) {
                Glide.with(getApplicationContext()).setDefaultRequestOptions(requestOptions).load(R.drawable.captainamerica).into(profile);
            }

            if (image.equals("ww")) {
                Glide.with(getApplicationContext()).setDefaultRequestOptions(requestOptions).load(R.drawable.wonderwoman).into(profile);
            }

            if (image.equals("flash")) {
                Glide.with(getApplicationContext()).setDefaultRequestOptions(requestOptions).load(R.drawable.theflash).into(profile);
            }

            if (image.equals("spiderman")) {
                Glide.with(getApplicationContext()).setDefaultRequestOptions(requestOptions).load(R.drawable.spiderman).into(profile);
            }

            if (image.equals("incredibles")) {
                Glide.with(getApplicationContext()).setDefaultRequestOptions(requestOptions).load(R.drawable.incredibles).into(profile);
            }

        }

      //  Glide.with(ProfileSettingsActivity.this).load(image).into(profile);
        username.setText(Name);
        id.setText("Teacher");
        dept.setText(Dept);

    }


    private void gotoStudent(DocumentSnapshot documentSnapshot) {
        name = documentSnapshot.getString("name");
        department = documentSnapshot.getString("dept");
        shifts = documentSnapshot.getString("shift");
        years = documentSnapshot.getString("year");
        rollnum = documentSnapshot.getString("roll");
        addyear = documentSnapshot.getString("add_year");
        image = documentSnapshot.getString("image");

        if(department.equals("IT")){
            department = "Information Technology";
        }

        if(department.equals("CO")){
            department = "Computer Department";
        }

        if(department.equals("CE")){
            department = "Civil Department";
        }


        if(department.equals("ME")){
            department = "Mechanical Department";
        }

        if(department.equals("EC")){
            department = "Electronics Department";
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.default_image);

        if (image!=null) {

            if (image.equals("captain")) {
                Glide.with(getApplicationContext()).setDefaultRequestOptions(requestOptions).load(R.drawable.captainamerica).into(profile);
            }

            if (image.equals("ww")) {
                Glide.with(getApplicationContext()).setDefaultRequestOptions(requestOptions).load(R.drawable.wonderwoman).into(profile);
            }

            if (image.equals("flash")) {
                Glide.with(getApplicationContext()).setDefaultRequestOptions(requestOptions).load(R.drawable.theflash).into(profile);
            }

            if (image.equals("spiderman")) {
                Glide.with(getApplicationContext()).setDefaultRequestOptions(requestOptions).load(R.drawable.spiderman).into(profile);
            }

            if (image.equals("incredibles")) {
                Glide.with(getApplicationContext()).setDefaultRequestOptions(requestOptions).load(R.drawable.incredibles).into(profile);
            }

        }

        username.setText(name);
        shift.setText(shifts+" Shift");
        id.setText(rollnum);
        dept.setText(department);
        year.setText(years+" Year");
        addYear.setText(addyear);
    }


}
