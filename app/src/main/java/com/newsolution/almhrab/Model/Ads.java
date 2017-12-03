package com.newsolution.almhrab.Model;

/**
 * Created by hp on 8/4/2016.
 */
public class Ads {
    int id;
    String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Ads(int id,String text){
        this.id=id;
        this.text=text;
    }
}
