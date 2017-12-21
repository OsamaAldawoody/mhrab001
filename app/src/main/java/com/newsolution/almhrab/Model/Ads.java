package com.newsolution.almhrab.Model;

/**
 * Created by hp on 8/4/2016.
 */
public class Ads {
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

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String start) {
        StartTime = start;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String end) {
        EndTime = end;
    }

    int id;
    int MasjedID;
    int Type;
    String Title;
    String Text;
    String Image;
    String Video;
    String StartTime;
    String EndTime;

    public boolean isSaturday() {
        return Saturday;
    }

    public void setSaturday(boolean saturday) {
        Saturday = saturday;
    }

    public boolean isSunday() {
        return Sunday;
    }

    public void setSunday(boolean sunday) {
        Sunday = sunday;
    }

    public boolean isMonday() {
        return Monday;
    }

    public void setMonday(boolean monday) {
        Monday = monday;
    }

    public boolean isTuesday() {
        return Tuesday;
    }

    public void setTuesday(boolean tuesday) {
        Tuesday = tuesday;
    }

    public boolean isWednesday() {
        return Wednesday;
    }

    public void setWednesday(boolean wednesday) {
        Wednesday = wednesday;
    }

    public boolean isThursday() {
        return Thursday;
    }

    public void setThursday(boolean thursday) {
        Thursday = thursday;
    }

    public boolean isFriday() {
        return Friday;
    }

    public void setFriday(boolean friday) {
        Friday = friday;
    }

    boolean Saturday;
    boolean Sunday;
    boolean Monday;
    boolean Tuesday;
    boolean Wednesday;
    boolean Thursday;
    boolean Friday;

}
