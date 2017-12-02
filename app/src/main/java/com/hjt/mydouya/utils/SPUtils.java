package com.hjt.mydouya.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.hjt.mydouya.entities.UserEntity;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * Created by HJT on 2017/11/15.
 */

public class SPUtils {
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor mEditor;
    private static SPUtils instance;
    private static final String SP_NAME = "WEIBO";
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String IS_LOGIN = "IS_LOGIN";
    private static final String USER = "USER";
    private static final String UID = "UID";

    private SPUtils() {}

    public static SPUtils getIntantce(Context context) {
        if (instance == null) {
            //修饰一个代码块：
            //一个线程访问一个对象中的synchronized(this)同步代码块时，其他试图访问该对象的线程将被阻塞。
            synchronized (SPUtils.class) {
                instance = new SPUtils();
                mSharedPreferences = context.getSharedPreferences(SP_NAME, Activity.MODE_PRIVATE);
                mEditor = mSharedPreferences.edit();
            }
        }
        return instance;
    }

    public void saveToken(Oauth2AccessToken accessToken) {
        mEditor.putString(ACCESS_TOKEN,new Gson().toJson(accessToken)).commit();
        mEditor.putBoolean(IS_LOGIN,true).commit();
    }

    public void saveUser(UserEntity userEntity) {
        mEditor.putString(USER, new Gson().toJson(userEntity)).commit();
    }

    public void saveText(String string) {
        mEditor.putString("TEXT",string).commit();
    }
    public String getText() {
        String json = mSharedPreferences.getString("TEXT","");
        if (json == null) {
            return null;
        }
        return json;
    }


    public Oauth2AccessToken getToken() {
        String json = mSharedPreferences.getString(ACCESS_TOKEN,"");
        if (json == null) {
            return null;
        }
        return new Gson().fromJson(json,Oauth2AccessToken.class);
    }

    public UserEntity getUser() {
        String json = mSharedPreferences.getString(USER, "");
        if (json == null) {
            return null;
        }
        return new Gson().fromJson(json, UserEntity.class);
    }


    public boolean isLogin() {
        return mSharedPreferences.getBoolean(IS_LOGIN,false);
    }

    public void clear() {
        mEditor.clear();
        mEditor.commit();
    }

}
