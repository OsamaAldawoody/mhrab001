package com.newsolution.almhrab.Model;

/**
 * Created by hp on 9/17/2017.
 */

public class News {
    private int Id ;
    private String TextAds; // الخبر
    private String UpdatedAt; // اخر تحديث
    private boolean isDeleted ;// محذوف ؟؟
    private String FromDate;// يتم العرض من بداية التاريخ
    private String ToDate; // يتم العرض حتى نهاية تاريخ
    public int sort;// ترتيب

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTextAds() {
        return TextAds;
    }

    public void setTextAds(String textAds) {
        TextAds = textAds;
    }

    public String getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        UpdatedAt = updatedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
