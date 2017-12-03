package com.newsolution.almhrab.WebServices;

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
    private final int SOCKET_TIMEOUT = 90 * 1000;//60 seconds - change to what you want

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

    public void sendRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
//        Request mRequest = new Request( url, listener, errorListener, params);
//        RequestQueueHandler.getInstance(context).addToRequestQueue(mRequest);

        JsonObjectRequest mRequest = new JsonObjectRequest(url, null, listener, errorListener){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String utf8String = new String(response.data, "UTF-8");
                    return Response.success(new JSONObject(utf8String), HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        mRequest.setShouldCache(false);
        RequestQueueHandler.getInstance(context).addToRequestQueue(mRequest);
    }
    public void sendRequest(int method, String url, Map<String, String> params,
                            Response.Listener<String> listener, Response.ErrorListener errorListener) {
        MyRequests mRequest = new MyRequests(method, url, listener, errorListener, params);
        RetryPolicy policy = new DefaultRetryPolicy(SOCKET_TIMEOUT, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        mRequest.setRetryPolicy(policy);
        mRequest.setShouldCache(false);
        RequestQueueHandler.getInstance(context).addToRequestQueue(mRequest);
//
//        JsonObjectRequest mRequest = new JsonObjectRequest(url, null, listener, errorListener);
//        RequestQueueHandler.getInstance(context).addToRequestQueue(mRequest);
    }
}
