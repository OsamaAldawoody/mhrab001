package com.newsolution.almhrab.Model;

import com.newsolution.almhrab.AppConstants.Constants;

/**
 * Created by hp on 9/7/2017.
 */

public class Users {
    int Id;
    String FullName;

    String UserName ;
    String Mobile ;
    String Email ;
    String GUID ;
    int IdCity ;
    String MyName;
    String img;
    public Users(int id,String FullName,String UserName,String Mobile,String Email,String GUID,int IdCity,String MyName,String img){
        this.Id=id;
        this.FullName=FullName;
        this.UserName=UserName;
        this.Mobile=Mobile;
        this.Email=Email;
        this.GUID=GUID;
        this.IdCity=IdCity;
        this.MyName=MyName;
        this.img=img;
    }
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGUID() {
        return GUID;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    public int getIdCity() {
        return IdCity;
    }

    public void setIdCity(int idCity) {
        IdCity = idCity;
    }

    public String getMyName() {
        return MyName;
    }

    public void setMyName(String myName) {
        MyName = myName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
