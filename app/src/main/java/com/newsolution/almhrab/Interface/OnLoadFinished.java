package com.newsolution.almhrab.Interface;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AmalK on 8/9/2015.
 */public interface OnLoadFinished {
    public void onSuccess(JSONObject jsonObject) throws JSONException;
    public void onFail(String error);

}

