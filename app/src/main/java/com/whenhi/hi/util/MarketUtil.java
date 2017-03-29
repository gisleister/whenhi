package com.whenhi.hi.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 王雷 on 2017/1/17.
 */

public class MarketUtil {

    public static void launchAppDetail(Context context, String appPkg, String channel) {

        String marketPkg = getMarketPkg(channel);
        try {
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg)) {
                intent.setPackage(marketPkg);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }else{
                openMarket(context);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getMarketPkg(String channel){
        Map<String,String> map = getMarketPkgMap();
        String marketPkg = map.get(channel);
        return  marketPkg;
    }

    private static Map<String,String> getMarketPkgMap(){
        Map<String,String> map= new HashMap<String, String>();
        map.put("baidu","com.baidu.appsearch");
        map.put("market360","com.qihoo.appstore");
        map.put("xiaomi","com.xiaomi.market");
        map.put("yingyongbao","com.tencent.android.qqdownloader");
        map.put("huawei","com.huawei.appmarket");
        map.put("wandoujia","com.wandoujia.phoenix2");
        map.put("taobao","com.taobao.appcenter");
        //map.put("sougou","");
        map.put("oppo","com.oppo.market");
        map.put("vivo","com.bbk.appstore");
        map.put("lenovo","com.lenovo.leos.appstore");
        map.put("sanxing","com.sec.android.app.samsungapps");
        return map;
    }

    private static  void openMarket(Context context){
        Intent startintent = new Intent("android.intent.action.MAIN");
        startintent.addCategory("android.intent.category.APP_MARKET");
        startintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startintent);
    }
}
