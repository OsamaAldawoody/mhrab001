package com.newsolution.almhrab.Weather;

import com.google.android.gms.plus.PlusShare;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONWeatherParser {
    public static Weather getWeather(String data) throws JSONException {
        Weather weather = new Weather();
        try {
            JSONObject jObj = new JSONObject(data);
            Location loc = new Location();
            JSONObject coordObj = getObject("coord", jObj);
            loc.setLatitude(getFloat("lat", coordObj));
            loc.setLongitude(getFloat("lon", coordObj));
            JSONObject sysObj = getObject("sys", jObj);
            loc.setCountry(getString("country", sysObj));
            loc.setSunrise((long) getInt("sunrise", sysObj));
            loc.setSunset((long) getInt("sunset", sysObj));
            loc.setCity(getString("name", jObj));
            weather.location = loc;
            JSONObject JSONWeather = jObj.getJSONArray("weather").getJSONObject(0);
            weather.currentCondition.setWeatherId(getInt("id", JSONWeather));
            weather.currentCondition.setDescr(getString(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, JSONWeather));
            weather.currentCondition.setCondition(getString("main", JSONWeather));
            weather.currentCondition.setIcon(getString("icon", JSONWeather));
            JSONObject mainObj = getObject("main", jObj);
            weather.currentCondition.setHumidity((float) getInt("humidity", mainObj));
            weather.currentCondition.setPressure((float) getInt("pressure", mainObj));
            weather.temperature.setMaxTemp(getFloat("temp_max", mainObj));
            weather.temperature.setMinTemp(getFloat("temp_min", mainObj));
            weather.temperature.setTemp(getFloat("temp", mainObj));
            JSONObject wObj = getObject("wind", jObj);
            weather.wind.setSpeed(getFloat("speed", wObj));
            weather.wind.setDeg(getFloat("deg", wObj));
            weather.clouds.setPerc(getInt("all", getObject("clouds", jObj)));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return weather;
    }

    private static JSONObject getObject(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getJSONObject(tagName);
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }
}
