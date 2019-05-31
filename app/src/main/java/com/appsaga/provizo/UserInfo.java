package com.appsaga.provizo;

public class UserInfo {
    private String email,name,number,dob,gender,verified;
    public  UserInfo(){}
    public UserInfo(String email,String name,String number,String dob,String gender,String verified)
    {
        this.name=name;
        this.number=number;
        this.email=email;
        this.gender=gender;
        this.verified=verified;
        this.dob=dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }
}
