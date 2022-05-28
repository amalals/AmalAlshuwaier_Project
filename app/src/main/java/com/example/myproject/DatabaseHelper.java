package com.example.myproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "DatabaseHelper";

    public static final String TABLE_NAME = "student_table";

    public static final String COL1 = "studentid";
    public static final String COL2 = "name";
    public static final String COL3 = "surname";
    public static final String COL4 = "fathername";
    public static final String COL5 = "nationalid";
    public static final String COL6 = "dob";
    public static final String COL7 = "gender";


    public DatabaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table = "CREATE TABLE " + TABLE_NAME + " (" +
                COL1 + " TEXT PRIMARY KEY, " +
                COL2 + " TEXT NOT NULL, " +
                COL3 + " TEXT NOT NULL, " +
                COL4 + " TEXT NOT NULL, " +
                COL5 + " INTEGER NOT NULL, " +
                COL6 + " TEXT NOT NULL, " +
                COL7 + " TEXT NOT NULL);";
        db.execSQL(table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addData(String id, String name, String surname, String fathername,
                        String nationalid, String dob, String gender) {
        long ch = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, id);
        contentValues.put(COL2, name);
        contentValues.put(COL3, surname);
        contentValues.put(COL4, fathername);
        contentValues.put(COL5, nationalid);
        contentValues.put(COL6, dob);
        contentValues.put(COL7, gender);

        Cursor c = null;
        boolean tableExists = false;
        /* get cursor on it */
        try {
            c = db.query(TABLE_NAME, null,
                    null, null, null, null, null);
            tableExists = true;
        } catch (Exception e) {
        }
        if (tableExists) {
            ch = db.insert("student_table", null, contentValues);
            Cursor cu = db.rawQuery("SELECT * FROM student_table ", null);
            if (cu.moveToFirst()) {
                do {
                    // Passing values
                    String column1 = cu.getColumnName(0);
                    String column2 = cu.getColumnName(1);
                    String column3 = cu.getColumnName(2);
                    String column4 = cu.getColumnName(3);
                    String column5 = cu.getColumnName(4);
                    String column6 = cu.getColumnName(5);
                    String column7 = cu.getColumnName(6);
                    String value1 = cu.getString(0);
                    String value2 = cu.getString(1);
                    String value3 = cu.getString(2);
                    String value4 = cu.getString(3);
                    int value5 = cu.getInt(4);
                    String value6 = cu.getString(5);
                    String value7 = cu.getString(6);
                    // Do something Here with values
                } while (cu.moveToNext());
            }
            cu.close();
            return ch;
        } else {
            return ch;
        }
    }

    public ArrayList<Student> getAllDataFromSQlite() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cu = db.rawQuery("SELECT * FROM student_table ", null);
        ArrayList<Student> studentDetails = new ArrayList<>();
        if (cu.moveToFirst()) {
            do {

                Student student = new Student();
                student.setId(cu.getString(0));
                student.setName(cu.getString(1));
                student.setSurName(cu.getString(2));
                student.setFatherName(cu.getString(3));
                student.setNationalId(cu.getString(4));
                student.setDateOfBirth(cu.getString(5));
                student.setGender(cu.getString(6));

                studentDetails.add(student);
            } while (cu.moveToNext());
        }
        cu.close();

        return studentDetails;
    }

    public void deleteDataFromSQlite(String id) {

        String deleteQueury = "DELETE FROM " + TABLE_NAME + " WHERE studentid =" + "'" + id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(deleteQueury);
    }

    public void updateDataInSQlite(String id, String name, String surname, String fathername,
                                   String nationalid, String dob, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();


        String updateQueury = "UPDATE student_table SET name= " + "'" + name + "'," + "surname= " + "'" + surname + "',"
                + "fathername= " + "'" + fathername + "',"
                + "nationalid= " + "'" + nationalid + "',"
                + "dob= " + "'" + dob + "',"
                + "gender= " + "'" + gender + "'"
                + " WHERE studentid= " + "'" + id + "'";
        db.execSQL(updateQueury);

    }
}


