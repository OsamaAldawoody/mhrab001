package com.newsolution.almhrab.WebServices;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.newsolution.almhrab.Interface.OnLoadFinished;
import com.newsolution.almhrab.Interface.OnLoadedFinished;
import com.newsolution.almhrab.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


/**
 * Created by AmalKronz on 8/9/2015.
 */
public class UserOperations {

    @SuppressLint("StaticFieldLeak")
    private static UserOperations instance;
    private Context context;

    private UserOperations(Context context) {
        this.context = context;
    }

    public static synchronized UserOperations getInstance(Context context) {
        if (instance == null)
            instance = new UserOperations(context);
        return instance;
    }

    void sendPostRequest(String url, Map<String, String> params, OnLoadedFinished listener) {
        sendRequest(Request.Method.POST, url, params, listener);
    }

    private void sendRequest(int method, final String url, Map<String,String> params, final OnLoadedFinished listener) {
        UserOperationsProcessor.getInstance(context).sendRequest(method, url, params, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String o) {
                        listener.onSuccess(o);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        NetworkResponse response = volleyError.networkResponse;
                        try {
                            if (response != null) {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers));
                                Log.e("error response", res);
//                                listener.onFail(res);
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        listener.onFail(context.getString(R.string.error));
                    }
                });
    }
}
