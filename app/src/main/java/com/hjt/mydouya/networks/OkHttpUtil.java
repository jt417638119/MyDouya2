package com.hjt.mydouya.networks;

import com.facebook.stetho.common.Utf8Charset;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ougonden on 17/12/1.
 */

public class OkHttpUtil {
    private static final OkHttpClient mOkhttpCilent = new OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).writeTimeout
                    (10, TimeUnit.SECONDS).build();

    /**
     * 不会开启异步线程，并返回结果
     */
    public static Response execute(Request request) throws IOException {

        return mOkhttpCilent.newCall(request).execute();
    }

    /**
     * 开启异步线程
     */
    public static void doEnqueue(Request request, Callback responseCallback) {
        mOkhttpCilent.newCall(request).enqueue(responseCallback);
    }

    /**
     * 为HttpGet的url添加多个参数
     */
    public static String attachHttpGetParams(String url, LinkedHashMap<String, String>
            linkedHashMap) {
        Iterator<String> keys = linkedHashMap.keySet().iterator();
        Iterator<String> values = linkedHashMap.values().iterator();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("?");


        for (int i = 0; i < linkedHashMap.size(); i++) {
            String value = null;
//            try {
//                value = URLEncoder.encode(values.next(), "utf-8");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            stringBuffer.append(keys.next() + "=" + values.next());
            if (i != linkedHashMap.size() - 1) {
                stringBuffer.append("&");
            }
        }

        return url + stringBuffer.toString();
    }
}
