package com.newsolution.almhrab.Model;

import java.io.Serializable;

/**
 * Created by hp on 8/4/2016.
 */
public class AdsPeriods implements Serializable {
    int id;
    int day;
    int advId;
    String StartTime;
    String EndTime;

    public String getPeriodsId() {
        return periodsId;
    }

    public void setPeriodsId(String periodsId) {
        this.periodsId = periodsId;
    }

    String periodsId;

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

    String days;

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }

    boolean isAdded;

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

    String EndDate;
    String StartDate;

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

//    boolean Saturday;
//    boolean Sunday;
//    boolean Monday;
//    boolean Tuesday;
//    boolean Wednesday;
//    boolean Thursday;
//    boolean Friday;

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

//    public boolean isSaturday() {
//        return Saturday;
//    }
//
//    public void setSaturday(boolean saturday) {
//        Saturday = saturday;
//    }
//
//    public boolean isSunday() {
//        return Sunday;
//    }
//
//    public void setSunday(boolean sunday) {
//        Sunday = sunday;
//    }
//
//    public boolean isMonday() {
//        return Monday;
//    }
//
//    public void setMonday(boolean monday) {
//        Monday = monday;
//    }
//
//    public boolean isTuesday() {
//        return Tuesday;
//    }
//
//    public void setTuesday(boolean tuesday) {
//        Tuesday = tuesday;
//    }
//
//    public boolean isWednesday() {
//        return Wednesday;
//    }
//
//    public void setWednesday(boolean wednesday) {
//        Wednesday = wednesday;
//    }
//
//    public boolean isThursday() {
//        return Thursday;
//    }
//
//    public void setThursday(boolean thursday) {
//        Thursday = thursday;
//    }
//
//    public boolean isFriday() {
//        return Friday;
//    }
//
//    public void setFriday(boolean friday) {
//        Friday = friday;
//    }



}
