package com.prashantchanne.chatbox;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment {

    private RecyclerView recyclerView;
    private View view;
    private FirebaseFirestore firebaseFirestore;
    private NotesRecyclerAdapter notesRecyclerAdapter;

    private String dept;
    private String year;

    private ProgressBar progressBar;

    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view =  inflater.inflate(R.layout.fragment_notes, container, false);

         dept = getArguments().getString("dept");
         year = getArguments().getString("year");

         progressBar = view.findViewById(R.id.notesProgressBar);
         progressBar.setVisibility(View.INVISIBLE);

        Toast.makeText(getContext(), "Dept "+dept+"\nYear "+year, Toast.LENGTH_SHORT).show();

         recyclerView = view.findViewById(R.id.notesRecyclerView);
         firebaseFirestore = FirebaseFirestore.getInstance();

         setUpRecyclerView();


        return view;
    }

    private void setUpRecyclerView() {

        //----- Firing Query ------

        progressBar.setVisibility(View.VISIBLE);

        Query query = firebaseFirestore
                .collection("Notes")
                .document(dept)
                .collection(year);

        FirestoreRecyclerOptions<NotesModel> options = new FirestoreRecyclerOptions
                .Builder<NotesModel>()
                .setQuery(query,NotesModel.class)
                .build();

        notesRecyclerAdapter = new NotesRecyclerAdapter(options,getContext());

        //----- RecyclerView -------

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(notesRecyclerAdapter);
        recyclerView.addItemDecoration(new SimpleDivderItemDecoration(getContext()));

        progressBar.setVisibility(View.INVISIBLE);
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
        Intent loginACtivityIntent = new Intent(getContext(), LogInActivity.class);
        loginACtivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginACtivityIntent);
        getActivity().finish();
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
                Toast.makeText(getContext(), "Email is not verified", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                sendToLoginActivity();
            }
    }




}
