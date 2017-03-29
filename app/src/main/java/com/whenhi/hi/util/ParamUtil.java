package com.whenhi.hi.util;


import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 王雷 on 2017/1/6.
 */

public class ParamUtil {
    public final static String generateOrderedParamString(Map<String, String> paramMap, String sep, String valueCharset) {
        Set<String> params = paramMap.keySet();
        List<String> sortedParams = new ArrayList<String>(params);
        Collections.sort(sortedParams);
        StringBuilder sb = new StringBuilder();
        boolean needEncodeValue = valueCharset != null;
        for (String paramKey : sortedParams) {
            // 参数值不限制字符长度
            String value = paramMap.get(paramKey);
            if(needEncodeValue) {
                value = DataUtils.encode(value, valueCharset);
            }
            sb.append(paramKey).append('=').append(value);
            sb.append(sep);
        }
        int len = sb.length();
        if(len > 0) {
            int sepLen = sep.length();
            sb.delete(len-sepLen, len);
        }
        return sb.toString();
    }

    public final static String generateSignature(String normalizedString, String secretKey) {
        String res = normalizedString+secretKey;
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(res.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }

    private static String getString(byte[] b){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < b.length; i ++){
            sb.append(b[i]);
        }
        return sb.toString();
    }
}
