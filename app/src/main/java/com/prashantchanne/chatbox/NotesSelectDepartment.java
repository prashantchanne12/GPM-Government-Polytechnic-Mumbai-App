package com.prashantchanne.chatbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class NotesSelectDepartment extends AppCompatActivity {

    private Spinner departments,years;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_select_department);

        departments = findViewById(R.id.NotesSpinnerSelectDept);
        years = findViewById(R.id.NotesSpinnerYear);
        next=findViewById(R.id.nextButton);

        //----- Department Spinner ------
        ArrayAdapter<String> deptArrayAdapter = new ArrayAdapter<>(NotesSelectDepartment.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.names));

        deptArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        departments.setAdapter(deptArrayAdapter);


        //----- Year Spinner -------
        ArrayAdapter<String> YearArrayAdapter = new ArrayAdapter<>(NotesSelectDepartment.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.year));

        YearArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        years.setAdapter(YearArrayAdapter);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String dept = departments.getSelectedItem().toString();
                String year = years.getSelectedItem().toString();

                if(dept.equals("Select your Department") || year.equals("Select your year")){
                    Toast.makeText(NotesSelectDepartment.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {

                    Intent gotoUploadActivityIntent = new Intent(NotesSelectDepartment.this,UploadDocuments.class);
                    gotoUploadActivityIntent.putExtra("dept",dept);
                    gotoUploadActivityIntent.putExtra("year",year);
                    startActivity(gotoUploadActivityIntent);

                }



            }
        });


    }
}
