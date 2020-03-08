package com.prashantchanne.chatbox;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SendActivity extends AppCompatActivity {

    private TextView user_id;
    private String userId;

    private Spinner shiftSpinner;
    private Spinner yearSpinner;


    private EditText message;
    private Button sendButton;

    private String currentUser;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private ProgressBar progressBar;
    private String userName;

    private Spinner sppinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

      //  user_id = findViewById(R.id.userIdText);

        userId = getIntent().getStringExtra("user_id");
       // user_id.setText(userId);

        userName = getIntent().getStringExtra("user_name");
        user_id.setText("Send to "+ userName);

        message = findViewById(R.id.enterNotMsgText);
        sendButton = findViewById(R.id.sendNotButton);

        currentUser = firebaseAuth.getCurrentUser().getUid();
        progressBar = findViewById(R.id.progress);

        sppinner = findViewById(R.id.spinn);
        shiftSpinner = findViewById(R.id.dropShift);
        yearSpinner = findViewById(R.id.drop);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SendActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.names));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sppinner.setAdapter(arrayAdapter);


        ArrayAdapter<String> shiftArrayAdapter = new ArrayAdapter<>(SendActivity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.shifts));

        shiftArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        shiftSpinner.setAdapter(shiftArrayAdapter);


        ArrayAdapter<String> yearArrayAdapter = new ArrayAdapter<>(SendActivity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.year));

        yearArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        yearSpinner.setAdapter(yearArrayAdapter);



        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String messageText = message.getText().toString();
                String deptText = sppinner.getSelectedItem().toString();
                String shiftText = shiftSpinner.getSelectedItem().toString();
                String yearText = yearSpinner.getSelectedItem().toString();

                if(!TextUtils.isEmpty(messageText)) {

                    if (deptText.contentEquals("Select your Department")
                            || shiftText.contentEquals("Select your Shift")
                            || yearText.contentEquals("Select your Year")) {

                        Toast.makeText(SendActivity.this, "Please select all the fields", Toast.LENGTH_SHORT).show();
                        return;

                    }else {


                        progressBar.setVisibility(View.VISIBLE);


                        final Map<String, Object> notificationMessage = new HashMap<>();
                        notificationMessage.put("message", messageText);
                        notificationMessage.put("from", currentUser);


                        firebaseFirestore.collection("Users")
                                .whereEqualTo("dept", deptText)
                                .whereEqualTo("shift", shiftText)
                                .whereEqualTo("year", yearText)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                                        firebaseFirestore.collection("Users/" + userId + "/notification").add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {

                                                progressBar.setVisibility(View.INVISIBLE);
                                                message.setText(" ");
                                                Toast.makeText(SendActivity.this, "Notification Sent", Toast.LENGTH_SHORT).show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                progressBar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(SendActivity.this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });

                                    }
                                });

                    }
                }else{
                    Toast.makeText(SendActivity.this, "Please Enter Message!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
