package com.prashantchanne.chatbox;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragmnet extends Fragment {

    private Button logOutButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private String userId;

    private CircleImageView userImage;
    private TextView userText;
    private TextView userDept;
    private TextView userShift;
    private TextView userYear;



    public ProfileFragmnet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        logOutButton = view.findViewById(R.id.logOutAcButton);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userImage = view.findViewById(R.id.notificationUserImageview);
        userText  = view.findViewById(R.id.notificationUserTextview);
        userDept = view.findViewById(R.id.userDeptartment);
        userYear = view.findViewById(R.id.userYear);
        userShift = view.findViewById(R.id.userShift);



        FirebaseUser currentUser = firebaseAuth.getCurrentUser();



        if(currentUser!=null) {
            userId = firebaseAuth.getCurrentUser().getUid();



            firebaseFirestore.collection("Users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    String userName = documentSnapshot.getString("name");
                    String userImaged = documentSnapshot.getString("image");
                    String departmentName = "Department:  " +documentSnapshot.getString("dept");
                    String shiftName = "Shift:  " + documentSnapshot.getString("shift") +" Shift";
                    String yearName = "Year:  " +documentSnapshot.getString("year") + " Year";

                    userText.setText(userName);
                    userDept.setText(departmentName);
                    userYear.setText(yearName);
                    userShift.setText(shiftName);

                    RequestOptions placeHolder = new RequestOptions();
                    placeHolder.placeholder(R.drawable.default_image);

                    Glide.with(container.getContext()).setDefaultRequestOptions(placeHolder).load(userImaged).into(userImage);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(container.getContext(), "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }


        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Map<String,Object> tokenRemove = new HashMap<>();
                tokenRemove.put("token_id", FieldValue.delete());

                firebaseFirestore.collection("Users").document(userId).update(tokenRemove).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        firebaseAuth.signOut();

                        Intent loginIntent = new Intent(container.getContext(), LogInActivity.class);
                        startActivity(loginIntent);
                        getActivity().finish();

                        Toast.makeText(container.getContext(), "Successfully Logegd Out", Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });


        //profile retreiving




        return view;
    }



    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if(currentUser == null){
            sendToLoginActivity();
        }


    }

    private void sendToLoginActivity() {
        Intent loginACtivityIntent = new Intent(getContext(), LogInActivity.class);
        startActivity(loginACtivityIntent);
        getActivity().finish();
    }
}
