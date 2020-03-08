package com.prashantchanne.chatbox;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

public class Singup_Student_Teacher extends AppCompatActivity {

    private CardView teacher;
    private CardView student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup_student_teacher);

        teacher = findViewById(R.id.teacher_card);
        student = findViewById(R.id.student_card);

        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoTeacherSignup();
            }
        });

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoNormalSignup();
            }
        });

    }

    private void gotoNormalSignup() {

        startActivity(new Intent(Singup_Student_Teacher.this,SelectActivity.class));

    }

    private void gotoTeacherSignup(){
        startActivity(new Intent(Singup_Student_Teacher.this,Teacher_Signup_key.class));
    }


    @Override
    public void onBackPressed() {


        final AlertDialog.Builder builder = new AlertDialog.Builder(Singup_Student_Teacher.this);
        builder.setMessage("Please select first");
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
