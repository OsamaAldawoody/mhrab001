package com.newsolution.almhrab.WebServices;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;


import com.newsolution.almhrab.AppConstants.Constants;
import com.newsolution.almhrab.AppConstants.DBOperations;
import com.newsolution.almhrab.Helpar.JsonHelper;
import com.newsolution.almhrab.Helpar.Utils;
import com.newsolution.almhrab.Interface.OnFetched;
import com.newsolution.almhrab.Interface.OnLoadedFinished;
import com.newsolution.almhrab.Model.Azkar;
import com.newsolution.almhrab.Model.City;
import com.newsolution.almhrab.Model.Khotab;
import com.newsolution.almhrab.Model.News;
import com.newsolution.almhrab.Model.OptionSiteClass;
import com.newsolution.almhrab.Model.Users;
import com.newsolution.almhrab.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AmalKronz on 1/4/2016.
 */
public class WS {
    private static SharedPreferences sp;
    private static SharedPreferences.Editor spedit;

    private static String LOG_TAG = "ALMHRAB";

    public static void login(final Activity activity, final Map<String, String> params, final OnFetched listener) {
        sp = activity.getSharedPreferences(Utils.PREFS, activity.MODE_PRIVATE);
        spedit = sp.edit();
        Log.i("params", params.toString());
        UserOperations.getInstance(activity).sendPostRequest(Constants.Main_URL + "LoginSubscribe", params, new OnLoadedFinished() {
            @Override
            public void onSuccess(String response) {
                Log.i("//// ", response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    boolean Status = jsonObject.optBoolean("Status");
                    String message = jsonObject.optString("ResultText");
                    if (Status) {
                        JSONObject OtherData = jsonObject.optJSONObject("OtherData");
                        int Id = OtherData.optInt("Id");
                        String FullName = OtherData.optString("FullName");
                        String UserName = OtherData.optString("UserName");
                        String Mobile = OtherData.optString("Mobile");
                        String Email = OtherData.optString("Email");
                        String GUID = OtherData.optString("GUID");
                        int IdCity = OtherData.optInt("IdCity");
                        String IdCityText = OtherData.optString("IdCityText");
                        String IdCityTextEn = OtherData.optString("IdCityTextEn");
                        String MyName = OtherData.optString("MyName");
                        String Password = OtherData.optString("Password");
                        String img = Constants.IMAGE_URL + (OtherData.optString("img").replace("~", ""));
                        int Lat1 = OtherData.optInt("Lat_Degree");
                        int Lat2 = OtherData.optInt("Lat_Minute");
                        int Long1 = OtherData.optInt("Long_Degree");
                        int Long2 = OtherData.optInt("Long_Minute");
                        boolean IsMasjed = OtherData.optBoolean("IsMasjed");
                        boolean IsDeaf = OtherData.optBoolean("IsDeaf");
                        spedit.putBoolean("IsMasjed", IsMasjed).commit();
                        spedit.putBoolean("IsDeaf", IsDeaf).commit();
                        spedit.putInt("masjedId", Id).commit();
                        spedit.putInt("cityId", IdCity).commit();
                        spedit.putInt("lat1", Lat1).commit();
                        spedit.putInt("lat2", Lat2).commit();
                        spedit.putInt("long1", Long1).commit();
                        spedit.putInt("long2", Long2).commit();
                        spedit.putString("masjedGUID", GUID).commit();
                        spedit.putString("masjedName", MyName).commit();
                        spedit.putString("masjedImg", img).commit();
                        spedit.putString("masjedPW", Password).commit();
                        Users user = new Users(Id, FullName, UserName, Mobile, Email, GUID, IdCity, MyName, img);

                        listener.onSuccess(user);
                    } else listener.onFail(message + "");

                } catch (JSONException e) {
                    listener.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                listener.onFail(error);
            }
        });
    }

    public static void updateAccount(final Activity activity, Map<String, String> param, final OnFetched listener) {
        sp = activity.getSharedPreferences(Utils.PREFS, activity.MODE_PRIVATE);
        spedit = sp.edit();
        Log.i("//// params ", param.toString());
        UserOperations.getInstance(activity).sendPostRequest(Constants.Main_URL + "SubscribeProfileEdit", param, new OnLoadedFinished() {
            @Override
            public void onSuccess(String response) {
                Log.i("//// ", response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    boolean Status = jsonObject.optBoolean("Status");
                    String message = jsonObject.optString("ResultText");
                    if (Status) {
                        JSONObject OtherData = jsonObject.optJSONObject("OtherData");
                        int Id = OtherData.optInt("Id");
                        String FullName = OtherData.optString("FullName");
                        String UserName = OtherData.optString("UserName");
                        String Mobile = OtherData.optString("Mobile");
                        String Email = OtherData.optString("Email");
                        String GUID = OtherData.optString("GUID");
                        int IdCity = OtherData.optInt("IdCity");
                        String IdCityText = OtherData.optString("IdCityText");
                        String IdCityTextEn = OtherData.optString("IdCityTextEn");
                        String MyName = OtherData.optString("MyName");
                        String img = Constants.IMAGE_URL + (OtherData.optString("img").replace("~", ""));
                        int Lat1 = OtherData.optInt("Lat_Degree");
                        int Lat2 = OtherData.optInt("Lat_Minute");
                        int Long1 = OtherData.optInt("Long_Degree");
                        int Long2 = OtherData.optInt("Long_Minute");
                        spedit.putInt("masjedId", Id).commit();
                        spedit.putInt("cityId", IdCity).commit();
                        spedit.putInt("lat1", Lat1).commit();
                        spedit.putInt("lat2", Lat2).commit();
                        spedit.putInt("long1", Long1).commit();
                        spedit.putInt("long2", Long2).commit();
                        spedit.putString("masjedGUID", GUID).commit();
                        spedit.putString("masjedName", MyName).commit();
                        spedit.putString("masjedImg", img).commit();
                        Users user = new Users(Id, FullName, UserName, Mobile, Email, GUID, IdCity, MyName, img);

                        listener.onSuccess(user);
                    } else listener.onFail(message);

                } catch (JSONException e) {
                    listener.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                listener.onFail(error);
            }
        });
    }

    public static void UpdateSettings(final Activity activity, OptionSiteClass settings, final OnLoadedFinished listener) {
        sp = activity.getSharedPreferences(Utils.PREFS, activity.MODE_PRIVATE);
        spedit = sp.edit();
        int id = sp.getInt("masjedId", -1);
        String GUID = sp.getString("masjedGUID", "");
        String DeviceNo = sp.getString(Utils.DeviceNo, "");

        Map<String, String> param = new HashMap<>();
        param.put("IdSubscribe", id + "");
        param.put("GUID", GUID);
        param.put("DeviceNo", DeviceNo);
        param.put("DateHijri", settings.getDateHijri() + "");
        param.put("PhoneStatusVoice", settings.isPhoneStatusVoice() + "");
        param.put("PhoneStatusAlerts", settings.isPhoneStatusAlerts() + "");
        param.put("PhoneAlertsUrdu", settings.getPhoneAlertsUrdu());
        String phoneAlertsEnglish = settings.getPhoneAlertsEnglish();//.replace("!", "");
        String phoneAlertsArabic = settings.getPhoneAlertsArabic();//.replace("!", "");
        param.put("PhoneAlertsEnglish", phoneAlertsEnglish);
        param.put("PhoneAlertsArabic", phoneAlertsArabic);
        param.put("PhoneShowAlertsBeforEkama", settings.getPhoneShowAlertsBeforEkama() + "");
        param.put("StatusAthanVoiceF", settings.isStatusAthanVoiceF() + "");
        param.put("StatusEkamaVoiceF", settings.isStatusEkamaVoiceF() + "");
        param.put("StatusAthanVoiceD", settings.isStatusAthanVoiceD() + "");
        param.put("StatusEkamaVoiceD", settings.isStatusEkamaVoiceD() + "");
        param.put("StatusAthanVoiceA", settings.isStatusAthanVoiceA() + "");
        param.put("StatusEkamaVoiceA", settings.isStatusEkamaVoiceA() + "");
        param.put("StatusAthanVoiceM", settings.isStatusAthanVoiceM() + "");
        param.put("StatusEkamaVoiceM", settings.isStatusEkamaVoiceM() + "");
        param.put("StatusAthanVoiceI", settings.isStatusAthanVoiceI() + "");
        param.put("StatusEkamaVoiceI", settings.isStatusEkamaVoiceI() + "");
        param.put("IshaAzkarTimer", settings.getIshaAzkarTimer() + "");
        param.put("IshaEkamaTime", settings.getIshaEkamaTime() + "");
        param.put("IshaEkamaIsTime", settings.ishaEkamaIsTime() + "");
        param.put("IshaAzkar", settings.getIshaAzkar() + "");
        param.put("IshaEkama", settings.getIshaEkama() + "");
        param.put("MagribEkamaTime", settings.getMagribEkamaTime() + "");
        param.put("MagribEkamaIsTime", settings.isMagribEkamaIsTime() + "");
        param.put("MagribEkama", settings.getMagribEkama() + "");
        param.put("MagribAzkar", settings.getMagribAzkar() + "");
        param.put("MagribAzkarTimer", settings.getMagribAzkarTimer() + "");
        param.put("AsrEkamaIsTime", settings.isAsrEkamaIsTime() + "");
        param.put("AsrEkamaTime", settings.getAsrEkamaTime() + "");
        param.put("AsrEkama", settings.getAsrEkama() + "");
        param.put("AsrAzkar", settings.getAsrAzkar() + "");
        param.put("AsrAzkarTimer", settings.getAsrAzkarTimer() + "");
        param.put("DhuhrEkamaTime", settings.getDhuhrEkamaTime() + "");
        param.put("DhuhrEkamaIsTime", settings.isDhuhrEkamaIsTime() + "");
        param.put("DhuhrEkama", settings.getDhuhrEkama() + "");
        param.put("DhuhrAzkar", settings.getDhuhrAzkar() + "");
        param.put("DhuhrAzkarTimer", settings.getDhuhrAzkarTimer() + "");
        param.put("FajrEkama", settings.getFajrEkama() + "");
        param.put("FajrEkamaTime", settings.getFajrEkamaTime() + "");
        param.put("FajrEkamaIsTime", settings.isFajrEkamaIsTime() + "");
        param.put("FajrAzkar", settings.getFajrAzkar() + "");
        param.put("FajrAzkarTimer", settings.getFajrAzkarTimer() + "");
        param.put("AlShrouqEkama", settings.getAlShrouqEkama() + "");
        param.put("AlShrouqEkamaTime", settings.getAlShrouqEkamaTime() + "");
        param.put("AlShrouqEkamaIsTime", settings.isAlShrouqEkamaIsTime() + "");
        param.put("CloseScreenAfterIsha", settings.getCloseScreenAfterIsha() + "");
        param.put("RunScreenBeforeFajr", settings.getRunScreenBeforeFajr() + "");

        Log.i("////params ", param.toString());

        UserOperations.getInstance(activity).sendPostRequest(Constants.Main_URL + "EditOptionSite", param, new OnLoadedFinished() {
            @Override
            public void onSuccess(String response) {
                Log.i("////response ", response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    boolean Status = jsonObject.optBoolean("Status");
                    String message = jsonObject.optString("ResultText");
                    if (Status) {
                        listener.onSuccess(activity.getString(R.string.saved));
                    } else listener.onFail(message);

                } catch (JSONException e) {
                    listener.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                listener.onFail(error);
            }
        });
    }

    public static void addUpdateNews(final Activity activity, News object, final OnLoadedFinished listener) {
        sp = activity.getSharedPreferences(Utils.PREFS, activity.MODE_PRIVATE);
        spedit = sp.edit();
        int id = sp.getInt("masjedId", -1);
        String GUID = sp.getString("masjedGUID", "");
        String DeviceNo = sp.getString(Utils.DeviceNo, "");

        Map<String, String> param = new HashMap<>();
        param.put("IdSubscribe", id + "");
        param.put("GUID", GUID);
        param.put("DeviceNo", DeviceNo);
        param.put("Id", object.getId() + "");
        param.put("isDeleted", object.isDeleted() + "");
        param.put("sort", object.getSort() + "");
        param.put("UpdatedAt", object.getUpdatedAt());
        param.put("TextAds", object.getTextAds());
        param.put("FromDate", object.getFromDate());
        param.put("ToDate", object.getToDate());

        Log.i("////params ", param.toString());

        UserOperations.getInstance(activity).sendPostRequest(Constants.Main_URL + "NewsCreateEdit", param, new OnLoadedFinished() {
            @Override
            public void onSuccess(String response) {
                Log.i("////response ", response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    boolean Status = jsonObject.optBoolean("Status");
                    String message = jsonObject.optString("ResultText");
                    if (Status) {
                        listener.onSuccess(activity.getString(R.string.success_add));
                    } else listener.onFail(message);

                } catch (JSONException e) {
                    listener.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                listener.onFail(error);
            }
        });
    }

    public static void getAllNews(final Activity activity, final OnLoadedFinished listener) {
        sp = activity.getSharedPreferences(Utils.PREFS, activity.MODE_PRIVATE);
        spedit = sp.edit();
        int id = sp.getInt("masjedId", -1);
        String GUID = sp.getString("masjedGUID", "");
        String DeviceNo = sp.getString(Utils.DeviceNo, "");

        Map<String, String> param = new HashMap<>();
        param.put("IdSubscribe", id + "");
        param.put("GUID", GUID);
        param.put("DeviceNo", DeviceNo);
        param.put("lastUpdate", "0");

        Log.i("////params ", param.toString());

        UserOperations.getInstance(activity).sendPostRequest(Constants.Main_URL + "getAllNews", param, new OnLoadedFinished() {
            @Override
            public void onSuccess(String response) {
                Log.i("////response ", response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    boolean Status = jsonObject.optBoolean("Status");
                    String message = jsonObject.optString("ResultText");
                    if (Status) {
                        JSONArray news = jsonObject.optJSONArray("OtherData");
                        ArrayList<News> newsList = JsonHelper.jsonToNewsArray(news);
                        DBOperations db = new DBOperations(activity);
                        db.insertAllNews(newsList);
                        listener.onSuccess(activity.getString(R.string.success_add));
                    } else listener.onFail(message);

                } catch (JSONException e) {
                    listener.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                listener.onFail(error);
            }
        });
    }

    public static void NewsDelete(final Activity activity, int newsId, final OnLoadedFinished listener) {
        sp = activity.getSharedPreferences(Utils.PREFS, activity.MODE_PRIVATE);
        spedit = sp.edit();
        int id = sp.getInt("masjedId", -1);
        String GUID = sp.getString("masjedGUID", "");
        String DeviceNo = sp.getString(Utils.DeviceNo, "");

        Map<String, String> param = new HashMap<>();
        param.put("IdSubscribe", id + "");
        param.put("GUID", GUID);
        param.put("DeviceNo", DeviceNo);
        param.put("Id", newsId + "");

        Log.i("////params ", param.toString());

        UserOperations.getInstance(activity).sendPostRequest(Constants.Main_URL + "NewsDelete", param, new OnLoadedFinished() {
            @Override
            public void onSuccess(String response) {
                Log.i("////response ", response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    boolean Status = jsonObject.optBoolean("Status");
                    String message = jsonObject.optString("ResultText");
                    if (Status) {
                        listener.onSuccess(activity.getString(R.string.success_delete));
                    } else listener.onFail(message);

                } catch (JSONException e) {
                    listener.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                listener.onFail(error);
            }
        });
    }

    public static void addUpdateAzkar(final Activity activity, Azkar object, final OnLoadedFinished listener) {
        sp = activity.getSharedPreferences(Utils.PREFS, activity.MODE_PRIVATE);
        spedit = sp.edit();
        int id = sp.getInt("masjedId", -1);
        String GUID = sp.getString("masjedGUID", "");
        String DeviceNo = sp.getString(Utils.DeviceNo, "");

        Map<String, String> param = new HashMap<>();
        param.put("IdSubscribe", id + "");
        param.put("GUID", GUID);
        param.put("DeviceNo", DeviceNo);
        param.put("Id", object.getId() + "");
        param.put("isDeleted", object.isDeleted() + "");
        param.put("sort", object.getSort() + "");
        param.put("UpdatedAt", object.getUpdatedAt());
        param.put("TextAzakar", object.getTextAzakar());
        param.put("Count", object.getCount() + "");
        param.put("Fajr", object.isFajr() + "");
        param.put("Dhuhr", object.isDhuhr() + "");
        param.put("Asr", object.isAsr() + "");
        param.put("Magrib", object.isMagrib() + "");
        param.put("Isha", object.isha() + "");

        Log.i("////params ", param.toString());

        UserOperations.getInstance(activity).sendPostRequest(Constants.Main_URL + "AzakarCreateEdit", param, new OnLoadedFinished() {
            @Override
            public void onSuccess(String response) {
                Log.i("////response ", response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    boolean Status = jsonObject.optBoolean("Status");
                    String message = jsonObject.optString("ResultText");
                    if (Status) {
                        listener.onSuccess(activity.getString(R.string.success_add));
                    } else listener.onFail(message);

                } catch (JSONException e) {
                    listener.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                listener.onFail(error);
            }
        });
    }

    public static void getAllAzkar(final Activity activity, final OnLoadedFinished listener) {
        sp = activity.getSharedPreferences(Utils.PREFS, activity.MODE_PRIVATE);
        spedit = sp.edit();
        int id = sp.getInt("masjedId", -1);
        String GUID = sp.getString("masjedGUID", "");
        String DeviceNo = sp.getString(Utils.DeviceNo, "");

        Map<String, String> param = new HashMap<>();
        param.put("IdSubscribe", id + "");
        param.put("GUID", GUID);
        param.put("DeviceNo", DeviceNo);
        param.put("lastUpdate", "0");

        Log.i("////params ", param.toString());

        UserOperations.getInstance(activity).sendPostRequest(Constants.Main_URL + "getAllAzakar", param, new OnLoadedFinished() {
            @Override
            public void onSuccess(String response) {
                Log.i("////response ", response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    boolean Status = jsonObject.optBoolean("Status");
                    String message = jsonObject.optString("ResultText");
                    if (Status) {
                        JSONArray azakar = jsonObject.optJSONArray("OtherData");
                        ArrayList<Azkar> azkarList = JsonHelper.jsonToAzkarArray(azakar);
                        DBOperations db = new DBOperations(activity);
                        db.insertAllAzkar(azkarList);
                        listener.onSuccess(activity.getString(R.string.success_add));
                    } else listener.onFail(message);

                } catch (JSONException e) {
                    listener.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                listener.onFail(error);
            }
        });
    }

    public static void AzkarDelete(final Activity activity, int newsId, final OnLoadedFinished listener) {
        sp = activity.getSharedPreferences(Utils.PREFS, activity.MODE_PRIVATE);
        spedit = sp.edit();
        int id = sp.getInt("masjedId", -1);
        String GUID = sp.getString("masjedGUID", "");
        String DeviceNo = sp.getString(Utils.DeviceNo, "");

        Map<String, String> param = new HashMap<>();
        param.put("IdSubscribe", id + "");
        param.put("GUID", GUID);
        param.put("DeviceNo", DeviceNo);
        param.put("Id", newsId + "");

        Log.i("////params ", param.toString());

        UserOperations.getInstance(activity).sendPostRequest(Constants.Main_URL + "AzakarDelete", param, new OnLoadedFinished() {
            @Override
            public void onSuccess(String response) {
                Log.i("////response ", response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    boolean Status = jsonObject.optBoolean("Status");
                    String message = jsonObject.optString("ResultText");
                    if (Status) {
                        listener.onSuccess(activity.getString(R.string.success_delete));
                    } else listener.onFail(message);

                } catch (JSONException e) {
                    listener.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                listener.onFail(error);
            }
        });
    }

    public static void updateCity(final Activity activity, int cityId, final OnLoadedFinished listener) {
        sp = activity.getSharedPreferences(Utils.PREFS, activity.MODE_PRIVATE);
        spedit = sp.edit();
        int id = sp.getInt("masjedId", -1);
        String GUID = sp.getString("masjedGUID", "");
        String DeviceNo = sp.getString(Utils.DeviceNo, "");

        Map<String, String> param = new HashMap<>();
        param.put("IdSubscribe", id + "");
        param.put("GUID", GUID);
        param.put("DeviceNo", DeviceNo);
        param.put("IdCity", cityId + "");

        Log.i("////params ", param.toString());

        UserOperations.getInstance(activity).sendPostRequest(Constants.Main_URL + "SubscribeChangeCity", param, new OnLoadedFinished() {
            @Override
            public void onSuccess(String response) {
                Log.i("////response ", response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    boolean Status = jsonObject.optBoolean("Status");
                    String message = jsonObject.optString("ResultText");
                    if (Status) {
                        listener.onSuccess(activity.getString(R.string.success_edit));
                    } else listener.onFail(message);

                } catch (JSONException e) {
                    listener.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                listener.onFail(error);
            }
        });
    }

    public static void getAllKhotab(final Activity activity, final OnLoadedFinished listener) {
        sp = activity.getSharedPreferences(Utils.PREFS, activity.MODE_PRIVATE);
        spedit = sp.edit();
        int id = sp.getInt("masjedId", -1);
        String GUID = sp.getString("masjedGUID", "");
        String DeviceNo = sp.getString(Utils.DeviceNo, "");

        Map<String, String> param = new HashMap<>();
        param.put("IdSubscribe", id + "");
        param.put("GUID", GUID);
        param.put("DeviceNo", DeviceNo);
        param.put("lastUpdate", "0");

        Log.i("////params ", param.toString());

        UserOperations.getInstance(activity).sendPostRequest(Constants.Main_URL + "getAllKhotab", param, new OnLoadedFinished() {
            @Override
            public void onSuccess(String response) {
                Log.i("////response ", response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    boolean Status = jsonObject.optBoolean("Status");
                    String message = jsonObject.optString("ResultText");
                    if (Status) {
                        JSONArray jAKhotab = jsonObject.optJSONArray("OtherData");
                        ArrayList<Khotab> khotabList = JsonHelper.jsonToKhotabArray(jAKhotab);
                        DBOperations db = new DBOperations(activity);
                        if (khotabList.size() > 0) db.insertAllKhotab(khotabList);
                    } else listener.onFail(message);

                } catch (JSONException e) {
                    listener.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                listener.onFail(error);
            }
        });
    }

    public static JSONObject syncData(int id, String GUID, String lastUpdate, String DeviceNo) {
        String query = "getAllData?IdSubscribe=" + id + "&GUID=" + GUID + "&lastUpdate=" + lastUpdate + "&DeviceNo=" + DeviceNo + "";
        return getJsonObject2(query);
    }

    private static JSONObject getJsonObject2(String urlQuery) {

        String url = Constants.Main_URL + urlQuery;

        Log.d(LOG_TAG, "Request url : " + url);
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        JSONObject jo = new JSONObject();
        String data = "";
        try {
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity e = response.getEntity();
                data = EntityUtils.toString(e);
                Log.d(LOG_TAG, "Result before converting to json : " + data);
                jo = new JSONObject(data);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        } finally {
            return jo;
        }
    }

    public static boolean InsertDataToDB(int action, Activity activity, JSONObject o) {
        Log.d(LOG_TAG, "Sync Service Insert Data");
        sp = activity.getSharedPreferences(Utils.PREFS, activity.MODE_PRIVATE);
        spedit = sp.edit();
        try {
            DBOperations db = new DBOperations(activity);
//            db.open();
            JSONObject OtherData = o.optJSONObject("OtherData");
            JSONArray azakar = OtherData.optJSONArray("azakar");
            ArrayList<Azkar> azkarList = JsonHelper.jsonToAzkarArray(azakar);
            JSONArray news = OtherData.optJSONArray("news");
            ArrayList<News> newsList = JsonHelper.jsonToNewsArray(news);


            JSONArray cities = OtherData.optJSONArray("cities");
            ArrayList<City> citiesList = JsonHelper.jsonToCitiesArray(cities);
            db.insertCities(citiesList);

            JSONObject setting = OtherData.optJSONObject("setting");
            OptionSiteClass settings = JsonHelper.jsonToSettingsClass(setting);
            db.insertSettings(settings);

            JSONObject profile = OtherData.optJSONObject("profile");
            Log.d(LOG_TAG, "Sync  " + profile.toString());
            int Id = profile.optInt("Id");
            String FullName = profile.optString("FullName");
            String UserName = profile.optString("UserName");
            String Mobile = profile.optString("Mobile");
            String Email = profile.optString("Email");
            String GUID = profile.optString("GUID");
            int IdCity = profile.optInt("IdCity");
            String IdCityText = profile.optString("IdCityText");
            String IdCityTextEn = profile.optString("IdCityTextEn");
            String MyName = profile.optString("MyName");
            String img = Constants.IMAGE_URL + (profile.optString("img").replace("~", ""));
            int Lat1 = profile.optInt("Lat_Degree");
            int Lat2 = profile.optInt("Lat_Minute");
            int Long1 = profile.optInt("Long_Degree");
            int Long2 = profile.optInt("Long_Minute");
            boolean IsMasjed = profile.optBoolean("IsMasjed");
            boolean IsDeaf = profile.optBoolean("IsDeaf");
            String Password = profile.optString("Password");
            spedit.putBoolean("IsMasjed", IsMasjed).commit();
            spedit.putBoolean("IsDeaf", IsDeaf).commit();
//            spedit.putString("masjedPW", Password).commit();
            spedit.putInt("masjedId", Id).commit();
            spedit.putInt("cityId", IdCity).commit();
            spedit.putInt("lat1", Lat1).commit();
            spedit.putInt("lat2", Lat2).commit();
            spedit.putInt("long1", Long1).commit();
            spedit.putInt("long2", Long2).commit();
            spedit.putString("masjedGUID", GUID).commit();
            spedit.putString("masjedName", MyName).commit();
            spedit.putString("masjedImg", img).commit();
            spedit.putString("masjedPW", Password).commit();
            Log.i("//////", "Sync  " + Password);

            if (action == 0) {
                db.insertAllAzkar(azkarList);
                db.insertAllNews(newsList);
            } else {
                db.insertAzkar(azkarList);
                db.insertNews(newsList);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static void isStreaming(final Activity activity, int TimeExpected, final OnLoadedFinished listener) {
        sp = activity.getSharedPreferences(Utils.PREFS, activity.MODE_PRIVATE);
        spedit = sp.edit();
        int id = sp.getInt("masjedId", -1);
        String GUID = sp.getString("masjedGUID", "");
        String DeviceNo = sp.getString(Utils.DeviceNo, "");

        Map<String, String> param = new HashMap<>();
        param.put("IdSubscribe", id + "");
        param.put("GUID", GUID);
        param.put("DeviceNo", DeviceNo);
        param.put("TimeExpected", TimeExpected + "");
        Log.i("params: ", param.toString());
        UserOperations.getInstance(activity).sendPostRequest(Constants.Main_URL + "isStreaming", param, new OnLoadedFinished() {
            @Override
            public void onSuccess(String response) {
                Log.i("/////: ", response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    boolean Status = jsonObject.optBoolean("Status");
                    String message = jsonObject.optString("ResultText");
                    if (Status) {
                        listener.onSuccess(activity.getString(R.string.saved));
                    } else listener.onFail(message);

                } catch (JSONException e) {
                    listener.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                listener.onFail(error);
            }
        });
    }

}