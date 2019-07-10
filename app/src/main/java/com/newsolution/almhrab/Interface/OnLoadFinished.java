package com.newsolution.almhrab.Interface;

import org.json.JSONException;
import org.json.JSONObject;

public interface OnLoadFinished {
     void onSuccess(JSONObject jsonObject) throws JSONException;
     void onFail(String error);

}

