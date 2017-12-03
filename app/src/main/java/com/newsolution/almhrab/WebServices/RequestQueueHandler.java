package com.newsolution.almhrab.WebServices;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by AmalKronz on 24/03/2017.
 */
public class RequestQueueHandler {

    private static RequestQueueHandler instance;
    private final ImageLoader mImageLoader;
    private RequestQueue requestQueue;
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

public RequestQueue getRequestQueue(){
        return requestQueue;
        }

public ImageLoader getImageLoader(){
        return mImageLoader;
        }


    public static synchronized RequestQueueHandler getInstance(Context context) {
        mContext = context;
        if (instance == null) {
            instance = new RequestQueueHandler();
        }
        return instance;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        requestQueue.add(req);
    }
//    public void addToRequestQueue(MyRequest req) {
//        requestQueue.add(req);
//    }
}
