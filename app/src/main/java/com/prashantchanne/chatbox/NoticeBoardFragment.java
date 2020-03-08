package com.prashantchanne.chatbox;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoticeBoardFragment extends Fragment {


    private RecyclerView recyclerView;
    private NoticeAdapter noticeAdapter;
    private FirebaseFirestore firebaseFirestore;
    private View view;

    private FirebaseUser firebaseUser;

    private String userId;

    public NoticeBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser==null){

            sendToLoginActivity();
        }
        else {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_notice_board, container, false);
            firebaseFirestore = FirebaseFirestore.getInstance();
            setUpRecyclerView();
        }
        return view;
    }

    private void sendToLoginActivity() {

        Intent intent = new Intent(getContext(),LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    private void setUpRecyclerView() {



        Query query = firebaseFirestore.collection("Users")
                .document(userId)
                .collection("Notice")
                .orderBy("timestamp",Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<NoticeModel> options = new FirestoreRecyclerOptions.Builder<NoticeModel>()
                .setQuery(query,NoticeModel.class)
                .build();

        noticeAdapter = new NoticeAdapter(options);

        recyclerView = view.findViewById(R.id.noticeRecyclerViewTP);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.addItemDecoration(new SimpleDivderItemDecoration(getContext()));
        recyclerView.setAdapter(noticeAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                noticeAdapter.deleteItem(viewHolder.getAdapterPosition());

            }
        }).attachToRecyclerView(recyclerView);

        noticeAdapter.SetOnItemClickListener(new NoticeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                NoticeModel model = documentSnapshot.toObject(NoticeModel.class);
                Toast.makeText(getContext(), "Title "+model.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

    }





    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user==null){
            sendToLoginActivity();
            return;
        }

        noticeAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();

        noticeAdapter.startListening();
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
                Toast.makeText(getContext(), "Email is not verified!", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                sendToLoginActivity();
            }
    }


}

