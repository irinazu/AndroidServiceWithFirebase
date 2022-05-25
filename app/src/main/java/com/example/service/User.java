package com.example.service;

import android.net.Uri;

public class User {
    public String name,sername,age,uri;
    public User(){}

    public User(String name, String sername, String age,String uri) {
        this.name = name;
        this.sername = sername;
        this.age = age;
        this.uri=uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSername() {
        return sername;
    }

    public void setSername(String sername) {
        this.sername = sername;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
