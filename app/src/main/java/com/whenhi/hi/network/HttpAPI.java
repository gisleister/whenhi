package com.whenhi.hi.network;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.whenhi.hi.App;
import com.whenhi.hi.Constants;
import com.whenhi.hi.model.BaseModel;
import com.whenhi.hi.model.ChargeRecordModel;
import com.whenhi.hi.model.CommentModel;
import com.whenhi.hi.model.DiscoveryModel;
import com.whenhi.hi.model.FeedModel;
import com.whenhi.hi.model.Feed;
import com.whenhi.hi.model.LoginModel;
import com.whenhi.hi.model.MessageModel;
import com.whenhi.hi.model.ChargeModel;
import com.whenhi.hi.model.TicketModel;
import com.whenhi.hi.model.UpdateModel;
import com.whenhi.hi.model.UserModel;
import com.whenhi.hi.model.UserScoreModel;
import com.whenhi.hi.model.UserIncomeRecordModel;
import com.whenhi.hi.platform.PlatformConfig;
import com.whenhi.hi.platform.login.qq.QQUserInfo;
import com.whenhi.hi.platform.login.wechat.WechatLoginResult;
import com.whenhi.hi.platform.login.wechat.WechatUserInfo;
import com.whenhi.hi.platform.login.weibo.WeiboUserInfo;
import com.whenhi.hi.platform.model.Login;
import com.whenhi.hi.platform.model.Share;
import com.whenhi.hi.util.ParamUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by 王雷 on 2016/12/21.
 */

public class HttpAPI {


    private static final String TAG = HttpAPI.class.getSimpleName();



    public interface Callback<T> {
        void onSuccess(T t);

        void onFailure(Exception e);
    }


    public static String buildUrl(String url, Map<String, String> params) {
        String urlStr = url;
        if (!urlStr.contains("?")) {
            urlStr += '?';
        }
        urlStr += encodeParams(params);
        return urlStr;
    }

    public static String encodeParams(Map<String, String> params) {
        StringBuilder encodedParams = new StringBuilder();
        String paramsEncoding = "UTF-8";
        try {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            int size = entrySet.size();
            int index = 0;
            for (Map.Entry<String, String> entry : entrySet) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                if (++index < size) {
                    encodedParams.append('&');
                }
            }
            return encodedParams.toString();
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }


