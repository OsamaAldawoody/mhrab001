package com.newsolution.almhrab.WebServices;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AmalK on 8/9/2015.
 */
public class MyRequests extends StringRequest {
    private Map<String, String> params;

    public MyRequests(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener
            , Map<String, String> params) {
        super(method, url, listener, errorListener);
        this.params = params;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
//    @Override
//    public Map<String, String> getHeaders() throws AuthFailureError {
//        Map<String, String> params = new HashMap<String, String>();
////        params.put("Content-Type", "application/x-www-form-urlencoded");
//        params.put("Content-Type", "pplication/json");
//        return params;
//    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String utf8String = new String(response.data, "UTF-8");
            return Response.success(new String(utf8String), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}