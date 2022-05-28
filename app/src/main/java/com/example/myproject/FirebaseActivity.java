package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class FirebaseActivity extends AppCompatActivity implements OnClickAction {
    FloatingActionButton add_student_data;
    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;
    EditText searchById;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;
    static StudentsRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    static List<Student> students_list_firebase;
    AlertDialog dialog_1;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Firebase Data");
        progressDialog.setMessage("Loading");
        // below line is used to get the
        // instance of our rebase database.
        FirebaseDatabase.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("Student Info");
        recyclerView = findViewById(R.id.firebase_users_recycler_view);
        //students = new ArrayList<Student>();
        add_student_data = findViewById(R.id.add_students);
        students_list_firebase = new ArrayList<Student>();
        adapter = new StudentsRecyclerAdapter(students_list_firebase, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        progressDialog.show();
        searchById = findViewById(R.id.searchMessage);
        searchById.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchData(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        databaseReference.child("Students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Student student = snapshot.getValue(Student.class);
                students_list_firebase.clear();
                Log.d("TAG", String.valueOf(snapshot.getChildren()));
                for (DataSnapshot obj : snapshot.getChildren()) {
                    Student student = obj.getValue(Student.class);
                    students_list_firebase.add(student);
                    adapter.notifyData(students_list_firebase);

                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        add_student_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData();
            }
        });

    }

    private void addData() {
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.data_add_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(FirebaseActivity.this);
        builder.setView(dialoglayout);
        dialog_1 = builder.create();
        //builder.show();
        dialog_1.show();
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
                    databaseReference.child("Students").child(student.getId()).setValue(student);
                    Toast.makeText(getApplicationContext(), "Data inserted Successfully", Toast.LENGTH_SHORT).show();
                    //recreate();
                    dialog_1.dismiss();
//
                } else {
                    Toast.makeText(getApplicationContext(), "One of the field is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public void openStudentsDetails(Student student) {
        Intent i = new Intent(getApplicationContext(), showStudentsDetails.class);
        i.putExtra("Student", student);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_for_main, menu);
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
            case (R.id.sqlite): {
                Intent i = new Intent(getApplicationContext(), SQLiteDataClass.class);
                startActivity(i);
                break;
            }

        }
        return true;
    }
    private void searchData(CharSequence charSequence) {
        ArrayList<Student> mStudents = new ArrayList<>();
        //users = new ArrayList<>();


        for (Student student : students_list_firebase) {
            //Log.e("TAG", "searchData: " +  searchText );
            if (student.getId().toLowerCase().contains(charSequence)) {
                Log.e("TAG", "searchData: " + charSequence);
                mStudents.add(student);

            }
        }
        adapter = new StudentsRecyclerAdapter(mStudents,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}