package com.hjt.mydouya.networks;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hjt.mydouya.entities.HttpResponse;
import com.hjt.mydouya.utils.LogUtils;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by HJT on 2017/11/16.
 */

public abstract class OkhttpBaseNetWork {
    private static final OkHttpClient mOkHttpClient = new OkHttpClient();
    private Request mRequest;
    private String mUrl;

    public OkhttpBaseNetWork() {

    }

    public OkhttpBaseNetWork setGetRequest() {
        mRequest = new Request.Builder().url(mUrl).build();
        return this;
    }

    public void doEqueue() {
        mOkHttpClient.newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                boolean success = false;
                String resString = response.body().string(); // json类型
                HttpResponse httpResponse = new HttpResponse();
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(resString);

                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    // 错误信息的处理
                    if (jsonObject.has("error_code")) {
                        httpResponse.code = jsonObject.get("error_code").getAsInt();
                    }
                    if (jsonObject.has("error")) {
                        httpResponse.message = jsonObject.get("error").getAsString();
                    }else
                    // 正确信息的处理
                    if (jsonObject.has("statuses")) {
                        httpResponse.response = jsonObject.get("statuses").toString();
                        LogUtils.e("statuses" + httpResponse.response);
                        success = true;
                    }
                }
                onFinish(httpResponse,success);
            }

        });
    }


    /**
     * 为HttpGet的url添加多个参数
     */
    public OkhttpBaseNetWork setHttpGetParams(String url, LinkedHashMap<String, String>
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

        this.mUrl = url + stringBuffer.toString();
        LogUtils.e("urlllllllllll" + mUrl);
        return this;
    }


    public abstract void onFinish(HttpResponse httpResponse, boolean success);
}
