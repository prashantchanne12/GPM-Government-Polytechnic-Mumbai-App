package com.prashantchanne.chatbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class AddActivityStudent extends AppCompatActivity {

    private CardView notes;
    private CardView statistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);


        notes = findViewById(R.id.upload_notes_card_student);
        statistics = findViewById(R.id.statistics_card_student);


        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent upload = new Intent(getApplicationContext(),NotesSelectDepartment.class);
                startActivity(upload);
            }
        });


        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showStatistics = new Intent(getApplicationContext(),StatisticsSelect.class);
                startActivity(showStatistics);
            }
        });


    }
}
