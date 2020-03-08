package com.prashantchanne.chatbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

public class addActivity extends AppCompatActivity {

    private CardView attendance;
    private CardView message;
    private CardView notes;
    private CardView notice;
    private CardView sendStudent;
    private CardView statistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        attendance = findViewById(R.id.attendance_card);
        message = findViewById(R.id.message_card);
        notes = findViewById(R.id.upload_notes_card);
        notice = findViewById(R.id.notice_card);
        sendStudent = findViewById(R.id.send_to_next_year_card);
        statistics = findViewById(R.id.statistics_card_teacher);

        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent subjectAndPracticalIntent = new Intent(addActivity.this,AttendanceActivity.class);
                startActivity(subjectAndPracticalIntent);
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent notification = new Intent(addActivity.this,SendNotificationActivity.class);
                startActivity(notification);
            }
        });

        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent upload = new Intent(addActivity.this,NotesSelectDepartment.class);
                startActivity(upload);

            }
        });

        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(addActivity.this,AddNoticeActivity.class));
            }
        });

        sendStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SendStudentYear.class);
                startActivity(intent);
            }
        });

        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showStatistics = new Intent(getApplicationContext(),StatisticsSelect.class);
                startActivity(showStatistics);
            }
        });

/*
        notiActivity = findViewById(R.id.gotoNotification);
        uploadActivity = findViewById(R.id.gotoUpload);
        attendanceActvity = findViewById(R.id.gotoAttendance);
        addNotice = findViewById(R.id.addNoticeButton);

       // notiActivity.setVisibility(View.INVISIBLE);

        notiActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent notification = new Intent(addActivity.this,SendNotificationActivity.class);
                startActivity(notification);

            }
        });


        uploadActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent upload = new Intent(addActivity.this,NotesSelectDepartment.class);
                startActivity(upload);


            }
        });


        attendanceActvity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent subjectAndPracticalIntent = new Intent(addActivity.this,SubjectAndPractical.class);
                startActivity(subjectAndPracticalIntent);

            }
        });


        addNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(addActivity.this,AddNoticeActivity.class));
            }
        });*/

    }
}
/*
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".NotesSelectDepartmentFragment"
    android:padding="15dp"
    android:layout_marginTop="47dp"
    android:screenOrientation="portrait">

    <ScrollView
        android:id="@+id/scrollView2"
        android:layout_width="0dp"
        android:layout_height="match_parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical">

            <Button
                android:id="@+id/itButton"
                android:layout_width="match_parent"
                android:layout_height="140dp"
                android:background="@drawable/backtwo"
                android:fontFamily="@font/roboto_medium"
                android:text="Information Technology"
                android:textAllCaps="false"
                android:textColor="@android:color/white"
                android:textSize="18sp"
                android:textStyle="bold" />

            <Button
                android:id="@+id/coButton"
                android:layout_width="match_parent"
                android:layout_height="140dp"
                android:layout_marginTop="16dp"
                android:background="@drawable/co"
                android:fontFamily="@font/roboto_medium"
                android:text="Computer Engineering"
                android:textAllCaps="false"
                android:textColor="@android:color/white"
                android:textSize="18sp"
                android:textStyle="bold" />

            <Button
                android:id="@+id/ecButton"
                android:layout_width="match_parent"
                android:layout_height="140dp"
                android:layout_marginTop="16dp"
                android:background="@drawable/ec"
                android:fontFamily="@font/roboto_medium"
                android:text="Electronics Engineering"
                android:textAllCaps="false"
                android:textColor="@android:color/white"
                android:textSize="18sp"
                android:textStyle="bold" />

            <Button
                android:id="@+id/meButton"
                android:layout_width="match_parent"
                android:layout_height="140dp"
                android:layout_marginTop="16dp"
                android:background="@drawable/me"
                android:fontFamily="@font/roboto_medium"
                android:text="Mechanical Engineering"
                android:textAllCaps="false"
                android:textColor="@android:color/white"
                android:textSize="18sp"
                android:textStyle="bold" />

            <Button
                android:id="@+id/ceButton"
                android:layout_width="match_parent"
                android:layout_height="140dp"
                android:layout_marginTop="16dp"
                android:background="@drawable/ce"
                android:fontFamily="@font/roboto_medium"
                android:text="Civil Engineering"
                android:textAllCaps="false"
                android:textColor="@color/common_google_signin_btn_text_dark_focused"
                android:textSize="18sp"
                android:textStyle="bold" />
        </LinearLayout>

    </ScrollView>

</android.support.constraint.ConstraintLayout>
 */