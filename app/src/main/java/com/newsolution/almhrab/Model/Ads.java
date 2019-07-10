package com.newsolution.almhrab.Model;

import java.io.Serializable;

/**
 * Created by hp on 8/4/2016.
 */
public class Ads implements Serializable {
    private int id;
    private int MasjedID;
    private int Type;
    private String Title;
    private String Text;
    private String Image;
    private String Video;
    private String StartDate;
    private String EndDate;

    private  String StartTime;
    private String EndTime;

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public Ads() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMasjedID() {
        return MasjedID;
    }

    public void setMasjedID(int masjedID) {
        MasjedID = masjedID;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getVideo() {
        return Video;
    }

    public void setVideo(String video) {
        Video = video;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String start) {
        StartDate = start;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String end) {
        EndDate = end;
    }

}
