package com.aaramon.aaramonjio.dataaccess;

import android.app.Application;
import android.content.Context;

import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.aaramon.aaramonjio.order.DatabaseHandler;
import com.aaramon.aaramonjio.order.DatabaseManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static AppController mInstance;

    private String miniOrderValue;

    public String getMiniOrderValue() {
        return miniOrderValue;
    }

    public void setMiniOrderValue(String miniOrderValue) {
        this.miniOrderValue = miniOrderValue;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseManager.initializeInstance(new DatabaseHandler(this));
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext(),new HurlStack());
        }

        return mRequestQueue;
    }


    public ImageLoader getImageLoader() {

        getRequestQueue();

        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
