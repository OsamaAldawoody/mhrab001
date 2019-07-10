package com.newsolution.almhrab.WebServices;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class RequestQueueHandler {

    @SuppressLint("StaticFieldLeak")
    private static RequestQueueHandler instance;
    private final ImageLoader mImageLoader;
    private RequestQueue requestQueue;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    private RequestQueueHandler() {
        requestQueue = Volley.newRequestQueue(mContext);


        mImageLoader = new ImageLoader(this.requestQueue, new ImageLoader.ImageCache() {

            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);

            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }

            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
    }

    public static synchronized RequestQueueHandler getInstance(Context context) {
        mContext = context;
        if (instance == null) {
            instance = new RequestQueueHandler();
        }
        return instance;
    }

    <T> void addToRequestQueue(Request<T> req) {
        requestQueue.add(req);
    }

}
