package com.newsolution.almhrab.Model;

/**
 * Created by hp on 8/7/2016.
 */
public class Azkar {
    private int Id;
    private String TextAzakar; // الاذكار
    private String UpdatedAt; // اخر تحديث
    private boolean isDeleted; // محدوف
    public int sort; // ترتيب
    private boolean Fajr; // فجر
    private boolean Dhuhr;// ظهر
    private boolean Asr;// عصر
    private boolean Magrib; // مغرب
    private boolean Isha; // عشاء
    private int Count; // تكرار ،، مبدئيا مالهاش اي قيمة

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTextAzakar() {
        return TextAzakar;
    }

    public void setTextAzakar(String textAzakar) {
        TextAzakar = textAzakar;
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

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public boolean isFajr() {
        return Fajr;
    }

    public void setFajr(boolean fajr) {
        Fajr = fajr;
    }

    public boolean isDhuhr() {
        return Dhuhr;
    }

    public void setDhuhr(boolean dhuhr) {
        Dhuhr = dhuhr;
    }

    public boolean isAsr() {
        return Asr;
    }

    public void setAsr(boolean asr) {
        Asr = asr;
    }

    public boolean isMagrib() {
        return Magrib;
    }

    public void setMagrib(boolean magrib) {
        Magrib = magrib;
    }

    public boolean isha() {
        return Isha;
    }

    public void setIsha(boolean isha) {
        Isha = isha;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }


}