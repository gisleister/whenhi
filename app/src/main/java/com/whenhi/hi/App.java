package com.whenhi.hi;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.bumptech.glide.Glide;
import com.whenhi.hi.model.LoginModel;
import com.whenhi.hi.model.User;
import com.whenhi.hi.network.OkHttp;
import com.whenhi.hi.platform.PlatformConfig;
import com.whenhi.hi.util.DateUtil;
import com.whenhi.hi.util.SPUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by 王雷 on 2016/12/22.
 */

public class App extends Application {
    private static String TAG = App.class.getSimpleName();

    public static Context sContext;
    private static SPUtil spUtil;
    private static int deviceWidth;
    private static int deviceHeight;
    private LocationManager locationManager;
    private String locationProvider;
    private static Location mLocation;
    private static String deviceId = "";


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCreate() {
        super.onCreate();
        setStrictMode();
        sContext = getApplicationContext();
        spUtil = SPUtil.getInstance(sContext);
        TelephonyManager tm = (TelephonyManager) sContext.getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice = "";
        String tmSerial = "";
        String androidId = "";
        try {
            tmDevice = "" + tm.getDeviceId();
        }catch (Exception e){

        }

        try {
            tmSerial = "" + tm.getSimSerialNumber();

        }catch (Exception e){

        }

        try {
            androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        }catch (Exception e){

        }

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        deviceId = deviceUuid.toString();


        if(TextUtils.isEmpty(App.getJPushRegisterID())){
            JPushInterface.init(getApplicationContext());
        }


        DisplayMetrics metric = new DisplayMetrics();
        WindowManager mWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay().getRealMetrics(metric);

        deviceWidth = metric.widthPixels;
        deviceHeight = metric.heightPixels;

        JPushInterface.setDebugMode(true);
        JPushInterface.init(getApplicationContext());

        OkHttp.init(sContext);


        PlatformConfig.getInstance()
                .initWeibo("369866112", "1916860d15cce2f7618d117402d2a060", Constants.REDIRECT_URL, Constants.SCOPE)
                .initWechat("wx07efc5e48700eb49", "0bcab6744823f0d365abcc6b78358360", "snsapi_userinfo", "1245")
                .initQQ("1105930594", "re8QnkQX5hONzBw0");

        initGPS();
    }

    public static Context getContext() {
        return sContext;
    }


