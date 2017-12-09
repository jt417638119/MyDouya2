package com.hjt.mydouya.networks;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hjt.mydouya.entities.HttpResponse;
import com.hjt.mydouya.utils.LogUtils;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by HJT on 2017/11/16.
 */

public abstract class OkhttpBaseNetWork {
    private static final OkHttpClient mOkHttpClient = new OkHttpClient();
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;");
    private Request mRequest;
    private String mUrl;
    private RequestBody formBody;

    public OkhttpBaseNetWork() {

    }

    public OkhttpBaseNetWork setGetRequest() {
        mRequest = new Request.Builder().url(mUrl).build();
        return this;
    }

    public OkhttpBaseNetWork setPostRequest() {
        mRequest = new Request.Builder().url(mUrl).post(formBody).build();
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
                    if (jsonObject.has("error_code")) {// 错误信息的处理
                        httpResponse.request = jsonObject.get("request").getAsString();
                        httpResponse.code = jsonObject.get("error_code").getAsInt();
                        httpResponse.message = jsonObject.get("error").getAsString();
                    }
                    else if (jsonObject.has("statuses")) { // 正确信息的处理
                        httpResponse.response = jsonObject.get("statuses").toString();
                        LogUtils.e("statuses" + httpResponse.response);
                        success = true;
                    }else if (jsonObject.has("comments")) {
                        httpResponse.response = jsonObject.get("comments").toString();
                        LogUtils.e("my comments" + httpResponse.response);
                        success = true;
                    } else { // 什么字段都没有，有一种情况是写微博后返回的数据
                        String string =  response.body().string();
                        LogUtils.e("sadasda" + resString);
                        httpResponse.response = resString;
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
        return this;
    }
    /**
     * 为HttpPost的url添加多个参数
     */
    public OkhttpBaseNetWork setHttpPostParams(String url,LinkedHashMap<String,String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : params.keySet()) {
            LogUtils.e("key ===" + key + "  values=====  " + params.get(key));
            builder.add(key, params.get(key));
        }
        formBody = builder.build();
        this.mUrl = url;
        return this;
    }

    public OkhttpBaseNetWork setHttpPostJsonParams(String url,LinkedHashMap<String,String> params) {
        StringBuffer tempParams = new StringBuffer();
        JSONObject jsonObject = new JSONObject();
        for (String key : params.keySet()) {
            try {
                jsonObject.put(key,params.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        LogUtils.e(jsonObject.toString());
        formBody = RequestBody.create(MEDIA_TYPE_JSON, jsonObject.toString());
        mUrl = url;
        return this;
    }


        public abstract void onFinish(HttpResponse httpResponse, boolean success);
}
