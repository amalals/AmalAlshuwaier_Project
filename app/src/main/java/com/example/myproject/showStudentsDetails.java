package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class showStudentsDetails extends AppCompatActivity {
    TextView name, father_name, surname, nationalID, DOB, gender, id;
    AlertDialog dialog_1;
    ArrayList<Student> mStudents;
    ProgressDialog pDialog;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Student student;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_students_details);
        name = findViewById(R.id.name);
        father_name = findViewById(R.id.F_name);
        surname = findViewById(R.id.surName);
        nationalID = findViewById(R.id.nationID);
        DOB = findViewById(R.id.DOB);
        gender = findViewById(R.id.gender);
        id = findViewById(R.id.id);
        Intent i = getIntent();
        db = new DatabaseHelper(this);
        pDialog = new ProgressDialog(showStudentsDetails.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Student Info");
        pDialog.setMessage("Loading...");
        student = (Student) i.getSerializableExtra("Student");
        name.append(student.getName());
        father_name.append(student.getFatherName());
        surname.append(student.getSurName());
        nationalID.append(student.getNationalId());
        DOB.append(student.getDateOfBirth());
        gender.append(student.getGender());
        id.append(student.getId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id_item = item.getItemId();
        switch (id_item) {
            case (R.id.edit): {
                update_data();
                break;
            }
            case (R.id.delete): {
                delete_record();
                break;
            }
            case (R.id.import_data): {
                import_data_to_sqlite();
                break;
            }
            case (R.id.weather_report): {
                Intent i = new Intent(getApplicationContext(), WeatherReportMainScreen.class);
                startActivity(i);
                break;

            }

        }
        return true;
    }

    private void delete_record() {
        databaseReference.child("Students").child(student.getId()).removeValue();
        Toast.makeText(this, "Record Deleted Successfully", Toast.LENGTH_SHORT).show();
        searchData(student.getId());
        FirebaseActivity.students_list_firebase = mStudents;
        FirebaseActivity.adapter.notifyData(FirebaseActivity.students_list_firebase);
        finish();
    }

    private void import_data_to_sqlite() {
        long c = db.addData(student.getId(), student.getName(), student.getSurName(), student.getFatherName(), student.getNationalId(), student.getDateOfBirth(), student.getGender());
        Log.d("TAGDB", String.valueOf(c));
        if (c != -1) {
            Toast.makeText(getApplicationContext(), "Successfully saved to sqlite", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Data already present there", Toast.LENGTH_SHORT).show();
        }
    }

    private void update_data() {
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.data_add_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(showStudentsDetails.this);
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
                pDialog.show();
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
                    pDialog.hide();
                    Toast.makeText(getApplicationContext(), "Data inserted Successfully", Toast.LENGTH_SHORT).show();
                    dialog_1.dismiss();
                    finish();
                } else {
                    pDialog.hide();
                    Toast.makeText(getApplicationContext(), "One of the field is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void searchData(String id) {
        mStudents = new ArrayList<>();
        //users = new ArrayList<>();


        for (Student student : FirebaseActivity.students_list_firebase) {
            //Log.e("TAG", "searchData: " +  searchText );
            if (student.getId().toLowerCase().contains(id)) {
                mStudents.add(student);

            }
        }
    }

}