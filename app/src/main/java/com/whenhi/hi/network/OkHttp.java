package com.whenhi.hi.network;

import android.content.Context;


import com.whenhi.hi.util.FileUtil;

import okhttp3.Cache;
import okhttp3.OkHttpClient;


/**
 * Created by 王雷 on 2016/12/21.
 */

public class OkHttp {
    private static final int MAX_CACHE_SIZE = 200 * 1024 * 1024;
    private static OkHttpClient sOkHttpClient;

    public static void init(Context context) {
        Context applicationContext = context.getApplicationContext();

        sOkHttpClient = new OkHttpClient.Builder()
                .cache(new Cache(FileUtil.getHttpCacheDir(applicationContext), MAX_CACHE_SIZE))
                .build();
    }

    public static OkHttpClient getOkHttpClient() {
        return sOkHttpClient;
    }
}

