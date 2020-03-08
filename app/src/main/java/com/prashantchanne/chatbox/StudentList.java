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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StudentList extends AppCompatActivity implements View.OnLongClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<StudentUsers> studentList;

    private ArrayList<StudentUsers> selectionList;
    private ArrayList<StudentUsers> notSelectedList;

    int counter = 0;

    private StudentRecyclerAdapter studentRecyclerAdapter;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

     String departmentText;
     String shiftText;
     String yearText;
     String addmissionYearText;

     String tempdept;

    private Toolbar toolbar;

    boolean isInActionMode = false;
    private TextView counterTextView;

    String rollId;
    String shiftId;
    String deptId;

    String subject;
    String lop;
    String lectures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);


        toolbar = findViewById(R.id.toolbarStudent);
        toolbar.setTitle("GPM");
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.studentRecycler);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        departmentText = getIntent().getStringExtra("dept"); //IT

        tempdept=departmentText;

        if(departmentText.equals("IT")){
            tempdept = "IF";
        }

        subject = getIntent().getStringExtra("subject");
        lop = getIntent().getStringExtra("lop");
        lectures = getIntent().getStringExtra("lectures");

        shiftText = getIntent().getStringExtra("shift"); //First Second
        yearText = getIntent().getStringExtra("year"); //First Second Third
        addmissionYearText = getIntent().getStringExtra("addYear"); //16 17...

        String test= tempdept +"\n"+ shiftText +"\n"+ yearText +"\n"+ addmissionYearText;

//        Toast.makeText(this, " "+test, Toast.LENGTH_SHORT).show();

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());


        studentList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentRecyclerAdapter = new StudentRecyclerAdapter(studentList, this,lectures,shiftText,addmissionYearText,tempdept);
        //adapter = new StudentRecyclerAdapter(studentList,this);
       // recyclerView.addItemDecoration(new SimpleDivderItemDecoration(this));
        recyclerView.setAdapter(studentRecyclerAdapter);

        counterTextView = findViewById(R.id.counterText);
        counterTextView.setVisibility(View.GONE);

        selectionList = new ArrayList<>();
        notSelectedList = new ArrayList<>();
    }
    @Override
    public void onStart() {
        super.onStart();

        if (firebaseAuth != null) {
            studentList.clear(); //It will avoid the loading of same item again and agian.
            firebaseFirestore.collection("Users")
                    .whereEqualTo("dept",departmentText)
                    .whereEqualTo("shift",shiftText)
                    .whereEqualTo("year",yearText)
                    .whereEqualTo("add_year",addmissionYearText)
                    .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if(e!=null){
                        Toast.makeText(StudentList.this, "Error! while loading", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    if(documentSnapshots!=null)
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            //String userId = doc.getDocument().getId();
                            StudentUsers users = doc.getDocument().toObject(StudentUsers.class);
                            studentList.add(users);

                            studentRecyclerAdapter.notifyDataSetChanged(); // to refresh the recycle adapter to know the data is changed.
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
        studentRecyclerAdapter.notifyDataSetChanged();
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

            StudentRecyclerAdapter adapter = studentRecyclerAdapter;
            adapter.submitStudentAttendance(selectionList,notSelectedList);

            clearActionMode();
            isInActionMode = false;


        }


        else if(item.getItemId() == android.R.id.home){

            clearActionMode();
            studentRecyclerAdapter.notifyDataSetChanged();

        }

        return true;
    }


    public void clearActionMode(){

        isInActionMode = false;
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_student_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        counterTextView.setVisibility(View.GONE);
        counter=0;

    }

    @Override
    public void onBackPressed() {

        if(isInActionMode){
            clearActionMode();
            studentRecyclerAdapter.notifyDataSetChanged();
            gotoPrevious();
        }
        else {
            super.onBackPressed();

        }
    }


    private void gotoPrevious(){

        Intent intent = new Intent(getApplicationContext(),AttendanceActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);


    }

}