    public static void requestWechatToken(String code, final Callback<WechatLoginResult> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("appid", PlatformConfig.getInstance().getWechatId());
        params.put("secret", PlatformConfig.getInstance().getWechatSecret());
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        String url = buildUrl(Constants.URL_TOKEN, params);
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<WechatLoginResult>(callback, new TypeToken<WechatLoginResult>() {
        }));
    }


    public static void requestWechatUserInfo(String openId, String accessToken, final Callback<WechatUserInfo> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);
        params.put("openid", openId);
        String url = buildUrl(Constants.URL_WECHAT_USER, params);;
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<WechatUserInfo>(callback, new TypeToken<WechatUserInfo>() {
        }));
    }


    public static void requestWeiboUserInfo(String openId, String accessToken, final Callback<WeiboUserInfo> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", openId);
        params.put("access_token", accessToken);
        params.put("source", PlatformConfig.getInstance().getWeiboKey());
        String url = buildUrl(Constants.URL_GET_USER_INFO, params);;
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<WeiboUserInfo>(callback, new TypeToken<WeiboUserInfo>() {
        }));
    }

    public static void requestList(String fun, String extras, int pageNo,final Callback<FeedModel> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());

        /**/params.put("uid", App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());


        params.put("pageNo", ""+pageNo);
        if(!"".equals(extras)){
            params.put("extras",extras);
        }


        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);

        //Map<String, String> params = new HashMap<>();
        params.put("sig", sig);

        String url = buildUrl(Constants.API_FEED_LIST_URL+fun, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<FeedModel>(callback, new TypeToken<FeedModel>() {
        }));
    }


    public static void requestComment(int feedId, String extras, int pageNo,final Callback<CommentModel> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());
        params.put("uid", ""+ App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());
        params.put("pageNo", ""+pageNo);
        params.put("feedId", ""+feedId);
        if(!"".equals(extras)){
            params.put("extras",extras);
        }

        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_COMMENT_LIST_URL, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<CommentModel>(callback, new TypeToken<CommentModel>() {
        }));
    }


    public static void updateUserInfo(Login login, final Callback<LoginModel> callback) {

        int type = login.getType();

        String nickName = "";
        String logo = "";
        int authSrc = 0;
        String authOpenId = "";
        String authToken = "";
        String gender = "U";
        int invitorId = 0;
        if( type== 1) {//qq登录
            QQUserInfo info = login.getqQUserInfo();
            nickName = info.nickname;
            logo = info.figureurl_qq_2;
            authSrc = 1;
            authOpenId = login.getAuthResult().id;
            authToken = login.getAuthResult().accessToken;
            if(info.gender.equals("男")){
                gender = "M";
            }else if(info.gender.equals("女")){
                gender = "F";
            }
            //信息发给服务器
        }else if(type == 2){//wx登录
            WechatUserInfo info = login.getWechatUserInfo();
            nickName = info.nickname;
            logo = info.headimgurl;
            authSrc = 2;
            authOpenId = login.getAuthResult().id;
            authToken = login.getAuthResult().accessToken;
            if(info.sex == 1){
                gender = "M";
            }else if(info.sex == 2) {
                gender = "F";
            }
            //信息发给服务器
        }else if(type == 3){//wb登录
            WeiboUserInfo info = login.getWeiboUserInfo();
            nickName = info.screen_name;
            logo = info.profile_image_url;
            authSrc = 3;
            authOpenId = login.getAuthResult().id;
            authToken = login.getAuthResult().accessToken;
            if(info.gender.equals("m")){
                gender = "M";
            }else if(info.gender.equals("w")){
                gender = "F";
            }
            //信息发给服务器
        }


        App.userGender(gender);

        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());
        params.put("uid", ""+ App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());
        params.put("nickName", nickName);
        params.put("logo", logo);
        params.put("authSrc", ""+authSrc);
        params.put("authOpenId", authOpenId);
        params.put("authToken", authToken);
        params.put("invitorId", ""+invitorId);
        params.put("gender", gender);



        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_USER_UPDATE_URL, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<LoginModel>(callback, new TypeToken<LoginModel>() {
        }));
    }



    public static void updateShareInfo(Share share,Feed feed, final Callback<BaseModel> callback) {



        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());
        params.put("feedId", ""+feed.getId());
        params.put("feedCategory", ""+feed.getFeedCategory());
        params.put("sharePlatform", ""+share.getType());
        params.put("type", ""+feed.getType());
        params.put("uid", ""+ App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());



        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_USER_SHARE_URL, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<BaseModel>(callback, new TypeToken<BaseModel>() {
        }));
    }


    public static void updateClickInfo(Feed feed, final Callback<BaseModel> callback) {



        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());
        params.put("feedId", ""+feed.getId());
        params.put("feedCategory", ""+feed.getFeedCategory());
        params.put("uid", ""+ App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());



        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_USER_CLICK_URL, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<BaseModel>(callback, new TypeToken<BaseModel>() {
        }));
    }




    public static void getMessage(int pageNo,String extras,final Callback<MessageModel> callback) {



        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());
        params.put("pageNo", ""+pageNo);
        params.put("uid", ""+ App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());
        if(!"".equals(extras)){
            params.put("extras",extras);
        }




        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_SYS_MESSAGE_URL, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<MessageModel>(callback, new TypeToken<MessageModel>() {
        }));
    }



    public static void updatePushBind(final Callback<BaseModel> callback) {

        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());
        params.put("regId", ""+ App.getJPushRegisterID());
        params.put("packageName",App.getAppPkg());
        params.put("buildNo",App.getBuildTime());
        params.put("channel",App.getChannel("UMENG_CHANNEL"));
        params.put("uid", ""+ App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());





        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_SYS_PUSH_BIND_URL, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<BaseModel>(callback, new TypeToken<BaseModel>() {
        }));
    }


    public static void updateDeviceInit(final Callback<BaseModel> callback) {

        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());
        params.put("deviceName", ""+ App.getPhoneModel());
        params.put("deviceBrand",App.getPhoneBrand());
        params.put("osName","android");
        params.put("osVersion",App.getBuildVersion());
        params.put("screenWidth",""+App.deviceWidth());
        params.put("screenHeight",""+App.deviceHeight());
        params.put("serialNo",App.getIMEI());
        params.put("clientUA",App.getUserAgent());
        params.put("deviceId",App.getDeviceId());
        params.put("clientMAC",App.getMac());
        params.put("uid", ""+ App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());





        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_DEVICE_INIT, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<BaseModel>(callback, new TypeToken<BaseModel>() {
        }));
    }


    public static void updateApp(final Callback<UpdateModel> callback) {

        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());
        params.put("appName", App.getApplicationName());
        params.put("uid", ""+ App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());
        params.put("channel",App.getChannel("UMENG_CHANNEL"));





        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_UPDATE_APP, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<UpdateModel>(callback, new TypeToken<UpdateModel>() {
        }));
    }

    public static void getUserDetail(final Callback<UserModel> callback) {

        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());
        params.put("userId", ""+ App.getUserId());
        params.put("uid", ""+ App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());





        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_USER_DETAIL, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<UserModel>(callback, new TypeToken<UserModel>() {
        }));
    }


    public static void getUserScoreRecord(int pageNo, String extras, final Callback<UserIncomeRecordModel> callback) {

        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());
        params.put("userId", ""+ App.getUserId());
        params.put("uid", ""+ App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());

        params.put("pageNo", ""+pageNo);
        if(!"".equals(extras)){
            params.put("extras",extras);
        }





        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_USER_SCORE_RECORD, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<UserIncomeRecordModel>(callback, new TypeToken<UserIncomeRecordModel>() {
        }));
    }

    public static void userCheckini(final Callback<BaseModel> callback) {

        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());
        params.put("userId", ""+ App.getUserId());
        params.put("uid", ""+ App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());





        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_USER_CHECK_IN, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<BaseModel>(callback, new TypeToken<BaseModel>() {
        }));
    }

    public static void noresReport(int feedId, int feedCategory,final Callback<BaseModel> callback) {

        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());
        params.put("userId", ""+ App.getUserId());
        params.put("uid", ""+ App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());

        params.put("feedId",""+feedId);
        params.put("feedCategory",""+feedCategory);





        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_NORES_REPORT, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<BaseModel>(callback, new TypeToken<BaseModel>() {
        }));
    }


    public static void loveFeed(int feedId, final Callback<BaseModel> callback) {

        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());
        params.put("userId", ""+ App.getUserId());
        params.put("uid", ""+ App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());

        params.put("feedId",""+feedId);





        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_USER_LOVE_URL, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<BaseModel>(callback, new TypeToken<BaseModel>() {
        }));
    }

    public static void disLoveFeed(int feedId, final Callback<BaseModel> callback) {

        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());
        params.put("userId", ""+ App.getUserId());
        params.put("uid", ""+ App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());

        params.put("feedId",""+feedId);





        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_USER_DIS_LOVE_URL, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<BaseModel>(callback, new TypeToken<BaseModel>() {
        }));
    }

    public static void favFeed(int feedId, int feedCategory,final Callback<BaseModel> callback) {

        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());
        params.put("userId", ""+ App.getUserId());
        params.put("uid", ""+ App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());

        params.put("feedId",""+feedId);
        params.put("feedCategory",""+feedCategory);





        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_USER_FAV_URL, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<BaseModel>(callback, new TypeToken<BaseModel>() {
        }));
    }

    public static void disFavFeed(int feedId, int feedCategory,final Callback<BaseModel> callback) {

        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());
        params.put("userId", ""+ App.getUserId());
        params.put("uid", ""+ App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());

        params.put("feedId",""+feedId);
        params.put("feedCategory",""+feedCategory);





        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_USER_DIS_FAV_URL, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<BaseModel>(callback, new TypeToken<BaseModel>() {
        }));
    }

    public static void addComment(int feedId, int targetId, int targetType,String content,final Callback<BaseModel> callback) {

        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());
        params.put("userId", ""+ App.getUserId());
        params.put("uid", ""+ App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());

        params.put("feedId",""+feedId);
        params.put("targetId",""+targetId);
        params.put("content",content);
        params.put("targetType",""+targetType);





        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_COMMENT_ADD, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<BaseModel>(callback, new TypeToken<BaseModel>() {
        }));
    }


    public static void discoveryHome(final Callback<DiscoveryModel> callback) {

        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());
        params.put("userId", ""+ App.getUserId());
        params.put("uid", ""+ App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());






        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_DISCOVERY_HOME, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<DiscoveryModel>(callback, new TypeToken<DiscoveryModel>() {
        }));
    }


    public static void craftList(String extras, int pageNo,final Callback<FeedModel> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());

        /**/params.put("uid", App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());


        params.put("pageNo", ""+pageNo);
        if(!"".equals(extras)){
            params.put("extras",extras);
        }


        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);

        //Map<String, String> params = new HashMap<>();
        params.put("sig", sig);

        String url = buildUrl(Constants.API_DISCOVERY_PAST, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<FeedModel>(callback, new TypeToken<FeedModel>() {
        }));
    }

    public static void maleList(String extras, int pageNo,final Callback<FeedModel> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());

        /**/params.put("uid", App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());
        params.put("gender", App.getGender());


        params.put("pageNo", ""+pageNo);
        if(!"".equals(extras)){
            params.put("extras",extras);
        }


        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);

        //Map<String, String> params = new HashMap<>();
        params.put("sig", sig);

        String url = buildUrl(Constants.API_DISCOVERY_MALE, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<FeedModel>(callback, new TypeToken<FeedModel>() {
        }));
    }


    public static void inviteCode(String inviteCode,final Callback<BaseModel> callback) {

        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());
        params.put("regId", ""+ App.getJPushRegisterID());
        params.put("packageName",App.getAppPkg());
        params.put("buildNo",App.getBuildTime());
        params.put("channel",App.getChannel("JPUSH_CHANNEL"));
        params.put("uid", ""+ App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());
        params.put("inviteCode",inviteCode);





        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_USER_INVITECODE_INPUT, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<BaseModel>(callback, new TypeToken<BaseModel>() {
        }));
    }



    public static void getChargeItems(String extras, int pageNo,final Callback<ChargeModel> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());

        /**/params.put("uid", App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());


        params.put("pageNo", ""+pageNo);
        if(!"".equals(extras)){
            params.put("extras",extras);
        }


        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_USER_CHARGES_ITEMS, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<ChargeModel>(callback, new TypeToken<ChargeModel>() {
        }));
    }




    public static void loveList(String extras, int pageNo,final Callback<FeedModel> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());

        /**/params.put("uid", App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());


        params.put("pageNo", ""+pageNo);
        if(!"".equals(extras)){
            params.put("extras",extras);
        }


        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);

        //Map<String, String> params = new HashMap<>();
        params.put("sig", sig);

        String url = buildUrl(Constants.API_DISCOVERY_LOVE, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<FeedModel>(callback, new TypeToken<FeedModel>() {
        }));
    }

    public static void shareList(String extras, int pageNo,final Callback<FeedModel> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());

        /**/params.put("uid", App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());


        params.put("pageNo", ""+pageNo);
        if(!"".equals(extras)){
            params.put("extras",extras);
        }


        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);

        //Map<String, String> params = new HashMap<>();
        params.put("sig", sig);

        String url = buildUrl(Constants.API_DISCOVERY_SHARE, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<FeedModel>(callback, new TypeToken<FeedModel>() {
        }));
    }


    public static void userIndexList(int pageNo, String extras, final Callback<UserScoreModel> callback) {



        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());
        params.put("pageNo", ""+pageNo);
        params.put("uid", ""+ App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());
        if(!"".equals(extras)){
            params.put("extras",extras);
        }




        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_DISCOVERY_USERS, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<UserScoreModel>(callback, new TypeToken<UserScoreModel>() {
        }));
    }


    public static void sendCode(String mobile,final Callback<BaseModel> callback) {



        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());
        params.put("mobile", mobile);
        params.put("uid", ""+ App.getUserId());
        params.put("t", App.getToken());




        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_REG_SENDCODE, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<BaseModel>(callback, new TypeToken<BaseModel>() {
        }));
    }


    public static void inviteSMSCode(String code,String mobile,final Callback<BaseModel> callback) {

        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());
        params.put("regId", ""+ App.getJPushRegisterID());
        params.put("packageName",App.getAppPkg());
        params.put("buildNo",App.getBuildTime());
        params.put("channel",App.getChannel("JPUSH_CHANNEL"));
        params.put("uid", ""+ App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());
        params.put("code",code);
        params.put("mobile",mobile);





        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_REG_SMS_CODE, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<BaseModel>(callback, new TypeToken<BaseModel>() {
        }));
    }



    public static void favList(String extras, int pageNo,final Callback<FeedModel> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());

        /**/params.put("uid", App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());


        params.put("pageNo", ""+pageNo);
        if(!"".equals(extras)){
            params.put("extras",extras);
        }


        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);

        //Map<String, String> params = new HashMap<>();
        params.put("sig", sig);

        String url = buildUrl(Constants.API_FEED_FAV_LIST_URL, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<FeedModel>(callback, new TypeToken<FeedModel>() {
        }));
    }


    public static void doCharge(int itemId,String mobile,final Callback<BaseModel> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());

        /**/params.put("uid", App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());


        params.put("itemId", ""+itemId);
        params.put("mobile", ""+mobile);



        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_USER_CHARGES_DO, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<BaseModel>(callback, new TypeToken<BaseModel>() {
        }));
    }



    public static void checkTicket(final Callback<TicketModel> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());

        /**/params.put("uid", App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());




        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_USER_CHECK_TICKET, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<TicketModel>(callback, new TypeToken<TicketModel>() {
        }));
    }


    public static void reportProblem(String content,final Callback<BaseModel> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());

        /**/params.put("uid", App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());


        params.put("content", content);



        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_USER_PROBLEM, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<BaseModel>(callback, new TypeToken<BaseModel>() {
        }));
    }



    public static void chargeList(int pageNo, String extras, final Callback<ChargeRecordModel> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());

        /**/params.put("uid", App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());

        params.put("pageNo", ""+pageNo);
        if(!"".equals(extras)){
            params.put("extras",extras);
        }




        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_USER_CHARGES_LIST, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<ChargeRecordModel>(callback, new TypeToken<ChargeRecordModel>() {
        }));
    }




    public static void updateNoOk(Feed feed, final Callback<BaseModel> callback) {



        Map<String, String> params = new HashMap<>();
        params.put("appId", Constants.APP_ID);
        params.put("v", Constants.APP_INTERFACE_VERSION);
        params.put("callId", ""+System.currentTimeMillis());
        params.put("lon", ""+App.getLongitude());
        params.put("lat", ""+App.getLatitude());
        params.put("bno", ""+App.getAppVersionCode());
        params.put("uid", ""+ App.getUserId());
        params.put("t", App.getToken());
        params.put("did",App.getDeviceId());

        params.put("feedId", ""+ feed.getId());
        params.put("feedCategory", ""+ feed.getFeedCategory());



        String str = ParamUtil.generateOrderedParamString(params,"",null);
        String sig = ParamUtil.generateSignature(str,Constants.APP_SECRET_KEY);
        params.put("sig", sig);
        String url = buildUrl(Constants.API_FEED_NO_OK, params);;
        Log.d(TAG,"url="+url);
        final Request request = new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient = OkHttp.getOkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new GsonCallbackWrapper<BaseModel>(callback, new TypeToken<BaseModel>() {
        }));
    }





}
