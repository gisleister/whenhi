package com.whenhi.hi.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by 王雷 on 2017/1/6.
 */

public class DataUtils {
    public static String encode(String oriStr, String charset) {
        try {
            return URLEncoder.encode(oriStr, charset).replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;

    }
}
