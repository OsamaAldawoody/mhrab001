package com.newsolution.almhrab.Helpar;

import com.newsolution.almhrab.Model.Azkar;
import com.newsolution.almhrab.Model.City;
import com.newsolution.almhrab.Model.Khotab;
import com.newsolution.almhrab.Model.News;
import com.newsolution.almhrab.Model.OptionSiteClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by AmalKronz on 3/15/2016.
 */
public class JsonHelper {

    public static ArrayList<Azkar> jsonToAzkarArray(JSONArray a) {
        int len = a.length();
        ArrayList<Azkar> array = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            try {
                array.add(jsonToAzkarClass(a.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return array;
    }

    public static Azkar jsonToAzkarClass(JSONObject o) {
        Azkar object = new Azkar();
        object.setId(o.optInt("Id"));
        object.setTextAzakar(o.optString("TextAzakar"));
        object.setUpdatedAt(o.optString("UpdatedAt"));
        object.setDeleted(o.optBoolean("isDeleted"));
        object.setFajr(o.optBoolean("Fajr"));
        object.setDhuhr(o.optBoolean("Dhuhr"));
        object.setAsr(o.optBoolean("Asr"));
        object.setMagrib(o.optBoolean("Magrib"));
        object.setIsha(o.optBoolean("Isha"));
        object.setSort(o.optInt("sort"));
        object.setCount(o.optInt("Count"));
        return object;
    }

    public static ArrayList<News> jsonToNewsArray(JSONArray a) {
        int len = a.length();
        ArrayList<News> array = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            try {
                array.add(jsonToNewsClass(a.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return array;
    }

    public static News jsonToNewsClass(JSONObject o) {
        News object = new News();
        object.setId(o.optInt("Id"));
        object.setTextAds(o.optString("TextAds"));
        object.setUpdatedAt(o.optString("UpdatedAt"));
        object.setFromDate(o.optString("FromDate"));
        object.setToDate(o.optString("ToDate"));
        object.setDeleted(o.optBoolean("isDeleted"));
        object.setSort(o.optInt("sort"));
        return object;
    }

    public static ArrayList<City> jsonToCitiesArray(JSONArray a) {
        int len = a.length();
        ArrayList<City> array = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            try {
                array.add(jsonToCityClass(a.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return array;
    }

    public static City jsonToCityClass(JSONObject o) {
        City object = new City();
        object.setId(o.optInt("Id"));
        object.setName(o.optString("Name"));
        object.setNameEn(o.optString("NameEn"));
        object.setLon1(o.optInt("Long_Degree"));
        object.setLon2(o.optInt("Long_Minute"));
        object.setLat1(o.optInt("Lat_Degree"));
        object.setLat2(o.optInt("Lat_Minute"));
        object.setUpdatedAt(o.optString("UpdatedAt"));
        object.setIsDeleted(o.optBoolean("isDeleted") ? 1 : 0);
        return object;
    }

    public static OptionSiteClass jsonToSettingsClass(JSONObject o) {
        OptionSiteClass object = new OptionSiteClass();
        object.setStatusAthanVoiceF(o.optBoolean("StatusAthanVoiceF"));
        object.setStatusEkamaVoiceF(o.optBoolean("StatusEkamaVoiceF"));
        object.setStatusAthanVoiceD(o.optBoolean("StatusAthanVoiceD"));
        object.setStatusEkamaVoiceD(o.optBoolean("StatusEkamaVoiceD"));
        object.setStatusAthanVoiceA(o.optBoolean("StatusAthanVoiceA"));
        object.setStatusEkamaVoiceA(o.optBoolean("StatusEkamaVoiceA"));
        object.setStatusAthanVoiceM(o.optBoolean("StatusAthanVoiceM"));
        object.setStatusEkamaVoiceM(o.optBoolean("StatusEkamaVoiceM"));
        object.setStatusAthanVoiceI(o.optBoolean("StatusAthanVoiceI"));
        object.setStatusEkamaVoiceI(o.optBoolean("StatusEkamaVoiceI"));
        object.setPhoneStatusAlerts(o.optBoolean("PhoneStatusAlerts"));
        object.setPhoneShowAlertsBeforEkama(o.optInt("PhoneShowAlertsBeforEkama"));
        object.setFajrEkamaIsTime(o.optBoolean("FajrEkamaIsTime"));
        object.setFajrEkamaTime(o.optString("FajrEkamaTime"));
        object.setFajrEkama(o.optInt("FajrEkama"));
        object.setFajrAzkar(o.optInt("FajrAzkar"));
        object.setFajrAzkarTimer(o.optInt("FajrAzkarTimer"));

        object.setAlShrouqEkamaIsTime(o.optBoolean("AlShrouqEkamaIsTime"));
        object.setAlShrouqEkamaTime(o.optString("AlShrouqEkamaTime"));
        object.setAlShrouqEkama(o.optInt("AlShrouqEkama"));

        object.setDhuhrEkamaIsTime(o.optBoolean("DhuhrEkamaIsTime"));
        object.setDhuhrEkamaTime(o.optString("DhuhrEkamaTime"));
        object.setDhuhrEkama(o.optInt("DhuhrEkama"));
        object.setDhuhrAzkar(o.optInt("DhuhrAzkar"));
        object.setDhuhrAzkarTimer(o.optInt("DhuhrAzkarTimer"));

        object.setAsrEkamaTime(o.optString("AsrEkamaTime"));
        object.setAsrEkamaIsTime(o.optBoolean("AsrEkamaIsTime"));
        object.setAsrEkama(o.optInt("AsrEkama"));
        object.setAsrAzkar(o.optInt("AsrAzkar"));
        object.setAsrAzkarTimer(o.optInt("AsrAzkarTimer"));

        object.setMagribEkamaTime(o.optString("MagribEkamaTime"));
        object.setMagribEkamaIsTime(o.optBoolean("MagribEkamaIsTime"));
        object.setMagribEkama(o.optInt("MagribEkama"));
        object.setMagribAzkar(o.optInt("MagribAzkar"));
        object.setMagribAzkarTimer(o.optInt("MagribAzkarTimer"));

        object.setIshaEkamaTime(o.optString("IshaEkamaTime"));
        object.setIshaEkamaIsTime(o.optBoolean("IshaEkamaIsTime"));
        object.setIshaEkama(o.optInt("IshaEkama"));
        object.setIshaAzkar(o.optInt("IshaAzkar"));
        object.setIshaAzkarTimer(o.optInt("IshaAzkarTimer"));
        object.setPhoneAlertsArabic(o.optString("PhoneAlertsArabic"));
        object.setPhoneAlertsUrdu(o.optString("PhoneAlertsUrdu"));
        object.setPhoneAlertsEnglish(o.optString("PhoneAlertsEnglish"));
        object.setPhoneStatusVoice(o.optBoolean("PhoneStatusVoice"));
        object.setDateHijri(o.optInt("DateHijri"));
        return object;
    }
    public static ArrayList<Khotab> jsonToKhotabArray(JSONArray a) {
        int len = a.length();
        ArrayList<Khotab> array = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            try {
                array.add(jsonToKhotabClass(a.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return array;
    }

    public static Khotab jsonToKhotabClass(JSONObject o) {
        Khotab object = new Khotab();
        object.setId(o.optInt("Id"));
        object.setTitle(o.optString("Title"));
        object.setBody(o.optString("Body"));
        object.setDateKhotab(o.optString("DateKhotab"));
        object.setUpdatedAt(o.optString("UpdatedAt"));
        object.setDescription(o.optString("Description"));
        object.setTitle1(o.optString("Title1"));
        object.setBody1(o.optString("Body1"));
        object.setTitle2(o.optString("Title2"));
        object.setBody2(o.optString("Body2"));
        object.setUrlVideoDeaf(o.optString("UrlVideoDeaf"));
        object.setTimeExpected(o.optInt("TimeExpected"));
        object.setIsException(o.optInt("isException"));
        object.setIsDeleted(o.optInt("isDeleted"));
        return object;
    }

    public static boolean contains(JSONObject o, String key) {
        boolean status = false;
        try {
            status = o != null && o.has(key) && !o.isNull(key) && !o.getString(key).equalsIgnoreCase("null");
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return status;
        }
    }
}
