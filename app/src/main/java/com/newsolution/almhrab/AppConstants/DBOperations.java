package com.newsolution.almhrab.AppConstants;

/**
 * Created by Amal on 12/8/2015.
 */

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.newsolution.almhrab.Model.Ads;
import com.newsolution.almhrab.Model.Azkar;
import com.newsolution.almhrab.Model.City;
import com.newsolution.almhrab.Model.Khotab;
import com.newsolution.almhrab.Model.News;
import com.newsolution.almhrab.Model.OptionSiteClass;

import java.io.IOException;
import java.util.ArrayList;

public class DBOperations {
    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;
    SharedPreferences sp;
    SharedPreferences.Editor spedit;
    private ArrayList<City> _cities;
    public String cfajr, csunrise, cdhohr, casr, cmaghrib, cisha;
    private ArrayList<News> adses;
    private ArrayList<Azkar> azkars;

    // GlobalData globalData;
    public DBOperations(Context context) {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
        sp = mContext.getSharedPreferences(AppConst.PREFS, mContext.MODE_PRIVATE);
        spedit = sp.edit();
        //globalData=(GlobalData)mContext.getApplicationContext();
    }

    public DBOperations createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public DBOperations open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>" + mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public ArrayList<City> getAllCity() {
        _cities = new ArrayList<>();
        String selectQuery = "SELECT * FROM City where isDeleted=0";
        Log.i("Quert", selectQuery);
        Cursor cursor = mDb.rawQuery(selectQuery, null);
        Log.i("dataBase", "" + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                int lon1 = cursor.getInt(cursor.getColumnIndex("lon1"));
                int lon2 = cursor.getInt(cursor.getColumnIndex("lon2"));
                int lat1 = cursor.getInt(cursor.getColumnIndex("lat1"));
                int lat2 = cursor.getInt(cursor.getColumnIndex("lat2"));
                int isDeleted = cursor.getInt(cursor.getColumnIndex("isDeleted"));
                String name = cursor.getString(cursor.getColumnIndex("Name"));
                String NameEn = cursor.getString(cursor.getColumnIndex("NameEn"));
                String updatedAt = cursor.getString(cursor.getColumnIndex("updatedAt"));
                _cities.add(new City(id, name, NameEn, lon1, lon2, lat1, lat2, updatedAt, isDeleted));
//                Log.i("city ID", "" + cursor.getInt(0));
//                Log.i("city name", "" + cursor.getString(1));
            } while (cursor.moveToNext());
        }
        return _cities;
    }

    public void insertCities(ArrayList<City> a) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.enableWriteAheadLogging();
        db.beginTransaction();
        int count = 0;
        int len = a.size();
        //Delete services
        for (int i = 0; i < len; i++) {
            City current = a.get(i);
            if (current.getIsDeleted() == 0) {
                addCity(db, current);
            }
        }
        Log.d("Sync service", "# of News inserted : " + count);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public boolean addCity(SQLiteDatabase db, City object) {
        ContentValues values = new ContentValues();
        db.delete("City", "id=" + object.getId(), null);
        values.put("id", object.getId());
        values.put("lon1", object.getLon1());
        values.put("lon2", object.getLon2());
        values.put("lat1", object.getLat1());
        values.put("lat2", object.getLat2());
        values.put("Name", object.getName());
        values.put("NameEn", object.getNameEn());
        values.put("updatedAt", object.getUpdatedAt());
        values.put("isDeleted", object.getIsDeleted());
        long rowid = db.insert("City", null, values);
        return rowid != -1;
    }

    public ArrayList<City> searchCity(String city) {
        _cities = new ArrayList<>();
        String selectQuery = "SELECT * FROM City where Name like '%" + city + "%' or NameEn like '%" + city + "%'";
        Log.i("Quert", selectQuery);
        Cursor cursor = mDb.rawQuery(selectQuery, null);
        Log.i("dataBase", "" + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                int lon1 = cursor.getInt(cursor.getColumnIndex("lon1"));
                int lon2 = cursor.getInt(cursor.getColumnIndex("lon2"));
                int lat1 = cursor.getInt(cursor.getColumnIndex("lat1"));
                int lat2 = cursor.getInt(cursor.getColumnIndex("lat2"));
                int isDeleted = cursor.getInt(cursor.getColumnIndex("isDeleted"));
                String name = cursor.getString(cursor.getColumnIndex("Name"));
                String NameEn = cursor.getString(cursor.getColumnIndex("NameEn"));
                String updatedAt = cursor.getString(cursor.getColumnIndex("updatedAt"));
                if (isDeleted == 0)
                    _cities.add(new City(id, name, NameEn, lon1, lon2, lat1, lat2, updatedAt, isDeleted));
            } while (cursor.moveToNext());
        }
        return _cities;
    }

    public City getCityById(int cityId) {
        City cities = new City();
        String selectQuery = "SELECT * FROM City where id = " + cityId + "";
        Log.i("Quert", selectQuery);
        Cursor cursor = mDb.rawQuery(selectQuery, null);
        Log.i("dataBase", "" + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                int lon1 = cursor.getInt(cursor.getColumnIndex("lon1"));
                int lon2 = cursor.getInt(cursor.getColumnIndex("lon2"));
                int lat1 = cursor.getInt(cursor.getColumnIndex("lat1"));
                int lat2 = cursor.getInt(cursor.getColumnIndex("lat2"));
                int isDeleted = cursor.getInt(cursor.getColumnIndex("isDeleted"));
                String name = cursor.getString(cursor.getColumnIndex("Name"));
                String NameEn = cursor.getString(cursor.getColumnIndex("NameEn"));
                String updatedAt = cursor.getString(cursor.getColumnIndex("updatedAt"));
                cities.setId(id);
                cities.setName(name);
                cities.setNameEn(NameEn);
                cities.setLon1(lon1);
                cities.setLon2(lon2);
                cities.setLat1(lat1);
                cities.setLat2(lat2);
                cities.setUpdatedAt(updatedAt);
                cities.setIsDeleted(isDeleted);
            } while (cursor.moveToNext());
        }
        return cities;
    }

    public String[] getPrayTimes(int day, int month, int cityId) {
        String selectQuery = "SELECT * FROM prayerDays where dayId=" + day + " and monthId=" + month + " and cityId=" + cityId + "";
        Log.d("DB Quert", selectQuery);
        Cursor CR = mDb.rawQuery(selectQuery, null);
        Log.d("DBOperations", "" + CR.getCount());
        if (CR.moveToFirst()) {
            cfajr = CR.getString(4);
            csunrise = CR.getString(5);
            cdhohr = CR.getString(6);
            casr = CR.getString(7);
            cmaghrib = CR.getString(8);
            cisha = CR.getString(9);
            Log.i("DB", "" + cfajr);

        }
        String[] prayTimes = {cfajr, csunrise, cdhohr, casr, cmaghrib, cisha};
        return prayTimes;

    }

    public Cursor getPrayTimes(Activity s, DBOperations db, int day, int month, int cityId) {
        Log.d("DBOperations", "table PrayTimes data reader");
        open();
        String selectQuery = "SELECT * FROM prayerDays where dayId=" + day + " and monthId=" + month + " and cityId=" + cityId + "";
        Cursor CR = mDb.rawQuery(selectQuery, null);
        // Toast.makeText(s," "+selectQuery+"  curser:" +CR.getCount(),Toast.LENGTH_LONG).show();

        return CR;
    }

    public ArrayList<String> getAzkar(int prayIndex) {
        ArrayList<String> azkars = new ArrayList<>();
        String selectQuery = null;
        if (prayIndex == 1)
            selectQuery = "SELECT * FROM azkar WHERE Fajr=1 ORDER BY sort ASC ";
        if (prayIndex == 2)
            selectQuery = "SELECT * FROM azkar WHERE Dhuhr=1 ORDER BY sort ASC ";
        if (prayIndex == 3)
            selectQuery = "SELECT * FROM azkar WHERE Asr=1 ORDER BY sort ASC ";
        if (prayIndex == 4)
            selectQuery = "SELECT * FROM azkar WHERE Magrib=1 ORDER BY sort ASC ";
        if (prayIndex == 5)
            selectQuery = "SELECT * FROM azkar WHERE Isha=1 ORDER BY sort ASC ";
        Log.i("Quert", selectQuery);
        Cursor cursor = mDb.rawQuery(selectQuery, null);
        Log.i("dataBase", "" + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                String text = cursor.getString(cursor.getColumnIndex("TextAzakar"));
                //+ " (" + cursor.getInt(cursor.getColumnIndex("Count")) + ")";
                azkars.add(text);
            } while (cursor.moveToNext());
        }
        return azkars;
    }

    public ArrayList<Azkar> getAzkar() {
        azkars = new ArrayList<>();
        String selectQuery = "SELECT * FROM azkar ORDER BY sort ASC ";
//        Log.i("Quert", selectQuery);
        Cursor cursor = mDb.rawQuery(selectQuery, null);
//        Log.i("dataBase", "" + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                Azkar object = new Azkar();
                object.setId(cursor.getInt(cursor.getColumnIndex("Id")));
                object.setTextAzakar(cursor.getString(cursor.getColumnIndex("TextAzakar")));
                object.setUpdatedAt(cursor.getString(cursor.getColumnIndex("UpdatedAt")));
                object.setDeleted(cursor.getInt(cursor.getColumnIndex("isDeleted")) == 1 ? true : false);
                object.setFajr(cursor.getInt(cursor.getColumnIndex("Fajr")) == 1 ? true : false);
                object.setDhuhr(cursor.getInt(cursor.getColumnIndex("Dhuhr")) == 1 ? true : false);
                object.setAsr(cursor.getInt(cursor.getColumnIndex("Asr")) == 1 ? true : false);
                object.setMagrib(cursor.getInt(cursor.getColumnIndex("Magrib")) == 1 ? true : false);
                object.setIsha(cursor.getInt(cursor.getColumnIndex("Isha")) == 1 ? true : false);
                object.setSort(cursor.getInt(cursor.getColumnIndex("sort")));
                object.setCount(cursor.getInt(cursor.getColumnIndex("Count")));
                object.setSort(cursor.getInt(cursor.getColumnIndex("sort")));
                azkars.add(object);
            } while (cursor.moveToNext());
        }
        return azkars;
    }

    public ArrayList<News> getNews() {
        adses = new ArrayList<>();
        String selectQuery = "SELECT * FROM News  ORDER BY sort ASC";
        Log.i("Quert", selectQuery);
        Cursor cursor = mDb.rawQuery(selectQuery, null);
        Log.i("dataBase", "" + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                News object = new News();
                object.setId(cursor.getInt(cursor.getColumnIndex("Id")));
                object.setTextAds(cursor.getString(cursor.getColumnIndex("TextAds")));
                object.setUpdatedAt(cursor.getString(cursor.getColumnIndex("UpdatedAt")));
                object.setFromDate(cursor.getString(cursor.getColumnIndex("FromDate")));
                object.setToDate(cursor.getString(cursor.getColumnIndex("ToDate")));
                object.setDeleted(cursor.getInt(cursor.getColumnIndex("isDeleted")) == 1 ? true : false);
                object.setSort(cursor.getInt(cursor.getColumnIndex("sort")));
                adses.add(object);
            } while (cursor.moveToNext());
        }
        return adses;
    }

    public boolean getNewsById(int Id) {
        boolean isExist = false;
        String selectQuery = "SELECT Id FROM News WHERE Id=" + Id + "";
        Log.i("Quert", selectQuery);
        Cursor cursor = mDb.rawQuery(selectQuery, null);
        Log.i("dataBase", "" + cursor.getCount());
        if (cursor.moveToFirst()) {
            isExist = true;
        }
        return isExist;
    }

    public boolean getAzkarById(int Id) {
        boolean isExist = false;
        String selectQuery = "SELECT Id FROM azkar WHERE Id=" + Id + "";
        Log.i("Quert", selectQuery);
        Cursor cursor = mDb.rawQuery(selectQuery, null);
        Log.i("dataBase", "" + cursor.getCount());
        if (cursor.moveToFirst()) {
            isExist = true;
        }
        return isExist;
    }

    public void delAds(int id) {
        Log.d("DBOperations", "table profile data eraser");
        // open();
        mDb.execSQL("DELETE  FROM News where Id=" + id + "");
        Log.i("DBOperations", "DELETE  FROM News where Id=" + id + "");

    }

    public void delAzkar(int id) {
        Log.d("DBOperations", "table profile data eraser");
        // open();
        mDb.execSQL("DELETE  FROM azkar where Id=" + id + "");
        Log.i("DBOperations", "DELETE  FROM News where Id=" + id + "");

    }

    public void insertAzkar(ArrayList<Azkar> a) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.enableWriteAheadLogging();
        db.beginTransaction();
