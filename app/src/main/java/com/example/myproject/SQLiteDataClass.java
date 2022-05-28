package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDataClass extends AppCompatActivity implements sqliteInterface {

    private RecyclerView recyclerView;
    FloatingActionButton fab;
    AlertDialog dialog_1;
    List<Student> student_sqlite_list;
    StudentsSQLiteRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_data_class);
        recyclerView = findViewById(R.id.firebase_users_recycler_view);
        fab = findViewById(R.id.add_students);
        student_sqlite_list = new ArrayList<Student>();
        DatabaseHelper db = new DatabaseHelper(this);
        student_sqlite_list = db.getAllDataFromSQlite();
        adapter = new StudentsSQLiteRecyclerAdapter(student_sqlite_list, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_data_into_sqlite();
            }
        });
    }

    private void add_data_into_sqlite() {
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.data_add_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(SQLiteDataClass.this);
        builder.setView(dialoglayout);
        dialog_1 = builder.create();
        dialog_1.show();
        //builder.show();
        String gender;
        EditText id = dialog_1.findViewById(R.id.student_id);
        EditText name = dialog_1.findViewById(R.id.student_name);
        EditText surname = dialog_1.findViewById(R.id.student_surname);
        EditText fatherName = dialog_1.findViewById(R.id.student_father_name);
        EditText nic = dialog_1.findViewById(R.id.student_national_id_number);
        EditText dob = dialog_1.findViewById(R.id.student_date_of_birth);
        RadioGroup radioGroup = dialog_1.findViewById(R.id.radioGroup);
//        RadioButton radioFemale = dialog_1.findViewById(R.id.radioFemale);
//        RadioButton radioMale = dialog_1.findViewById(R.id.radioMale);
        Button save = dialog_1.findViewById(R.id.save_data);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gender_1;
                int selectedButtonID = radioGroup.getCheckedRadioButtonId();
                RadioButton student_gender = dialog_1.findViewById(selectedButtonID);
                if (student_gender.getText().toString().equals("Male")) {
                    gender_1 = "Male";
                } else {
                    gender_1 = "Female";
                }
                Boolean isEmpty = !id.getText().toString().isEmpty() && !name.getText().toString().isEmpty() && !surname.getText().toString().isEmpty() && !fatherName.getText().toString().isEmpty() && !nic.getText().toString().isEmpty() && !gender_1.isEmpty() && !dob.getText().toString().isEmpty();
                //Creating Student Object here....
                //We can also use constructor here but we will use setter for setting values
                if (isEmpty) {
                    Student student = new Student();
                    student.setId(id.getText().toString());
                    student.setName(name.getText().toString());
                    student.setSurName(surname.getText().toString());
                    student.setFatherName(fatherName.getText().toString());
                    student.setNationalId(nic.getText().toString());
                    student.setGender(gender_1);
                    student.setDateOfBirth(dob.getText().toString());
                    DatabaseHelper db = new DatabaseHelper(SQLiteDataClass.this);
                    //Add data
                    long c = db.addData(student.getId(), student.getName(), student.getSurName(), student.getFatherName(), student.getNationalId(), student.getDateOfBirth(), student.getGender());
                    Log.d("TAGDB", String.valueOf(c));
                    if (c != -1) {
                        Toast.makeText(getApplicationContext(), "Successfully saved to sqlite", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), student.getName() + " " + student.getSurName() + " Added Successfully", Toast.LENGTH_SHORT).show();
                        dialog_1.dismiss();
                        student_sqlite_list = db.getAllDataFromSQlite();
                        adapter.notifyData(student_sqlite_list);
                    } else {
                        Toast.makeText(getApplicationContext(), "Data already present with this ID", Toast.LENGTH_SHORT).show();
                    }
//
                } else {
                    Toast.makeText(getApplicationContext(), "One of the field is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_for_sqlite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id_item = item.getItemId();
        switch (id_item) {

            case (R.id.weather_report): {
                Intent i = new Intent(getApplicationContext(), WeatherReportMainScreen.class);
                startActivity(i);
                break;
            }
            case (R.id.fetch): {
                fetchFirebaseData();
            }

        }
        return true;
    }

    @Override
    public void deleteRecode(String id, int position) {
        DatabaseHelper db = new DatabaseHelper(this);
        db.deleteDataFromSQlite(id);
        student_sqlite_list.remove(position);
        adapter.notifyData(student_sqlite_list);
        Toast.makeText(getApplicationContext(), "Data removed successfully", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void updateRecord(Student student, int position) {
        Student student1 = student_sqlite_list.get(position);
        update_data(student1);
    }

    private void update_data(Student student) {
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.data_add_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(SQLiteDataClass.this);
        builder.setView(dialoglayout);
        dialog_1 = builder.create();
        //builder.show();
        dialog_1.show();
        String gender;
        EditText id = dialog_1.findViewById(R.id.student_id);
        id.setText(student.getId());
        id.setEnabled(false);
        EditText name = dialog_1.findViewById(R.id.student_name);
        name.setText(student.getName());
        EditText surname = dialog_1.findViewById(R.id.student_surname);
        surname.setText(student.getSurName());
        EditText fatherName = dialog_1.findViewById(R.id.student_father_name);
        fatherName.setText(student.getFatherName());
        EditText nic = dialog_1.findViewById(R.id.student_national_id_number);
        nic.setText(student.getNationalId());
        EditText dob = dialog_1.findViewById(R.id.student_date_of_birth);
        dob.setText(student.getDateOfBirth());
        RadioGroup radioGroup = dialog_1.findViewById(R.id.radioGroup);
        RadioButton radioFemale = dialog_1.findViewById(R.id.radioFemale);
        RadioButton radioMale = dialog_1.findViewById(R.id.radioMale);
        if (student.getGender().equals("Male")) {
            radioMale.setChecked(true);
        } else {
            radioFemale.setChecked(true);
        }
        Button save = dialog_1.findViewById(R.id.save_data);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gender_1;
                int selectedButtonID = radioGroup.getCheckedRadioButtonId();
                RadioButton student_gender = dialog_1.findViewById(selectedButtonID);
                if (student_gender.getText().toString().equals("Male")) {
                    gender_1 = "Male";
                } else {
                    gender_1 = "Female";
                }
                Boolean isEmpty = !id.getText().toString().isEmpty() && !name.getText().toString().isEmpty() && !surname.getText().toString().isEmpty() && !fatherName.getText().toString().isEmpty() && !nic.getText().toString().isEmpty() && !gender_1.isEmpty() && !dob.getText().toString().isEmpty();
                //Creating Student Object here....
                //We can also use constructor here but we will use setter for setting values
                if (isEmpty) {
                    Student student = new Student();
                    student.setId(id.getText().toString());
                    student.setName(name.getText().toString());
                    student.setSurName(surname.getText().toString());
                    student.setFatherName(fatherName.getText().toString());
                    student.setNationalId(nic.getText().toString());
                    student.setGender(gender_1);
                    student.setDateOfBirth(dob.getText().toString());
                    DatabaseHelper db = new DatabaseHelper(SQLiteDataClass.this);
                    db.updateDataInSQlite(student.getId(), student.getName(), student.getSurName(), student.getFatherName(), student.getNationalId(), student.getDateOfBirth(), student.getGender());
                    Toast.makeText(getApplicationContext(), "Data updated Successfully", Toast.LENGTH_SHORT).show();
                    adapter.notifyData(db.getAllDataFromSQlite());
                    dialog_1.dismiss();


                } else {
                    Toast.makeText(getApplicationContext(), "One of the field is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void fetchFirebaseData() {
        Log.d("TAGIns", "INSIDE FETCH");
        FirebaseDatabase.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        // below line is used to get reference for our database.
        DatabaseReference databaseReference = firebaseDatabase.getReference("Student Info");
        databaseReference.child("Students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                List<Student> students = new ArrayList<>();
                for (DataSnapshot obj : datasnapshot.getChildren()) {

                    Student student = obj.getValue(Student.class);
                    Log.d("TAGIns", student.getGender());
                    students.add(student);

                }
                for (Student obj : students
                ) {
                    saveDataToSqlite(obj.getId(),
                            obj.getName(),
                            obj.getSurName(),
                            obj.getFatherName(),
                            obj.getNationalId(),
                            obj.getDateOfBirth(),
                            obj.getGender());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SQLiteDataClass.this,
                        error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveDataToSqlite(String id, String name, String surName,
                                  String fatherName, String nationalId, String dob, String gender) {
        DatabaseHelper db = new DatabaseHelper(this);
        long insertData = db.addData(id, name, surName, fatherName, nationalId, dob, gender);
        // clearFields();

        if (insertData != -1) {
            Toast.makeText(this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
            student_sqlite_list = db.getAllDataFromSQlite();
            adapter.notifyData(student_sqlite_list);
        } else {
            Toast.makeText(this, "Data Insertion Failed", Toast.LENGTH_SHORT).show();
        }

    }

}