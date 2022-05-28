package com.example.myproject;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Date;

public class Student implements Serializable {
    //Declaring data properties of students for storing into firebase real time database
    private String id;
    private String name, surName, fatherName, nationalId, dateOfBirth, gender;
    private String key;

    //Parameterized constructor
    public Student(String id, String name, String surName, String fatherName, String nationalId, String dateOfBirth, String gender) {
        this.id = id;
        this.name = name;
        this.surName = surName;
        this.fatherName = fatherName;
        this.nationalId = nationalId;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;

    }
    //Default constructor

    public Student() {

    }

    //Getters and setters for setting values of these variables
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }


}