    private void initGPS() {
        //获取地理位置管理器
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);

        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocation = locationManager.getLastKnownLocation(locationProvider);
        //监视地理位置变化
        locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);

    }

    public static double getLongitude(){

        if(mLocation != null){
            return mLocation.getLongitude();
        }else{
            return 0.0;
        }

    }

    public static double getLatitude(){

        if(mLocation != null){
            return mLocation.getLatitude();
        }else{
            return 0.0;
        }

    }

    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */

    LocationListener locationListener =  new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            mLocation = location;

            if(locationManager!=null){
                //移除监听器
                locationManager.removeUpdates(locationListener);
            }

        }
    };



    private void setStrictMode() {
        if (com.aspsine.swipetoloadlayout.BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.enableDefaults();
        }
    }

    public static void firstLogin(boolean first){
        spUtil.putBoolean("first",first);
    }

    public  static Boolean isFirstLogin(){
        return spUtil.getBoolean("first",false);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void getScreenSizeOfDevice2() {
        Point point = new Point();
        WindowManager mWindowManager  = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay().getRealSize(point);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        double x = Math.pow(point.x/ dm.xdpi, 2);
        double y = Math.pow(point.y / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        Log.d(TAG, "Screen inches : " + screenInches);
    }

    public static boolean isLogin(){
        boolean isLogin = spUtil.getBoolean("isLogin",false);
        return isLogin;
    }

    public static void loginSucees(LoginModel loginModel){
        spUtil.putBoolean("isLogin", true);
        spUtil.putString("userId",""+loginModel.getData().getUserId());
        spUtil.putString("token",loginModel.getData().getToken());
    }

    public static void userGender(String gender){
        spUtil.putString("gender",gender);
    }

    public static String getGender(){
        String gender = spUtil.getString("gender","");
        return gender;
    }

    public static void loginout(){
        spUtil.putBoolean("isLogin", false);
        spUtil.putString("userId","0");
        spUtil.putString("token",null);
        spUtil.putString("gender","");
    }

    public static void userInit(User user){
        spUtil.putString("invitePageUrl",user.getInvitePageUrl());
        spUtil.putString("userLogo",user.getLogo());
        spUtil.putString("nickName",user.getName());

    }

    public static String getInvitePageUrl(){
        String invitePageUrl = spUtil.getString("invitePageUrl","http://www.whenhi.cn/invite/index.html?code=100004");
        return invitePageUrl;
    }

    public static String getUserLogo(){
        String userLogo = spUtil.getString("userLogo",null);
        return userLogo;
    }

    public static String getNickname(){
        String nickName = spUtil.getString("nickName",null);
        return nickName;
    }




    public static void deviceInit(){
        spUtil.putBoolean("isDeviceInit", true);

    }

    public static void setStatusMainActivity(boolean status){
        spUtil.putBoolean("mainActivity", status);

    }

    public static String getLastBindJPushTime(){
        String bindTime = spUtil.getString("bindTime","");
        return bindTime;
    }

    public static void setLastBindJPushTime(String time){
        spUtil.putString("bindTime",time);
    }

    public static boolean getStatusMainActivity(){
        return spUtil.getBoolean("mainActivity",false);
    }

    public static boolean isDeviceInit(){
        boolean isDeviceInit = spUtil.getBoolean("isDeviceInit",false);
        return isDeviceInit;
    }


    public static String getUserId(){
        String userId = spUtil.getString("userId","");
        return userId;
    }

    public static void setExtras(String key,String value){
        spUtil.putString(key,value);
    }
    public static String getExtras(String key){
        String value = spUtil.getString(key,"");
        return value;
    }

    public static String getToken(){
        String token = spUtil.getString("token","");
        return token;
    }

    public static void setOnlyLoginTime(String time){
        spUtil.putString("logintime", time);
    }

    public static String getOnlyLoginTime(){
        String timelogin = spUtil.getString("logintime","2010-11-20");
        return timelogin;
    }
    public static boolean isTodayFirst(){//此方法只能在app调用一次，需要定义全局变量或者统一调用


        boolean res = false;
        try {
            boolean isToday = DateUtil.isToday(getOnlyLoginTime());
            if(isToday){
                res = false;
            }else{
                res = true;
                long now = System.currentTimeMillis();
                String nowStr = DateUtil.getNowTimeString(now);
                setOnlyLoginTime(nowStr);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return res;
    }

    /**
     * 获取application中指定的meta-data
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getChannel(String key) {
        String resultData = "";
        try {
            PackageManager packageManager = sContext.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(sContext.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                        if(resultData == null){
                            resultData = "";
                        }
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }

    /**
     * 获得app的打包时间
     *
     * @return
     */
    public static String getBuildTime() {
        String result = "";
        try {
            ApplicationInfo ai = sContext.getPackageManager().getApplicationInfo(sContext.getPackageName(),0);
            ZipFile zf = new ZipFile(ai.sourceDir);
            ZipEntry ze = zf.getEntry("META-INF/MANIFEST.MF");
            long time = ze.getTime();
            SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getInstance();
            formatter.applyPattern("yy/MM/dd");
            result = formatter.format(new java.util.Date(time));
            zf.close();
        } catch (Exception e) {
        }

        return result;
    }

    public static String getJPushRegisterID(){
        String registerID = JPushInterface.getRegistrationID(sContext);
        Log.d(TAG,"registerID:"+registerID);
        return registerID;
    }

    public static String  getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = sContext.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(getAppPkg(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName =
                (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

    public static String getAppVersionName() {
        try {
            String pkName = sContext.getPackageName();
            String versionName = sContext.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            Log.d(TAG,"versionName:"+versionName);
            return versionName ;
        } catch (Exception e) {
        }
        return "";
    }

    public static String getAppPkg() {
        try {
            String pkName = sContext.getPackageName();
            Log.d(TAG,"pkName:"+pkName);
            return pkName;
        } catch (Exception e) {
        }
        return "";
    }

    public static String getAppVersionCode() {
        try {
            String pkName = sContext.getPackageName();

            int versionCode = sContext.getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;
            Log.d(TAG,"versionCode:"+versionCode);
            return  ""+versionCode;
        } catch (Exception e) {
        }
        return "";
    }



    /**
     * 获取手机品牌
     *
     * @return
     */
    public static String getPhoneBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机Android 版本（4.4、5.0、5.1 ...）
     *
     * @return
     */
    public static String getBuildVersion() {
        return android.os.Build.VERSION.RELEASE;
    }
    /**
     * 获取设备宽度（px）
     * @return
     */
    public static int deviceWidth() {
        return deviceWidth;
    }
    /**
     * 获取设备高度（px）
     * @return
     */
    public static int deviceHeight() {
        return deviceHeight;
    }

    public static String getIMEI(){

        return deviceId;


    }

    /**
     * 获取设备的唯一标识，deviceId
     *
     * @return
     */
    public static String getDeviceId() {

        return deviceId;


    }

    public static String getUserAgent(){
        WebView webview;
        webview = new WebView(sContext);
        webview.layout(0, 0, 0, 0);
        WebSettings settings = webview.getSettings();
        String ua = settings.getUserAgentString();
        return ua;
    }

    public static String getMac()
    {
        String macSerial = "";
        String str = "";

        try
        {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str;)
            {
                str = input.readLine();
                if (str != null)
                {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }


    public static void clearGlideMemoryCache(Context context){
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
