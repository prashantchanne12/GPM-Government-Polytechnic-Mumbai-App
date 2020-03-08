package com.prashantchanne.chatbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class NotesView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String dept;
    private String year;

    private NotesRecyclerAdapter notesRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_view);

        recyclerView = findViewById(R.id.notes_recycler_view);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbarNotes);
        toolbar.setTitle("Notes");

        dept = getIntent().getStringExtra("dept");
        year = getIntent().getStringExtra("year");

        setUpRecyclerView();

    }

    private void setUpRecyclerView() {

        //----- Firing Query ------

        Query query = firebaseFirestore
                .collection("Notes")
                .document(dept)
                .collection(year);

        FirestoreRecyclerOptions<NotesModel> options = new FirestoreRecyclerOptions
                .Builder<NotesModel>()
                .setQuery(query,NotesModel.class)
                .build();

        notesRecyclerAdapter = new NotesRecyclerAdapter(options,getApplicationContext());

        //----- RecyclerView -------

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(notesRecyclerAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search,menu);

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser == null){
            sendToLoginActivity();
            return;
        }

        notesRecyclerAdapter.startListening();
    }

    private void sendToLoginActivity() {
        Intent loginACtivityIntent = new Intent(getApplicationContext(), LogInActivity.class);
        loginACtivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginACtivityIntent);
        finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        notesRecyclerAdapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkUser();
    }

    private void checkUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user==null){
            sendToLoginActivity();
        }

        if(user!=null)
            if(!user.isEmailVerified()){
                Toast.makeText(getApplicationContext(), "Email is not verified", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                sendToLoginActivity();
            }
    }


}
