package com.prashantchanne.chatbox;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;

    private List<Users> usersList;

    private UsersRecyclerAdapter usersRecyclerAdapter;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_users, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.recycler);

        usersList = new ArrayList<>();

        usersRecyclerAdapter = new UsersRecyclerAdapter(usersList, container.getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.setAdapter(usersRecyclerAdapter);



        return view;
    }



    @Override
    public void onStart() {
        super.onStart();

        if (firebaseAuth != null) {
            usersList.clear(); //It will avoid the loading of same item again and agian.
            firebaseFirestore.collection("Users").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    if(documentSnapshots!=null)
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                        String userId = doc.getDocument().getId();

                        Users users = doc.getDocument().toObject(Users.class).withId(userId);
                        usersList.add(users);

                        usersRecyclerAdapter.notifyDataSetChanged(); // to refresh the recycle adapter to know the data is changed.
                    }

                }
            });


        }
    }
}





