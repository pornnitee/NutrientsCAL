package com.example.starius.project;

/**
 * Created by STARIUS on 1/19/2018.
 */
//this is very simple class and it only contains the user attributes, a constructor and the getters
// you can easily do this by right click -> generate -> constructor and getters
public class User {
    private String username, password,email, weight,height,age,gender;

    public User(String username,String password, String email,String weight,String height, String gender, String age) {
       // this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getWeight() {
        return weight;
    }

    public String getHeight() {
        return height;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

}