//        if (a!=null)
//            db.delete("azkar","",null);
        int count = 0;
        int len = a.size();
        //Delete services
        for (int i = 0; i < len; i++) {
            Azkar current = a.get(i);
            if (!current.isDeleted()) {
                addAzkar(db, current);
                count++;
            }
        }
        Log.d("Sync service", "# of New Azkar inserted : " + count);

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void insertAllAzkar(ArrayList<Azkar> a) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.enableWriteAheadLogging();
        db.beginTransaction();
        if (a != null)
            db.delete("azkar", "", null);
        int count = 0;
        int len = a.size();
        //Delete services
        for (int i = 0; i < len; i++) {
            Azkar current = a.get(i);
            if (!current.isDeleted()) {
                addAzkar(db, current);
                count++;
            }
        }
        Log.d("Sync service", "# of New Azkar inserted : " + count);

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public boolean addAzkar(SQLiteDatabase db, Azkar object) {
        ContentValues values = new ContentValues();
        db.delete("azkar", "Id=" + object.getId(), null);
        values.put("Id", object.getId());
        values.put("TextAzakar", object.getTextAzakar());
        values.put("UpdatedAt", object.getUpdatedAt());
        values.put("isDeleted", object.isDeleted() ? 1 : 0);
        values.put("Fajr", object.isFajr() ? 1 : 0);
        values.put("Dhuhr", object.isDhuhr() ? 1 : 0);
        values.put("Asr", object.isAsr() ? 1 : 0);
        values.put("Magrib", object.isMagrib() ? 1 : 0);
        values.put("Isha", object.isha() ? 1 : 0);
        values.put("sort", object.getSort());
        values.put("Count", object.getCount());

        //db.insert("Service_Provider", null, values);
        long rowid = db.insert("azkar", null, values);
        // db.close();
        return rowid != -1;
    }

    public void insertNews(ArrayList<News> a) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.enableWriteAheadLogging();
        db.beginTransaction();
        int count = 0;
        int len = a.size();
        //Delete services
        for (int i = 0; i < len; i++) {
            News current = a.get(i);
            if (!current.isDeleted()) {
                Log.d("Sync:From ", current.getFromDate());
                Log.d("Sync:to ", current.getToDate());
                addNews(db, current);
                count++;
            }
        }
        Log.d("Sync service", "# of News inserted : " + count);

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void insertAllNews(ArrayList<News> a) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.enableWriteAheadLogging();
        db.beginTransaction();
        if (a != null)
            db.delete("News", "", null);
        int count = 0;
        int len = a.size();
        //Delete services
        for (int i = 0; i < len; i++) {
            News current = a.get(i);
            if (!current.isDeleted()) {
                addNews(db, current);
            }
        }
        Log.d("Sync service", "# of News inserted : " + count);

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public boolean addNews(SQLiteDatabase db, News object) {
        ContentValues values = new ContentValues();
        db.delete("News", "Id=" + object.getId(), null);
        values.put("Id", object.getId());
        values.put("TextAds", object.getTextAds());
        values.put("UpdatedAt", object.getUpdatedAt());
        values.put("FromDate", object.getFromDate());
        values.put("ToDate", object.getToDate());
        values.put("isDeleted", object.isDeleted() ? 1 : 0);
        values.put("sort", object.getSort());
        long rowid = db.insert("News", null, values);
        return rowid != -1;
    }

    public void insertAllKhotab(ArrayList<Khotab> a) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.enableWriteAheadLogging();
        db.beginTransaction();
        if (a != null)
            db.delete("Khotab", "", null);
        int count = 0;
        int len = a.size();
        //Delete services
        for (int i = 0; i < len; i++) {
            Khotab current = a.get(i);
            if (current.getIsDeleted() == 0) {
                addKhotab(db, current);
                count++;
            }
        }
        Log.d("Sync service", "# of New Azkar inserted : " + count);

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public boolean addKhotab(SQLiteDatabase db, Khotab object) {
        ContentValues values = new ContentValues();
        db.delete("Khotab", "Id=" + object.getId(), null);
        values.put("Id", object.getId());
        values.put("Title", object.getTitle());
        values.put("Title1", object.getTitle1());
        values.put("Title2", object.getTitle2());
        values.put("Body", object.getBody());
        values.put("Body1", object.getBody1());
        values.put("Body2", object.getBody2());
        values.put("UpdatedAt", object.getUpdatedAt());
        values.put("DateKhotab", object.getDateKhotab());
        values.put("Description", object.getDescription());
        values.put("UrlVideoDeaf", object.getUrlVideoDeaf());
        values.put("TimeExpected", object.getTimeExpected());
        values.put("isDeleted", object.getIsDeleted());
        values.put("isException", object.getIsException());
        //db.insert("Service_Provider", null, values);
        long rowid = db.insert("Khotab", null, values);
        // db.close();
        return rowid != -1;
    }

    //    public void updateNews(News news) {
