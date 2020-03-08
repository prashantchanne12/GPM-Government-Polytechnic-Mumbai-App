package com.prashantchanne.chatbox;

import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class StatisticsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private StatisticsRecyclerAdapter statisticsRecyclerAdapter;

    private String lop;
    private String subject;
    private String dept;
    private String shift;
    private String year;
    private String addYear;


    int total;
    int temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        lop = getIntent().getStringExtra("lop");
        subject = getIntent().getStringExtra("subject");
        dept = getIntent().getStringExtra("dept");
        shift = getIntent().getStringExtra("shift");
        year = getIntent().getStringExtra("year");
        addYear = getIntent().getStringExtra("addYear");
        total = Integer.parseInt(getIntent().getStringExtra("total"));

        recyclerView = findViewById(R.id.staticRecyclerView);
        firebaseFirestore = FirebaseFirestore.getInstance();

        Toast.makeText(this, ""+total, Toast.LENGTH_SHORT).show();

        setUpRecyclerView();

    }

    private void setUpRecyclerView() {

       // Toast.makeText(this, ""+temp, Toast.LENGTH_SHORT).show();

        Query query = firebaseFirestore.collection("Attendance")
                .document(dept)
                .collection(year)
                .document(shift)
                .collection(addYear)
                .document(subject)
                .collection(lop)
                .whereEqualTo("type","student");

        FirestoreRecyclerOptions<StatisticsModel> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<StatisticsModel>()
                .setQuery(query,StatisticsModel.class)
                .build();



        statisticsRecyclerAdapter = new StatisticsRecyclerAdapter(firestoreRecyclerOptions,getApplicationContext(),total);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //recyclerView.addItemDecoration(new SimpleDivderItemDecoration(getApplicationContext()));
        recyclerView.setAdapter(statisticsRecyclerAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

        statisticsRecyclerAdapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();

        statisticsRecyclerAdapter.stopListening();
    }



}
