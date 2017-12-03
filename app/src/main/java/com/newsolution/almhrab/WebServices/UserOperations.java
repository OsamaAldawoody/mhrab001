package com.newsolution.almhrab.WebServices;

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

    public void sendRequest(final String url, final OnLoadFinished onLoadFinished) {
        UserOperationsProcessor.getInstance(context).sendRequest(url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject o) {
                        try {
                            onLoadFinished.onSuccess(o);
                        } catch (JSONException e) {
                            onLoadFinished.onFail("An error occurred, try again");
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onLoadFinished.onFail(" This operation failed, Please try again");
                    }
                });
    }

    public void sendGetRequest(String url, OnLoadedFinished listener) {
        sendRequest(Request.Method.GET, url, null, listener);
    }

    public void sendPutRequest(String url, Map<String, String> params, OnLoadedFinished listener) {
        sendRequest(Request.Method.PUT, url, params, listener);
    }

    public void sendPostRequest(String url, Map<String, String> params, OnLoadedFinished listener) {
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
