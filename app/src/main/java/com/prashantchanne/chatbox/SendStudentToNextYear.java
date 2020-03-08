package com.prashantchanne.chatbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class SendStudentToNextYear extends AppCompatActivity implements View.OnLongClickListener {

    public String year;
    private String shift;
    private String tempDept;
    private String dept;
    private String addYear;
    private RecyclerView recyclerView;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private List<StudentUsers> studentList;
    private ArrayList<StudentUsers> selectionList;
    private ArrayList<StudentUsers> notSelectedList;
    int counter = 0;

    private Toolbar toolbar;
    private TextView counterTextView;
    boolean isInActionMode = false;

    private SendStudentRecyclerAdapter sendStudentRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_student_to_next_year);


        year = getIntent().getStringExtra("year");
        shift = getIntent().getStringExtra("shift");
        tempDept = getIntent().getStringExtra("dept");
        dept = tempDept;
        addYear = getIntent().getStringExtra("add_year");

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.sendStudentRecycler);

        if(tempDept.equals("Information Technology")){
            dept = "IT";
        }

        if(tempDept.equals("Computer Engineering")){
            dept = "CO";
        }

        if(tempDept.equals("Electronics Engineering")){
            dept = "EC";
        }

        if(tempDept.equals("Electrical Engineering")){
            dept = "EE";
        }

        if(tempDept.equals("Civil Engineering")){
            dept = "CE";
        }

        if(tempDept.equals("Instrumentation Engineering")){
            dept = "IS";
        }

        toolbar = findViewById(R.id.toolbarSend);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Student");


        studentList = new ArrayList<>();
        studentList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sendStudentRecyclerAdapter = new SendStudentRecyclerAdapter(studentList, this);
        //adapter = new StudentRecyclerAdapter(studentList,this);
       // recyclerView.addItemDecoration(new SimpleDivderItemDecoration(this));
        recyclerView.setAdapter(sendStudentRecyclerAdapter);

        counterTextView = findViewById(R.id.counterText);
        counterTextView.setVisibility(View.GONE);

        selectionList = new ArrayList<>();
        notSelectedList = new ArrayList<>();

    }




    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth != null) {
            studentList.clear(); //It will avoid the loading of same item again and agian.
            firebaseFirestore.collection("Users")
                    .whereEqualTo("dept",dept)
                    .whereEqualTo("shift",shift)
                    .whereEqualTo("year",year)
                    .whereEqualTo("add_year",addYear)
                    .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                            if(e!=null){
                                Toast.makeText(getApplicationContext(), "Error! while loading", Toast.LENGTH_SHORT).show();
                                return;
                            }


                            if(documentSnapshots!=null)
                                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                                    //String userId = doc.getDocument().getId();
                                    StudentUsers users = doc.getDocument().toObject(StudentUsers.class);
                                    studentList.add(users);

                                    sendStudentRecyclerAdapter.notifyDataSetChanged(); // to refresh the recycle adapter to know the data is changed.
                                }

                        }
                    });


        }
    }

    @Override
    public boolean onLongClick(View view) {

        toolbar.inflateMenu(R.menu.menu_actionmode_student_list);
        counterTextView.setVisibility(View.VISIBLE);
        isInActionMode = true;
        sendStudentRecyclerAdapter.notifyDataSetChanged();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_student_list,menu);
        return true;
    }

    public void preapareSelection(View view,int position) {
        //Toast.makeText(this, "one item selected", Toast.LENGTH_SHORT).show();


        if(((CheckBox)view).isChecked()){
            selectionList.add(studentList.get(position));
            counter+=1;
            updateCounter(counter);
        }
        else {
            selectionList.remove(studentList.get(position));
            counter-=1;
            updateCounter(counter);
        }


    }

    public void updateCounter(int counter){

        if(counter == 0){
            counterTextView.setText(counter+" item selected");
        }
        else {
            counterTextView.setText(counter+" item selected");
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.submitStudents){

            SendStudentRecyclerAdapter adapter = sendStudentRecyclerAdapter;
            adapter.submitStudents(selectionList);

            clearActionMode();
            isInActionMode = false;


        }


        else if(item.getItemId() == android.R.id.home){

            clearActionMode();
            sendStudentRecyclerAdapter.notifyDataSetChanged();

        }

        return true;
    }


    public void clearActionMode(){
        isInActionMode = false;
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_student_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        counterTextView.setVisibility(View.GONE);
        counterTextView.setText("0 item selected");
        counter=0;

    }

    @Override
    public void onBackPressed() {

        if(isInActionMode){
            clearActionMode();
            sendStudentRecyclerAdapter.notifyDataSetChanged();
            gotoPrevious();
        }
        else {
            super.onBackPressed();
        }
    }


    private void gotoPrevious(){
        startActivity(new Intent(getApplicationContext(),SendStudentYear.class));
    }

}


