package com.newsolution.almhrab.Model;

/**
 * Created by hp on 9/17/2017.
 */

public  class City {
    public City() {
    }

    public City(int id, String Name, String NameEn, int lon1, int lon2, int lat1, int lat2, String updatedAt, int isDeleted) {
        this.id = id;
        this.Name = Name;
        this.NameEn = NameEn;
        this.lon1 = lon1;
        this.lon2 = lon2;
        this.lat1 = lat1;
        this.lat2 = lat2;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
    }

    public City(int id, String Name, String NameEn, int lon1, int lon2, int lat1, int lat2, int isDeleted) {
        this.id = id;
        this.Name = Name;
        this.NameEn = NameEn;
        this.lon1 = lon1;
        this.lon2 = lon2;
        this.lat1 = lat1;
        this.lat2 = lat2;
        this.isDeleted = isDeleted;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNameEn() {
        return NameEn;
    }

    public void setNameEn(String nameEn) {
        NameEn = nameEn;
    }

    public int getLon1() {
        return lon1;
    }

    public void setLon1(int lon1) {
        this.lon1 = lon1;
    }

    public int getLon2() {
        return lon2;
    }

    public void setLon2(int lon2) {
        this.lon2 = lon2;
    }

    public int getLat1() {
        return lat1;
    }

    public void setLat1(int lat1) {
        this.lat1 = lat1;
    }

    public int getLat2() {
        return lat2;
    }

    public void setLat2(int lat2) {
        this.lat2 = lat2;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    int id;
    String Name;
    String NameEn;
    int lon1;
    int lon2;
    int lat1;
    int lat2;
    String updatedAt;
    int isDeleted;
}