//        SQLiteDatabase db = mDbHelper.getWritableDatabase();
//        db.enableWriteAheadLogging();
//        db.beginTransaction();
//        Log.d("DBOperations", "table profile data eraser");
//        // open();
//        mDb.execSQL("update ads set adsText ='" + adsText + "' where id='" + id + "'");
//        Log.i("DBOperations", "update ads set adsText ='" + adsText + "' where id='" + id + "'");
//        db.setTransactionSuccessful();
//        db.endTransaction();
//        db.close();
//    }
    public ArrayList<String> getNews(String date) {
        ArrayList<String> newsList = new ArrayList<String>();
        String selectQuery = "SELECT * FROM News where FromDate <='" + date + "' and ToDate >='" + date
                + "' and isDeleted=0  ORDER BY sort ASC";
        Log.d("Sync: Query", selectQuery);
        Cursor cursor = mDb.rawQuery(selectQuery, null);
        Log.d("Sync:count news ", "" + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                String TextAds = cursor.getString(cursor.getColumnIndex("TextAds"));
                newsList.add("  " + TextAds + " ");
                Log.d("Sync:TextAds: ", TextAds);
//                News object = new News();
//                object.setId(cursor.getInt(cursor.getColumnIndex("Id")));
//                object.setTextAds(cursor.getString(cursor.getColumnIndex("TextAds")));
//                object.setUpdatedAt(cursor.getString(cursor.getColumnIndex("UpdatedAt")));
//                object.setFromDate(cursor.getString(cursor.getColumnIndex("FromDate")));
//                object.setToDate(cursor.getString(cursor.getColumnIndex("ToDate")));
//                object.setDeleted(cursor.getInt(cursor.getColumnIndex("isDeleted"))==1?true:false);
//                object.setSort(cursor.getInt(cursor.getColumnIndex("sort")));
                Log.i("dataBase TextAds", "" + TextAds);
            } while (cursor.moveToNext());
        }
        return newsList;
    }

    public void insertSettings(OptionSiteClass object) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.enableWriteAheadLogging();
        db.beginTransaction();
        int count = 0;
        db.delete("Settings", "", null);
        ContentValues values = new ContentValues();

        values.put("FajrEkamaIsTime", object.isFajrEkamaIsTime() ? 1 : 0);
        values.put("FajrEkama", object.getFajrEkama());
        values.put("FajrEkamaTime", object.getFajrEkamaTime());
        values.put("FajrAzkar", object.getFajrAzkar());
        values.put("FajrAzkarTimer", object.getFajrAzkarTimer());

        values.put("AlShrouqEkamaIsTime", object.isAlShrouqEkamaIsTime() ? 1 : 0);
        values.put("AlShrouqEkamaTime", object.getAlShrouqEkamaTime());
        values.put("AlShrouqEkama", object.getAlShrouqEkama());

        values.put("DhuhrEkamaIsTime", object.isDhuhrEkamaIsTime() ? 1 : 0);
        values.put("DhuhrEkamaTime", object.getDhuhrEkamaTime());
        values.put("DhuhrEkama", object.getDhuhrEkama());
        values.put("DhuhrAzkar", object.getDhuhrAzkar());
        values.put("DhuhrAzkarTimer", object.getDhuhrAzkarTimer());


        values.put("AsrEkamaIsTime", object.isAsrEkamaIsTime() ? 1 : 0);
        values.put("AsrEkamaTime", object.getAsrEkamaTime());
        values.put("AsrEkama", object.getAsrEkama());
        values.put("AsrAzkar", object.getAsrAzkar());
        values.put("AsrAzkarTimer", object.getAsrAzkarTimer());

        values.put("MagribEkamaIsTime", object.isMagribEkamaIsTime() ? 1 : 0);
        values.put("MagribEkamaTime", object.getMagribEkamaTime());
        values.put("MagribEkama", object.getMagribEkama());
        values.put("MagribAzkar", object.getMagribAzkar());
        values.put("MagribAzkarTimer", object.getMagribAzkarTimer());

        values.put("IshaEkamaIsTime", object.ishaEkamaIsTime() ? 1 : 0);
        values.put("IshaEkamaTime", object.getIshaEkamaTime());
        values.put("IshaEkama", object.getIshaEkama());
        values.put("IshaAzkar", object.getIshaAzkar());
        values.put("IshaAzkarTimer", object.getIshaAzkarTimer());

        values.put("PhoneAlertsArabic", (object.getPhoneAlertsArabic() == "null") ? "" : object.getPhoneAlertsArabic());
        values.put("PhoneAlertsEnglish", (object.getPhoneAlertsEnglish() == "null") ? "" : object.getPhoneAlertsEnglish());
        values.put("PhoneAlertsUrdu", (object.getPhoneAlertsUrdu() == "null") ? "" : object.getPhoneAlertsUrdu());
        values.put("PhoneShowAlertsBeforEkama", object.getPhoneShowAlertsBeforEkama());

        values.put("PhoneStatusVoice", (object.isPhoneStatusVoice()) ? 1 : 0);
        values.put("PhoneStatusAlerts", (object.isPhoneStatusAlerts()) ? 1 : 0);
        values.put("StatusEkamaVoiceF", (object.isStatusEkamaVoiceF()) ? 1 : 0);
        values.put("StatusAthanVoiceF", (object.isStatusAthanVoiceF()) ? 1 : 0);
        values.put("StatusEkamaVoiceD", (object.isStatusEkamaVoiceD()) ? 1 : 0);
        values.put("StatusAthanVoiceD", (object.isStatusAthanVoiceD()) ? 1 : 0);
        values.put("StatusEkamaVoiceA", (object.isStatusEkamaVoiceA()) ? 1 : 0);
        values.put("StatusAthanVoiceA", (object.isStatusAthanVoiceA()) ? 1 : 0);
        values.put("StatusEkamaVoiceM", (object.isStatusEkamaVoiceM()) ? 1 : 0);
        values.put("StatusAthanVoiceM", (object.isStatusAthanVoiceM()) ? 1 : 0);
        values.put("StatusEkamaVoiceI", (object.isStatusEkamaVoiceI()) ? 1 : 0);
        values.put("StatusAthanVoiceI", (object.isStatusAthanVoiceI()) ? 1 : 0);
        values.put("DateHijri", object.getDateHijri());
        db.insert("Settings", null, values);
        Log.d("Sync service", "# of set inserted : " + count);
        Log.d("Sync service", "# of set  : " + values.get("AlShrouqEkama"));

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public OptionSiteClass getSettings() {
        OptionSiteClass object = new OptionSiteClass();
        String selectQuery = "SELECT * FROM Settings";
        Log.i("Quert", selectQuery);
        Cursor cursor = mDb.rawQuery(selectQuery, null);
        Log.i("Qu dataBase", "" + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {

                object.setFajrEkamaIsTime(cursor.getInt(cursor.getColumnIndex("FajrEkamaIsTime")) == 1 ? true : false);
                object.setAlShrouqEkamaIsTime(cursor.getInt(cursor.getColumnIndex("AlShrouqEkamaIsTime")) == 1 ? true : false);
                object.setDhuhrEkamaIsTime(cursor.getInt(cursor.getColumnIndex("DhuhrEkamaIsTime")) == 1 ? true : false);
                object.setAsrEkamaIsTime(cursor.getInt(cursor.getColumnIndex("AsrEkamaIsTime")) == 1 ? true : false);
                object.setMagribEkamaIsTime(cursor.getInt(cursor.getColumnIndex("MagribEkamaIsTime")) == 1 ? true : false);
                object.setIshaEkamaIsTime(cursor.getInt(cursor.getColumnIndex("IshaEkamaIsTime")) == 1 ? true : false);

                object.setStatusAthanVoiceF(cursor.getInt(cursor.getColumnIndex("StatusAthanVoiceF")) == 1 ? true : false);
                object.setStatusEkamaVoiceF(cursor.getInt(cursor.getColumnIndex("StatusEkamaVoiceF")) == 1 ? true : false);
                object.setStatusAthanVoiceD(cursor.getInt(cursor.getColumnIndex("StatusAthanVoiceD")) == 1 ? true : false);
                object.setStatusEkamaVoiceD(cursor.getInt(cursor.getColumnIndex("StatusEkamaVoiceD")) == 1 ? true : false);
                object.setStatusAthanVoiceA(cursor.getInt(cursor.getColumnIndex("StatusAthanVoiceA")) == 1 ? true : false);
                object.setStatusEkamaVoiceA(cursor.getInt(cursor.getColumnIndex("StatusEkamaVoiceA")) == 1 ? true : false);
                object.setStatusAthanVoiceM(cursor.getInt(cursor.getColumnIndex("StatusAthanVoiceM")) == 1 ? true : false);
                object.setStatusEkamaVoiceM(cursor.getInt(cursor.getColumnIndex("StatusEkamaVoiceM")) == 1 ? true : false);
                object.setStatusAthanVoiceI(cursor.getInt(cursor.getColumnIndex("StatusAthanVoiceI")) == 1 ? true : false);
                object.setStatusEkamaVoiceI(cursor.getInt(cursor.getColumnIndex("StatusEkamaVoiceI")) == 1 ? true : false);
                object.setPhoneStatusAlerts(cursor.getInt(cursor.getColumnIndex("PhoneStatusAlerts")) == 1 ? true : false);
                object.setPhoneShowAlertsBeforEkama(cursor.getInt(cursor.getColumnIndex("PhoneShowAlertsBeforEkama")));
                object.setAlShrouqEkama(cursor.getInt(cursor.getColumnIndex("AlShrouqEkama")));
                object.setAlShrouqEkamaTime(cursor.getString(cursor.getColumnIndex("AlShrouqEkamaTime")));

                object.setFajrEkama(cursor.getInt(cursor.getColumnIndex("FajrEkama")));
                object.setFajrEkamaTime(cursor.getString(cursor.getColumnIndex("FajrEkamaTime")));
                object.setFajrAzkar(cursor.getInt(cursor.getColumnIndex("FajrAzkar")));
                object.setFajrAzkarTimer(cursor.getInt(cursor.getColumnIndex("FajrAzkarTimer")));
                object.setDhuhrEkamaTime(cursor.getString(cursor.getColumnIndex("DhuhrEkamaTime")));
                object.setDhuhrEkama(cursor.getInt(cursor.getColumnIndex("DhuhrEkama")));
                object.setDhuhrAzkar(cursor.getInt(cursor.getColumnIndex("DhuhrAzkar")));
                object.setDhuhrAzkarTimer(cursor.getInt(cursor.getColumnIndex("DhuhrAzkarTimer")));
                object.setAsrEkamaTime(cursor.getString(cursor.getColumnIndex("AsrEkamaTime")));
                object.setAsrEkama(cursor.getInt(cursor.getColumnIndex("AsrEkama")));
                object.setAsrAzkar(cursor.getInt(cursor.getColumnIndex("AsrAzkar")));
                object.setAsrAzkarTimer(cursor.getInt(cursor.getColumnIndex("AsrAzkarTimer")));
                object.setMagribEkamaTime(cursor.getString(cursor.getColumnIndex("MagribEkamaTime")));
                object.setMagribEkama(cursor.getInt(cursor.getColumnIndex("MagribEkama")));
                object.setMagribAzkar(cursor.getInt(cursor.getColumnIndex("MagribAzkar")));
                object.setMagribAzkarTimer(cursor.getInt(cursor.getColumnIndex("MagribAzkarTimer")));
                object.setIshaEkamaTime(cursor.getString(cursor.getColumnIndex("IshaEkamaTime")));
                object.setIshaEkama(cursor.getInt(cursor.getColumnIndex("IshaEkama")));
                object.setIshaAzkar(cursor.getInt(cursor.getColumnIndex("IshaAzkar")));
                object.setIshaAzkarTimer(cursor.getInt(cursor.getColumnIndex("IshaAzkarTimer")));
                object.setPhoneAlertsArabic(cursor.getString(cursor.getColumnIndex("PhoneAlertsArabic")));
                object.setPhoneAlertsUrdu(cursor.getString(cursor.getColumnIndex("PhoneAlertsUrdu")));
                object.setPhoneAlertsEnglish(cursor.getString(cursor.getColumnIndex("PhoneAlertsEnglish")));
                object.setPhoneStatusVoice(cursor.getInt(cursor.getColumnIndex("PhoneStatusVoice")) == 1 ? true : false);
                object.setDateHijri(cursor.getInt(cursor.getColumnIndex("DateHijri")));
                Log.d("dataBase iqama", "" + object.isStatusAthanVoiceF());
            } while (cursor.moveToNext());
        }
        return object;
    }

    public boolean ifSettingsExists() {
        boolean isExist = false;
        String selectQuery = "SELECT * FROM Settings";
        Log.i("Quert", selectQuery);
        Cursor cursor = mDb.rawQuery(selectQuery, null);
        Log.i("Qu dataBase", "" + cursor.getCount());
        if (cursor.getCount() > 0) {
            isExist = true;
        }
        return isExist;
    }

    public void insertAds(Ads object) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.enableWriteAheadLogging();
        db.beginTransaction();
        int count = 0;
