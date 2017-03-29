package com.whenhi.hi.listener;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.whenhi.hi.model.BaseModel;
import com.whenhi.hi.network.HttpAPI;

/**
 * Created by 王雷 on 2017/2/17.
 */

public class GlideListener {

    private int feedId;
    private int feedCategory;
    private boolean erroReport;


    public GlideListener(){

    }

    private RequestListener<String, GlideDrawable> listener= new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

            if(erroReport){
                HttpAPI.noresReport(feedId,feedCategory,new HttpAPI.Callback<BaseModel>() {
                    @Override
                    public void onSuccess(BaseModel baseModel) {
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });

            }

            //可以在这里处理你想做的×××如果你使用了error方法指定了出错时的占位资源，那么记得返回false×××
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            return false;
        }
    };

    public int getFeedId() {
        return feedId;
    }

    public void setFeedId(int feedId) {
        this.feedId = feedId;
    }

    public int getFeedCategory() {
        return feedCategory;
    }

    public void setFeedCategory(int feedCategory) {
        this.feedCategory = feedCategory;
    }

    public RequestListener<String, GlideDrawable> getListener() {
        return listener;
    }

    public void setListener(RequestListener<String, GlideDrawable> listener) {
        this.listener = listener;
    }

    public boolean getErroReport() {
        return erroReport;
    }

    public void setErroReport(boolean erroReport) {
        this.erroReport = erroReport;
    }
}
