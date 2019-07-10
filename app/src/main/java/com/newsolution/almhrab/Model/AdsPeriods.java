package com.newsolution.almhrab.Model;

import java.io.Serializable;

/**
 * Created by hp on 8/4/2016.
 */
public class AdsPeriods implements Serializable {
    private int id;
    private  int day;
    private  int advId;
    private  String StartTime;
    private  String EndTime;
    private  String periodsId;
    private String days;
    private  boolean isAdded;
    private  String EndDate;
    private String StartDate;

    public String getPeriodsId() {
        return periodsId;
    }

    public void setPeriodsId(String periodsId) {
        this.periodsId = periodsId;
    }


    public AdsPeriods(int advId, String startTime, String endTime, String startDate, String endDate,
                      String days,String periodsId, boolean added) {
        this.StartDate=startDate;
        this.StartTime=startTime;
        this.EndTime=endTime;
        this.EndDate=endDate;
        this.isAdded=added;
        this.advId=advId;
        this.days=days;
        this.periodsId=periodsId;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }



    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }


    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getAdvId() {
        return advId;
    }

    public void setAdvId(int advId) {
        this.advId = advId;
    }
    public AdsPeriods() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
