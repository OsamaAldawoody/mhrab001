package com.newsolution.almhrab.Weather;

import android.location.Location;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherHttpClient {
    private static String APPID = "&APPID=d8f97354cd6c8b69845ff36cac573557";
    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?APPID=043d0e3f20b5184472d6f89eabe1c01f";
    private static String URL = "http://api.openweathermap.org/data/2.5/weather?q=Salalah&APPID=d8f97354cd6c8b69845ff36cac573557";

    public String getWeatherData(Location location) {
        HttpURLConnection con = null;
        InputStream is = null;
        try {
            con = (HttpURLConnection) new URL("http://api.openweathermap.org/data/2.5/weather?APPID=d8f97354cd6c8b69845ff36cac573557" + "&lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&units=metric").openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                buffer.append(line + "\r\n");
            }
            is.close();
            con.disconnect();
            String stringBuffer = buffer.toString();
            try {
                is.close();
            } catch (Throwable th) {
            }
            try {
                con.disconnect();
                return stringBuffer;
            } catch (Throwable th2) {
                return stringBuffer;
            }
        } catch (Throwable th3) {
        }
        con.disconnect();
        return null;
    }

    public String getImage(String code) {
        return code;
    }
}
