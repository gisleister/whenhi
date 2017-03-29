package com.whenhi.hi.util;

import android.util.Log;

import com.whenhi.hi.App;
import com.whenhi.hi.model.BaseModel;
import com.whenhi.hi.network.HttpAPI;

/**
 * Created by 王雷 on 2017/3/22.
 */

public class BindUtil {

    public static void bindJPush(){
        String nowTime = DateUtil.getNowTimeString(System.currentTimeMillis());

        App.setLastBindJPushTime(nowTime);
        HttpAPI.updatePushBind(new HttpAPI.Callback<BaseModel>() {//当用户没有登录的时候调用 当用户登录后要重新调用
            @Override
            public void onSuccess(BaseModel loginModel) {
                if(loginModel.getState() == 0){


                }else{

                }

            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }
}
