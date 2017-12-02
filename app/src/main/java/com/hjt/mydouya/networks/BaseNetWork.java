package com.hjt.mydouya.networks;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hjt.mydouya.activities.CWConstant;
import com.hjt.mydouya.entities.HttpResponse;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;

/**
 * Created by HJT on 2017/11/16.
 */

public abstract class BaseNetWork {
    private AsyncWeiboRunner mAsyncWeiboRunner;
    private String url;

    public BaseNetWork(Context context, String url) {
        mAsyncWeiboRunner = new AsyncWeiboRunner(context);
        this.url = url;
    }

    private RequestListener mRequestListener = new RequestListener() {
        @Override
        public void onComplete(String s) {
            /* {
            "request" : "/statuses/home_timeline.json",
                "error_code" : "20502",
                "error" : "Need you follow uid."
        }*/
            boolean success = false;
            HttpResponse httpResponse = new HttpResponse();
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(s);

            if (element.isJsonObject()) {
                JsonObject object = element.getAsJsonObject();
                // 错误信息的处理
                if (object.has("error_code")) {
                    httpResponse.code = object.get("error_code").getAsInt();
                }
                if (object.has("error")) {
                    httpResponse.message = object.get("error").getAsString();
                }
                // 正确信息的处理
                if (object.has("statuses")) {
                    httpResponse.response = object.get("statuses").toString();
                    success = true;
                } else if (object.has("users")) {
                    httpResponse.response = object.get("users").toString();
                    success = true;
                } else if (object.has("comments")) {
                    httpResponse.response = object.get("comments").toString();
                    success = true;
                } else if (object.has("uid")) {
                    httpResponse.response = object.get("uid").toString();
                    success = true;
                }
                else { // 什么字段都没有
                    httpResponse.response = s;
                    success = true;
                }
            }

            onFinish(httpResponse, success);
        }

        @Override
        public void onWeiboException(WeiboException e) {
            HttpResponse httpResponse = new HttpResponse();
            httpResponse.message = e.getMessage();
            onFinish(httpResponse, false);
        }
    };

    public void get() {
        mAsyncWeiboRunner.requestAsync(url, onPrepare(), "GET", mRequestListener);
    }

    public void post() {
        mAsyncWeiboRunner.requestAsync(url, onPrepare(), "POST", mRequestListener);
    }

    public void delete() {
        mAsyncWeiboRunner.requestAsync(url, onPrepare(), "DELETE", mRequestListener);
    }

    public abstract WeiboParameters onPrepare();

    public abstract void onFinish(HttpResponse httpResponse, boolean success);
}
