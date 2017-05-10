package com.whenhi.hi.network;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by 王雷 on 2016/12/21.
 */
public class GsonCallbackWrapper<T> implements okhttp3.Callback {
    private static final String TAG = GsonCallbackWrapper.class.getSimpleName();

    private static final Handler sHandler = new Handler(Looper.getMainLooper());

    private static final Gson sGson = new Gson();

    private HttpAPI.Callback<T> mCallback;

    private TypeToken<T> mTypeToken;

    /**
     * @param callback  callback to be wrapped
     * @param typeToken why we need a TypeToken instance? see: https://github.com/google/gson/issues/828
     */
    public GsonCallbackWrapper(HttpAPI.Callback<T> callback, TypeToken<T> typeToken) {
        this.mCallback = callback;
        this.mTypeToken = typeToken;
    }

    @Override
    public void onResponse(final Call call, Response response){
        if (response.isSuccessful()) {
            ResponseBody responseBody = response.body();
            try {
                Log.d(TAG,"Whenhi request code:"+response.code());
                final T t = sGson.getAdapter(mTypeToken).fromJson(responseBody.charStream());
                deliverToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mCallback.onSuccess(t);
                        call.cancel();
                    }
                });
            } catch (IOException e) {
                Log.e(TAG,""+e.toString());
            }

        }
    }

    @Override
    public void onFailure(final Call call, final IOException e) {
        deliverToMainThread(new Runnable() {
            @Override
            public void run() {
                mCallback.onFailure(e);
                call.cancel();
            }
        });
    }

    public void deliverToMainThread(Runnable runnable) {
        sHandler.post(runnable);
    }
}
