package com.newsolution.almhrab.Model;

/**
 * Created by hp on 12/25/2017.
 */

public class Khotab {
    public int Id; // رقم الخطبة ، لا يتكرر
    public String Title; // عنوان الخطبة بالعربي ،،، ،، لازم يكون في نص ، يعني لازم يرجع الك
    public String Body;// نص الخطبة بالعربي ،، لازم يكون في نص ، يعني لازم يرجع الك
    public String DateKhotab; // تاريخ الخطبة ،، غالبا يكون تاريخ ايام جمعة فقط
    public String UpdatedAt; // اخر تحديث
    public int isDeleted; // هل هو محذوف :: نعم يعني محذوف
    public String Description; // الوصف ،، زيادة ،، غير اجبارية
    public String Title1;// عنوان الترجمة الاولى  اللغة الاولى
    public String Body1; // ترجمة الخطبة للغة الاولى
    public String Title2; // ترجمة العنوان للغة التانية
    public String Body2; // ترجمة نص الخطبة للغة التانية
    public String UrlVideoDeaf; // رابط فيديو لغة الاشارة ،، اذا موجود بكون في فيديو ، واذا فاضي اذا لا يحتوي على لغة اشارة
    public int TimeExpected;// وقت الخطبة المقدر ،، الوقت بالدقائق ،،، يستخدم ل زمن اظهار الترجمة على الشاشة وزمن تسجيل الخطبة من بعد الاذان ب 2 دقيقة مثلا
    public int isException;
    public Khotab(){}
    public Khotab(int Id, String Title, String Body, String DateKhotab, String UpdatedAt, int isDeleted, String Description
            , String Title1, String Body1, String Title2, String Body2, String UrlVideoDeaf, int TimeExpected, int isException) {
        this.Id = Id;
        this.Title = Title;
        this.Body = Body;
        this.DateKhotab = DateKhotab;
        this.UpdatedAt = UpdatedAt;
        this.isDeleted = isDeleted;
        this.Description = Description;
        this.Title2 = Title2;
        this.Title1 = Title1;
        this.Body1 = Body1;
        this.Body2 = Body2;
        this.UrlVideoDeaf = UrlVideoDeaf;
        this.TimeExpected = TimeExpected;
        this.isException = isException;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public String getDateKhotab() {
        return DateKhotab;
    }

    public void setDateKhotab(String dateKhotab) {
        DateKhotab = dateKhotab;
    }

    public String getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        UpdatedAt = updatedAt;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTitle1() {
        return Title1;
    }

    public void setTitle1(String title1) {
        Title1 = title1;
    }

    public String getBody1() {
        return Body1;
    }

    public void setBody1(String body1) {
        Body1 = body1;
    }

    public String getTitle2() {
        return Title2;
    }

    public void setTitle2(String title2) {
        Title2 = title2;
    }

    public String getBody2() {
        return Body2;
    }

    public void setBody2(String body2) {
        Body2 = body2;
    }

    public String getUrlVideoDeaf() {
        return UrlVideoDeaf;
    }

    public void setUrlVideoDeaf(String urlVideoDeaf) {
        UrlVideoDeaf = urlVideoDeaf;
    }

    public int getTimeExpected() {
        return TimeExpected;
    }

    public void setTimeExpected(int timeExpected) {
        TimeExpected = timeExpected;
    }

    public int getIsException() {
        return isException;
    }

    public void setIsException(int isException) {
        this.isException = isException;
    }
}