//        db.delete("Advertisement", "", null);
        ContentValues values = new ContentValues();

//        values.put("id", object.getId());
        values.put("MasjedID", object.getMasjedID());
        values.put("Type", object.getType());
        values.put("Title", object.getTitle());
        values.put("Text", object.getText());
        values.put("Image", object.getImage());
        values.put("Video", object.getVideo());
        values.put("StartTime", object.getStartTime());
        values.put("EndTime", object.getEndTime());
        values.put("Saturday", object.isSaturday() ? 1 : 0);
        values.put("Sunday", object.isSunday() ? 1 : 0);
        values.put("Monday", object.isMonday() ? 1 : 0);
        values.put("Tuesday", object.isTuesday() ? 1 : 0);
        values.put("Wednesday", object.isWednesday() ? 1 : 0);
        values.put("Thursday", object.isThursday() ? 1 : 0);
        values.put("Friday", object.isFriday() ? 1 : 0);

        db.insert("Advertisement", null, values);
        Log.d("Sync service", "# of set inserted in Advertisement: " + count);

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public Ads getAds(int masjedID) {
        Ads object = new Ads();
        String selectQuery = "SELECT * FROM Advertisement WHERE MasjedID=" + masjedID + " LIMIT 1";
        Log.i("Quert", selectQuery);
        Cursor cursor = mDb.rawQuery(selectQuery, null);
        Log.i("Qu dataBase", "" + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                object.setId(cursor.getInt(cursor.getColumnIndex("id")));
                object.setMasjedID(cursor.getInt(cursor.getColumnIndex("MasjedID")));
                object.setType(cursor.getInt(cursor.getColumnIndex("Type")));
                object.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                object.setText(cursor.getString(cursor.getColumnIndex("Text")));
                object.setImage(cursor.getString(cursor.getColumnIndex("Image")));
                object.setVideo(cursor.getString(cursor.getColumnIndex("Video")));
                object.setStartTime(cursor.getString(cursor.getColumnIndex("StartTime")));
                object.setEndTime(cursor.getString(cursor.getColumnIndex("EndTime")));
                object.setSaturday(cursor.getInt(cursor.getColumnIndex("Saturday")) == 1 ? true : false);
                object.setSunday(cursor.getInt(cursor.getColumnIndex("Sunday")) == 1 ? true : false);
                object.setMonday(cursor.getInt(cursor.getColumnIndex("Monday")) == 1 ? true : false);
                object.setTuesday(cursor.getInt(cursor.getColumnIndex("Tuesday")) == 1 ? true : false);
                object.setWednesday(cursor.getInt(cursor.getColumnIndex("Wednesday")) == 1 ? true : false);
                object.setThursday(cursor.getInt(cursor.getColumnIndex("Thursday")) == 1 ? true : false);
                object.setFriday(cursor.getInt(cursor.getColumnIndex("Friday")) == 1 ? true : false);
            } while (cursor.moveToNext());
        } else object = null;
        return object;
    }

    public ArrayList<Ads> getAdsByTime(int masjedID, String time) {
        ArrayList<Ads> adses = new ArrayList<>();
        String selectQuery = "SELECT * FROM Advertisement WHERE MasjedID=" + masjedID + " AND StartTime='" + time
                + "' ORDER BY StartTime DESC";
        Log.i("Quert", selectQuery);
        Cursor cursor = mDb.rawQuery(selectQuery, null);
        Log.i("Qu dataBase", "" + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                Ads object = new Ads();
                object.setId(cursor.getInt(cursor.getColumnIndex("id")));
                object.setMasjedID(cursor.getInt(cursor.getColumnIndex("MasjedID")));
                object.setType(cursor.getInt(cursor.getColumnIndex("Type")));
                object.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                object.setText(cursor.getString(cursor.getColumnIndex("Text")));
                object.setImage(cursor.getString(cursor.getColumnIndex("Image")));
                object.setVideo(cursor.getString(cursor.getColumnIndex("Video")));
                object.setStartTime(cursor.getString(cursor.getColumnIndex("StartTime")));
                object.setEndTime(cursor.getString(cursor.getColumnIndex("EndTime")));
                object.setSaturday(cursor.getInt(cursor.getColumnIndex("Saturday")) == 1 ? true : false);
                object.setSunday(cursor.getInt(cursor.getColumnIndex("Sunday")) == 1 ? true : false);
                object.setMonday(cursor.getInt(cursor.getColumnIndex("Monday")) == 1 ? true : false);
                object.setTuesday(cursor.getInt(cursor.getColumnIndex("Tuesday")) == 1 ? true : false);
                object.setWednesday(cursor.getInt(cursor.getColumnIndex("Wednesday")) == 1 ? true : false);
                object.setThursday(cursor.getInt(cursor.getColumnIndex("Thursday")) == 1 ? true : false);
                object.setFriday(cursor.getInt(cursor.getColumnIndex("Friday")) == 1 ? true : false);
                adses.add(object);
            } while (cursor.moveToNext());
        }
        return adses;
    }

    public ArrayList<Ads> getAdsList(int masjedID) {
        ArrayList<Ads> adsList = new ArrayList<>();
        String selectQuery = "SELECT * FROM Advertisement WHERE MasjedID=" + masjedID + "";
        Log.i("Quert", selectQuery);
        Cursor cursor = mDb.rawQuery(selectQuery, null);
        Log.i("Qu dataBase", "" + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                Ads object = new Ads();
                object.setId(cursor.getInt(cursor.getColumnIndex("id")));
                object.setMasjedID(cursor.getInt(cursor.getColumnIndex("MasjedID")));
                object.setType(cursor.getInt(cursor.getColumnIndex("Type")));
                object.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                object.setText(cursor.getString(cursor.getColumnIndex("Text")));
                object.setImage(cursor.getString(cursor.getColumnIndex("Image")));
                object.setVideo(cursor.getString(cursor.getColumnIndex("Video")));
                object.setStartTime(cursor.getString(cursor.getColumnIndex("StartTime")));
                object.setEndTime(cursor.getString(cursor.getColumnIndex("EndTime")));
                object.setSaturday(cursor.getInt(cursor.getColumnIndex("Saturday")) == 1 ? true : false);
                object.setSunday(cursor.getInt(cursor.getColumnIndex("Sunday")) == 1 ? true : false);
                object.setMonday(cursor.getInt(cursor.getColumnIndex("Monday")) == 1 ? true : false);
                object.setTuesday(cursor.getInt(cursor.getColumnIndex("Tuesday")) == 1 ? true : false);
                object.setWednesday(cursor.getInt(cursor.getColumnIndex("Wednesday")) == 1 ? true : false);
                object.setThursday(cursor.getInt(cursor.getColumnIndex("Thursday")) == 1 ? true : false);
                object.setFriday(cursor.getInt(cursor.getColumnIndex("Friday")) == 1 ? true : false);
                Log.i("Qu dataBase", "" + object.getStartTime());
                adsList.add(object);
            } while (cursor.moveToNext());
        }
        ;
        return adsList;
    }

    public ArrayList<Ads> getAdsListByDay(int masjedID, Ads ads) {
        ArrayList<Ads> adsList = new ArrayList<>();
// MasjedID=" + masjedID + " AND
        String selectQuery = "SELECT * FROM Advertisement WHERE ";
        if (ads.isSaturday())
            selectQuery = selectQuery + " Saturday=1 OR";
        if (ads.isSunday())
            selectQuery = selectQuery + " Sunday=1 OR";
        if (ads.isMonday())
            selectQuery = selectQuery + "  Monday=1 OR";
        if (ads.isTuesday())
            selectQuery = selectQuery + "  Tuesday=1 OR";
        if (ads.isWednesday())
            selectQuery = selectQuery + "  Wednesday=1 OR";
        if (ads.isThursday())
            selectQuery = selectQuery + "  Thursday=1 OR";
        if (ads.isFriday())
            selectQuery = selectQuery + "  Friday=1";
        if (selectQuery.endsWith("OR"))
            selectQuery = selectQuery.substring(0, selectQuery.length() - 2);
        Log.i("Quert", selectQuery);
        Cursor cursor = mDb.rawQuery(selectQuery, null);
        Log.i("Qu dataBase", "" + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                Ads object = new Ads();
                object.setId(cursor.getInt(cursor.getColumnIndex("id")));
                object.setMasjedID(cursor.getInt(cursor.getColumnIndex("MasjedID")));
                object.setType(cursor.getInt(cursor.getColumnIndex("Type")));
                object.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                object.setText(cursor.getString(cursor.getColumnIndex("Text")));
                object.setImage(cursor.getString(cursor.getColumnIndex("Image")));
                object.setVideo(cursor.getString(cursor.getColumnIndex("Video")));
                object.setStartTime(cursor.getString(cursor.getColumnIndex("StartTime")));
                object.setEndTime(cursor.getString(cursor.getColumnIndex("EndTime")));
                object.setSaturday(cursor.getInt(cursor.getColumnIndex("Saturday")) == 1 ? true : false);
                object.setSunday(cursor.getInt(cursor.getColumnIndex("Sunday")) == 1 ? true : false);
                object.setMonday(cursor.getInt(cursor.getColumnIndex("Monday")) == 1 ? true : false);
                object.setTuesday(cursor.getInt(cursor.getColumnIndex("Tuesday")) == 1 ? true : false);
                object.setWednesday(cursor.getInt(cursor.getColumnIndex("Wednesday")) == 1 ? true : false);
                object.setThursday(cursor.getInt(cursor.getColumnIndex("Thursday")) == 1 ? true : false);
                object.setFriday(cursor.getInt(cursor.getColumnIndex("Friday")) == 1 ? true : false);
                adsList.add(object);
                Log.i("Qu dataBase", "" + object.getTitle() + " :" + object.isSaturday() + ":" + object.isSunday() + ": " + object.isMonday() +
                        ":" + object.isTuesday() + " :" + object.isWednesday() + ":" + object.isThursday() + ":" + object.isFriday());
            } while (cursor.moveToNext());
        }
        return adsList;
    }

    public void delAdvertisement(int id, int MasjedID) {
        Log.d("DBOperations", "table profile data eraser");
        // open();
        mDb.execSQL("DELETE  FROM Advertisement where id=" + id + " AND MasjedID=" + MasjedID + "");
        Log.i("DBOperations", "DELETE  FROM Advertisement where id=" + id + " AND MasjedID=" + MasjedID + "");

    }

}