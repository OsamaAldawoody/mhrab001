package com.newsolution.almhrab.WebServices;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by AmalKronz on 8/9/2015.
 */
public class UserOperationsProcessor {

    @SuppressLint("StaticFieldLeak")
    private static UserOperationsProcessor instance;
    private Context context;

    private UserOperationsProcessor(Context context) {
        this.context = context;
    }

    public static synchronized UserOperationsProcessor getInstance(Context context) {
        if (instance == null) {
            instance = new UserOperationsProcessor(context);
        }
        return instance;
    }

    public void sendRequest(int method, String url, Map<String, String> params,
                            Response.Listener<String> listener, Response.ErrorListener errorListener) {
        MyRequests mRequest = new MyRequests(method, url, listener, errorListener, params);
        //60 seconds - change to what you want
        int SOCKET_TIMEOUT = 90 * 1000;
        RetryPolicy policy = new DefaultRetryPolicy(SOCKET_TIMEOUT, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        mRequest.setRetryPolicy(policy);
        mRequest.setShouldCache(false);
        RequestQueueHandler.getInstance(context).addToRequestQueue(mRequest);

    }
}
