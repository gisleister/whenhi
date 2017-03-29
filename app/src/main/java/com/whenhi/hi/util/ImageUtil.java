/*
 * Copyright (C) 2015 MarkMjw
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.whenhi.hi.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.whenhi.hi.App;
import com.whenhi.hi.R;
import com.whenhi.hi.image.CircleTransform;
import com.whenhi.hi.model.BaseModel;
import com.whenhi.hi.model.Feed;
import com.whenhi.hi.model.Image;
import com.whenhi.hi.network.HttpAPI;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.R.attr.duration;

/**
 * Image util.
 *
 * @author markmjw
 * @since 1.0.0
 */
public class ImageUtil {
    private static final String TAG = "ImageUtil";

    public static final int TYPE_WECHAT_FRIEND = 0;
    public static final int TYPE_WECHAT_TIMELINE = 1;

    /** Min supported version. */
    private static final int MIN_SUPPORTED_VERSION = 0x21020001;

    private static final int MAX_IMAGE_LENGTH = 32 * 1024;
    private static final int DEFAULT_MAX_SIZE = 150;

    public static Bitmap zoomOut(Bitmap bitmap) {
        Bitmap dstBitmap = null;
        if (null != bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            if (width <= 0 || height <= 0) return null;

            int w, h;
            float scale = height * 1.0f / width;
            if (width > height) {
                w = DEFAULT_MAX_SIZE;
                h = (int) (w * scale);
            } else {
                h = DEFAULT_MAX_SIZE;
                w = (int) (h / scale);
            }

            dstBitmap = ImageUtil.zoom(bitmap, w, h);
            byte[] data = ImageUtil.bitmapToBytes(dstBitmap, false);

            while (data.length > MAX_IMAGE_LENGTH) {
                dstBitmap.recycle();

                w -= 10;
                h = (int) (w * scale);

                dstBitmap = ImageUtil.zoom(bitmap, w, h);
                data = ImageUtil.bitmapToBytes(dstBitmap, false);
            }
        }

        return dstBitmap;
    }

    /**
     * Convert Bitmap to byte[]
     *
     * @param bitmap      the source bitmap
     * @param needRecycle need recycle
     * @return byte[]
     */
    public static byte[] bitmapToBytes(Bitmap bitmap, boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bitmap.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return result;
    }

    /**
     * Scale bitmap with width and height.
     *
     * @param bitmap the source bitmap
     * @param w      the width
     * @param h      the height
     * @return the bitmap
     */
    public static Bitmap zoom(Bitmap bitmap, int w, int h) {
        if (null == bitmap) {
            return null;
        }

        try {
            float scaleWidth = w * 1.0f / bitmap.getWidth();
            float scaleHeight = h * 1.0f / bitmap.getHeight();

            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);

            Bitmap result = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(result);

            canvas.drawBitmap(bitmap, matrix, null);

            return result;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }





   /* public static Bitmap mBitmap = null;
    public static Bitmap mFullBitmap = null;

    public static  void getBitMapFromUrl(final Context context, final String imageUrl){
        Glide.with( context ).load( imageUrl ).asBitmap().into( target ) ;
    }

    private static SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            //图片加载完成
            mBitmap = zoomOut(bitmap);
            mFullBitmap = bitmap;
        }
    };*/

    private static Bitmap bitmap = null;
    public static void getBitmap(final Context context, final String url){

        if(TextUtils.isEmpty(url))
            return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                FutureTarget<File> future =  Glide.with(context)
                        .load(url).downloadOnly(480,800);
                try {
                    File cacheFile = future.get();
                    String path = cacheFile.getAbsolutePath();
                    bitmap=BitmapFactory.decodeFile(path);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public static Bitmap getShareBitmap(){
        return bitmap;
    }

    public static void setShareBitmap(Bitmap bm){
        bitmap = bm;
    }


    public static  String getShareImageUrl(Feed feed){
        String url = "";
        if(feed.getFeedCategory() == 0 ||feed.getFeedCategory() == 1 || feed.getFeedCategory() == 4 || feed.getFeedCategory() == 6 || feed.getFeedCategory() == 8){
            url = feed.getImageUrl();
        }else if(feed.getFeedCategory() == 2 || feed.getFeedCategory() == 3 || feed.getFeedCategory() == 5){
            List<Image> images = feed.getResList();
            if(images.size() > 0){
                url = images.get(0).getContent();
            }
        }


        return url;
    }

    public static ArrayList<String> getShareImageUrls(Feed feed){
        ArrayList<String> urls = new ArrayList<>();
        if(feed.getFeedCategory() == 0 || feed.getFeedCategory() == 1 || feed.getFeedCategory() == 4 || feed.getFeedCategory() == 6 || feed.getFeedCategory() == 8){
            urls.add(feed.getImageUrl());
        }else if(feed.getFeedCategory() == 2 || feed.getFeedCategory() == 3 || feed.getFeedCategory() == 5){
            List<Image> images = feed.getResList();
            for(int i = 0; i < images.size(); i++){
                Image image = images.get(i);
                urls.add(image.getContent());
            }
        }


        return urls;
    }


    public static void detailImage(Context context, final int feedId, final int feedCategory, String imageUrl, ImageView imageView){

        Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//设置为磁盘缓存 否则加载gif的时候会很慢呢
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        //此处添加上报机制
                        HttpAPI.noresReport(feedId,feedCategory,new HttpAPI.Callback<BaseModel>() {
                            @Override
                            public void onSuccess(BaseModel baseModel) {
                                if(baseModel.getState() == 0){

                                }else{

                                }

                            }

                            @Override
                            public void onFailure(Exception e) {

                            }
                        });
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        //imageView.setImageDrawable(resource);
                        return false;
                    }
                })
                .into(imageView);

    }

    public static void avatarImage(Context context, String imageUrl, ImageView imageView){
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.mipmap.user_default)
                //.priority(Priority.LOW)
                .transform(new CircleTransform(context))
                .error(R.mipmap.user_default)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//设置为磁盘缓存 否则加载gif的时候会很慢呢
                //.override(33, 33)
                .into(imageView);
    }


    private static int width = App.deviceWidth();
    private static int height = width*2/3;

    public static void itemImage(final Context context, final int feedId, final int feedCategory, String imageUrl, ImageView imageView){
        Glide.with(context)
                .load(imageUrl)
               // .override(300,100)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//设置为磁盘缓存 否则加载gif的时候会很慢呢
                .listener(new RequestListener<String, GlideDrawable>() {//监听数据加载数否正常
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        //此处添加上报机制
                        HttpAPI.noresReport(feedId,feedCategory,new HttpAPI.Callback<BaseModel>() {
                            @Override
                            public void onSuccess(BaseModel baseModel) {
                            }

                            @Override
                            public void onFailure(Exception e) {

                            }
                        });
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        /*try{
                            // 计算动画时长
                            GifDrawable drawable = (GifDrawable) resource;
                            GifDecoder decoder = drawable.getDecoder();
                            int duration = 0;
                            for (int i = 0; i < drawable.getFrameCount(); i++) {
                                duration += decoder.getDelay(i);
                            }
                            isGif = true;

                        }catch (Exception e){
                            isGif = false;
                        }*/

                        //Glide.get(context).clearMemory();
                        //System.gc();

                        return false;
                    }
                })
                .into(new GlideDrawableImageViewTarget(imageView, 1));//仅播放一次


    }


    private static boolean isGif = false;






}
