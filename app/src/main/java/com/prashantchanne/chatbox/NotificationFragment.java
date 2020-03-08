package com.prashantchanne.chatbox;


import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;

    private List<NotificationUsers> notificationUsersList;

    private NotificationUserAdapterList notificationUserAdapterList;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private View view;

    private Drawable icon;
    private final ColorDrawable colorDrawable=null;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if(view==null) { //initialize if view is null

            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_notification, container, false);

            recyclerView = view.findViewById(R.id.notificationUserList);

            firebaseAuth = FirebaseAuth.getInstance();
            firebaseFirestore = FirebaseFirestore.getInstance();

            notificationUsersList = new ArrayList<>();

            notificationUserAdapterList = new NotificationUserAdapterList(notificationUsersList, container.getContext());

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
            recyclerView.setAdapter(notificationUserAdapterList);
           // recyclerView.addItemDecoration(new SimpleDivderItemDecoration(getContext()));
            notificationUsersList.clear();

            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                    //otificationUserAdapterList.deleteItem(viewHolder.getAdapterPosition());
/*
                    int position = viewHolder.getAdapterPosition();
                    NotificationUsers notificationUsers = notificationUsersList.get(position);

                    Toast.makeText(getContext(), "Msg: "+notificationUsers.getMessage(), Toast.LENGTH_SHORT).show();


                    firebaseFirestore.collection("Users")
                            .document(firebaseAuth.getCurrentUser().getUid())
                            .collection("notification")
                            .document(notificationUsers.getDoc())
                            .delete();

                    notificationUserAdapterList.notifyDataSetChanged();
*/
                }
            }).attachToRecyclerView(recyclerView);


        /*if(firebaseAuth!=null){
            notificationUsersList.clear();

            String currentUser = firebaseAuth.getCurrentUser().getUid();
            firebaseFirestore.collection("Users").document(currentUser).collection("notification")
                    .orderBy("timeStamp", Query.Direction.DESCENDING)
                    .addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                            for(DocumentChange doc : documentSnapshots.getDocumentChanges() ){

                                NotificationUsers notificationUsers = doc.getDocument().toObject(NotificationUsers.class);
                                notificationUsersList.add(notificationUsers);

                                notificationUserAdapterList.notifyDataSetChanged();

                            }


                        }
                    });


        }*/



        }
        return view;
    }

   /*@Override
    public void onStart() {
        super.onStart();
       notificationUsersList.clear();
        if(firebaseAuth!=null){
            notificationUsersList.clear();

            String currentUser = firebaseAuth.getCurrentUser().getUid();

            firebaseFirestore.collection("Users").document(currentUser).collection("notification")
                    .orderBy("timeStamp", Query.Direction.DESCENDING)
                    .addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    for(DocumentChange doc : documentSnapshots.getDocumentChanges() ){

                        NotificationUsers notificationUsers = doc.getDocument().toObject(NotificationUsers.class);
                        notificationUsersList.add(notificationUsers);

                        //this invoke both onCreateViewHolder and onCreateViewHolder
                        notificationUserAdapterList.notifyDataSetChanged();

                    }


                }
            });


        }

    }*/

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            sendToLoginActivity();
            return;
        }
        else {
            notificationUsersList.clear();

            String currentUser = firebaseAuth.getCurrentUser().getUid();

            firebaseFirestore.collection("Users").document(currentUser).collection("notification")
                    .orderBy("timeStamp", Query.Direction.DESCENDING)
                    .addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                            if (documentSnapshots!=null)
                            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                                NotificationUsers notificationUsers = doc.getDocument().toObject(NotificationUsers.class);
                                notificationUsersList.add(notificationUsers);

                                //this invoke both onCreateViewHolder and onCreateViewHolder
                                notificationUserAdapterList.notifyDataSetChanged();

                            }


                        }
                    });


        }
    }

    private void sendToLoginActivity() {

        Intent loginActivityIntent = new Intent(getContext(), LogInActivity.class);
        loginActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginActivityIntent);
        getActivity().finish();

    }

    @Override
    public void onResume() {
        super.onResume();

        checkUser();

    }

    private void checkUser() {

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user==null){
            sendToLoginActivity();
        }

        if(user!=null)
            if(!user.isEmailVerified()){
                Toast.makeText(getContext(), "Email is not verified!", Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                sendToLoginActivity();
            }

    }
}
