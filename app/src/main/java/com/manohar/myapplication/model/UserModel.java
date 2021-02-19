package com.manohar.myapplication.model;

public class UserModel {

    public String firstname;
    public String lastname;
    public String dob;
    public String country;
    public String state;
    public String hometown;
    public String phonenumber;
    public Long age;
    public String gender;
    public String image;


    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public UserModel()

    {

    }

    public UserModel( String firstname,
             String lastname,
             String dob,
             String country,
             String state,
             String hometown,
             String phonenumber,
             Long age,
             String gender,
             String image)

    {
        this.firstname = firstname;
        this.lastname = lastname;
        this.dob = dob;
        this.country = country;
        this.state = state;
        this.hometown = hometown;
        this.phonenumber = phonenumber;
        this.age = age;
        this.gender = gender;
        this.image = image;



    }



}
