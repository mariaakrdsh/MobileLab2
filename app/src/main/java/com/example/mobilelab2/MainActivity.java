package com.example.mobilelab2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DBHandler dbHandler;
    Button btnAddStudent, btnGetAllFromDB, getPercentageOfStudentsWithAverageAbove60, getStudentsWithAverageAbove60, btnContacts,btnAbout;
    ArrayAdapter studentsArrayAdapter;
    ListView studentsList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        dbHandler = new DBHandler(this);

        studentsList = findViewById(R.id.studentsList);

        studentsArrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, dbHandler.getAll());
        studentsList.setAdapter(studentsArrayAdapter);

        btnAddStudent = findViewById(R.id.btnAddStudentToDB);
        btnAddStudent.setOnClickListener(view -> {
            addStudent();

            studentsArrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, dbHandler.getAll());
            studentsList.setAdapter(studentsArrayAdapter);
        });


        btnContacts = findViewById(R.id.btnContacts);
        btnContacts.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ContactActivity.class);
            startActivity(intent);
        });

        btnGetAllFromDB = findViewById(R.id.btnGetAll);
        btnGetAllFromDB.setOnClickListener(view -> {
            studentsArrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, dbHandler.getAll());
            studentsList.setAdapter(studentsArrayAdapter);
        });

        btnAbout = findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, DeveloperActivity.class);
            startActivity(intent);
        });

        getPercentageOfStudentsWithAverageAbove60 = findViewById(R.id.getPercentageOfStudentsWithAverageAbove60);
        getPercentageOfStudentsWithAverageAbove60.setOnClickListener(view -> {
            ArrayList<Double> doubles = new ArrayList<>();
            doubles.add(dbHandler.getPercentageOfStudentsWithAverageAbove60());
            studentsArrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, doubles);
            studentsList.setAdapter(studentsArrayAdapter);
        });

        getStudentsWithAverageAbove60 = findViewById(R.id.getStudentsWithAverageAbove60);
        getStudentsWithAverageAbove60.setOnClickListener(view -> {
            studentsArrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, dbHandler.getStudentsWithAverageAbove60());
            studentsList.setAdapter(studentsArrayAdapter);
        });
    }

    public void addStudent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View view = inflater.inflate(R.layout.add_student, null);
        builder.setView(view);

        final EditText pibEditText = view.findViewById(R.id.editTextPIB);
        final EditText score1EditText = view.findViewById(R.id.score1);
        final EditText score2EditText = view.findViewById(R.id.score2);
        final EditText addressEditText = view.findViewById(R.id.address);

        builder.setPositiveButton("Add", (dialog, which) -> {
            try {
                String pib = pibEditText.getText().toString();
                int score1 = Integer.parseInt(score1EditText.getText().toString());
                int score2 = Integer.parseInt(score2EditText.getText().toString());
                String address = addressEditText.getText().toString();

                if (pib != null && !pib.isEmpty()) {
                    Student newStudent = new Student(pib, score1, score2, address);
                    dbHandler.addStudent(newStudent);
                } else {
                    Toast.makeText(MainActivity.this, "PIB or Address can't be empty", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e){
                Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

}
