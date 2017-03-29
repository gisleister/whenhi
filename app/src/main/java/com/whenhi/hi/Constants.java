package com.whenhi.hi;

/**
 * Created by Aspsine on 2015/9/6.
 */
public class Constants {


 public static final String URL_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
 public static final String URL_WECHAT_USER = "https://api.weixin.qq.com/sns/userinfo";
 /** weibo api. */
 public static final String URL_GET_USER_INFO = "https://api.weibo.com/2/users/show.json";
 public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
 public static final String SCOPE =
         "email,direct_messages_read,direct_messages_write,"
                 + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                 + "follow_app_official_microblog," + "invitation_write";


 public static  final boolean DEBUG = true;//此处在发版本时候必须修改为false

 public static final String host = "http://api.whenhi.cn/";
 //public static final String host = "http://admin2.kanglejiating.com/";


 public static final String API_FEED_LIST_URL = host+"api/feed/list/";
 public static final String API_COMMENT_LIST_URL = host+"api/comment/list";
 public static final String API_USER_UPDATE_URL = host+"api/user/login";
 public static final String API_USER_SHARE_URL = host+"api/feed/share";
 public static final String API_USER_CLICK_URL = host+"api/feed/click";
 public static final String API_SYS_MESSAGE_URL = host+"api/msg/notify/list";
 public static final String API_SYS_PUSH_BIND_URL = host+"api/push/bind";
 public static final String API_DEVICE_INIT = host+"api/app/device/init";
 public static final String API_UPDATE_APP = host+"api/app/version/check";
 public static final String API_USER_DETAIL = host+"api/user/detail";
 public static final String API_USER_SCORE_RECORD = host+"api/user/score/records";
 public static final String API_USER_CHECK_IN = host+"api/user/checkin";
 public static final String API_NORES_REPORT = host+"api/feed/nores/report";
 public static final String API_USER_LOVE_URL = host+"api/feed/like";
 public static final String API_USER_DIS_LOVE_URL = host+"api/feed/dislike";
 public static final String API_USER_FAV_URL = host+"api/feed/favorite/add";
 public static final String API_USER_DIS_FAV_URL = host+"api/feed/favorite/remove";
 public static final String API_COMMENT_ADD = host+"api/comment/add";
 public static final String API_DISCOVERY_HOME = host+"api/discovery/home";
 public static final String API_DISCOVERY_PAST = host+"api/discovery/past";
 public static final String API_USER_INVITECODE_INPUT = host+"api/user/invitecode/input";
 public static final String API_USER_CHARGES_ITEMS = host+"api/user/charge/items";
 public static final String API_DISCOVERY_SHARE = host+"api/discovery/top/share";
 public static final String API_DISCOVERY_LOVE = host+"api/discovery/top/hot";
 public static final String API_DISCOVERY_USERS = host+"api/discovery/top/users";
 public static final String API_REG_SENDCODE = host+"api/user/mobile/valid";
 public static final String API_FEED_FAV_LIST_URL = host+"api/feed/list/favorite";
 public static final String API_REG_SMS_CODE = host+"api/user/mobile/update";
 public static final String API_USER_CHARGES_DO = host+"api/user/charge/docharge";
 public static final String API_USER_CHECK_TICKET = host+"api/user/checkTicket";
 public static final String API_USER_PROBLEM = host+"api/app/feedback";
 public static final String API_USER_CHARGES_LIST = host+"api/user/charge/list";
 public static final String API_FEED_NO_OK = host+"api/feed/notOK";


 /***以下是导航页面跳转常量**/
 public static final int NAV_BOTTOM_BAR_HOME = 0;
 public static final int NAV_BOTTOM_BAR_EXPLORE = 1;
 public static final int NAV_BOTTOM_BAR_MESSAGE = 2;
 public static final int NAV_BOTTOM_BAR_MORE = 3;

 /***以下是详情页面跳转常量**/

 public static final int DETAIL_WEBVIEW = 0;
 public static final int DETAIL_PIC = 1;
 public static final int DETAIL_VIDEO = 2;
 public static final int DETAIL_TEXT = 3;

 public static final int OTHER_FAV = 0;
 public static final int OTHER_INCOME_RECORD = 1;
 public static final int OTHER_CRAFT = 2;
 public static final int OTHER_PHONE = 3;
 public static final int OTHER_LOVE_INDEX = 4;
 public static final int OTHER_SHARE_INDEX = 5;
 public static final int OTHER_INCOME_INDEX = 6;
 public static final int OTHER_LOVE_INDEX_SUB = 7;
 public static final int OTHER_SHARE_INDEX_SUB = 8;
 public static final int OTHER_INCOME_INDEX_SUB = 9;
 public static final int OTHER_MESSAGE = 10;
 public static final int OTHER_CHARGE_RECORD = 11;



 public static final String APP_ID = "3";
 public static final String APP_INTERFACE_VERSION = "1.0";
 public static final String APP_SECRET_KEY = "173559e25f8433dea2ca36babb331be7";
}
