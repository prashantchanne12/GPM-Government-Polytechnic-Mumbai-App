package com.prashantchanne.chatbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

public class StatisticsSelect extends AppCompatActivity {

    private Spinner dept;
    private Spinner shift;
    private Spinner year;
    private Spinner addYear;

    private ProgressBar progressBar;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_select);

        dept = findViewById(R.id.statisticsSpinnerSelectDept);
        shift = findViewById(R.id.statisticsSpinnerShift);
        year = findViewById(R.id.statisticsSpinnerYear);
        addYear = findViewById(R.id.statisticsSpinnerAddYear);

        progressBar = findViewById(R.id.statisticsProgress);
        submit = findViewById(R.id.statisticsSubmitButton);

        //----------- Select Department -----------//
        ArrayAdapter<CharSequence> deptAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.names2,
                android.R.layout.simple_list_item_1);
        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dept.setAdapter(deptAdapter);

        //----------- Select Shift -----------//
        ArrayAdapter<CharSequence> shiftAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.shifts2,
                android.R.layout.simple_list_item_1);
        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shift.setAdapter(shiftAdapter);

        //----------- Select Year -----------//
        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.year2,
                android.R.layout.simple_list_item_1);
        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(yearAdapter);

        //----------- Select Admission Year -----------//
        ArrayAdapter<CharSequence> addYearAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.yearText2,
                android.R.layout.simple_list_item_1);
        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addYear.setAdapter(addYearAdapter);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String Department = dept.getSelectedItem().toString();
                String Shift = shift.getSelectedItem().toString();
                String Year = year.getSelectedItem().toString();
                String AddYear = addYear.getSelectedItem().toString();


                if (Department.equals("Select Department") ||
                        Shift.equals("Select shift") ||
                        Year.equals("Select year") ||
                        AddYear.equals("Select admission year")) {

                    Toast.makeText(StatisticsSelect.this, "Please select all fields", Toast.LENGTH_SHORT).show();
                    return;

                } else {

                    Intent intent = new Intent(StatisticsSelect.this, StatisticsSubject.class);
                    intent.putExtra("dept", Department);
                    intent.putExtra("shift", Shift);
                    intent.putExtra("year", Year);
                    intent.putExtra("addYear", AddYear);
                    startActivity(intent);

                }
            }
        });


    }
}
