package com.hjt.mydouya.model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjt.mydouya.activities.CWConstant;
import com.hjt.mydouya.entities.HttpResponse;
import com.hjt.mydouya.entities.UidEntity;
import com.hjt.mydouya.entities.UserEntity;
import com.hjt.mydouya.networks.BaseNetWork;
import com.hjt.mydouya.networks.CWUrls;
import com.hjt.mydouya.networks.OkHttpUtil;
import com.hjt.mydouya.networks.ParameterKeySet;
import com.hjt.mydouya.utils.LogUtils;
import com.hjt.mydouya.utils.SPUtils;
import com.hjt.mydouya.views.mvpviews.HomePageView;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.net.WeiboParameters;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ougonden on 17/11/30.
 */

public class UserModelImpl implements UserModel {
    private static final int UPDATE_UI = 1;
    private Context mContext;
    private UserEntity mUserEntity;
    public String mUid;
    private Handler mHandler;

    public UserModelImpl(Context context) {
        this.mContext = context;
        this.mUserEntity = new UserEntity();
        mUid = new String();
    }

    @Override
    public String getUid() {
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        params.put(WBConstants.AUTH_ACCESS_TOKEN, SPUtils.getIntantce(mContext).getToken()
                .getToken());
        String urlGetUid = OkHttpUtil.attachHttpGetParams(CWUrls.GET_UID, params);
        LogUtils.e("url " + urlGetUid);
        Request request = new Request.Builder().url(urlGetUid).build();
        // 异步通信
        OkHttpUtil.doEnqueue(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mUid = response.body().string();
                LogUtils.e("errororororororo" + mUid);
                SPUtils.getIntantce(mContext).saveText("hahahahah");
                LogUtils.e("sucessssss" + SPUtils.getIntantce(mContext).getText());
            }
        });
        LogUtils.e("sucessssss" + SPUtils.getIntantce(mContext).getText());

        return mUid;
    }

    @Override
    public UserEntity getUser() {
        getUid();
        // 请求参数
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put(WBConstants.AUTH_ACCESS_TOKEN, SPUtils.getIntantce(mContext).getToken()
                .getToken());
        linkedHashMap.put(WBConstants.GAME_PARAMS_UID, mUid);
        String urlGetUser = OkHttpUtil.attachHttpGetParams(CWUrls.GET_USER, linkedHashMap);
        Request request = new Request.Builder().url(urlGetUser).build();
        LogUtils.e("sucessssss" + SPUtils.getIntantce(mContext).getText());

        return mUserEntity;
    }


    @Override
    public void saveLocalUser(Handler handler){
        mHandler = handler;
        // 先获取Uid
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        params.put(WBConstants.AUTH_ACCESS_TOKEN, SPUtils.getIntantce(mContext).getToken()
                .getToken());
        String urlGetUid = OkHttpUtil.attachHttpGetParams(CWUrls.GET_UID, params);
        LogUtils.e("url " + urlGetUid);
        Request request = new Request.Builder().url(urlGetUid).build();
        // 获取uid
        OkHttpUtil.doEnqueue(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                UidEntity uidEntity = new Gson().fromJson(response.body().string(), UidEntity.class);
                // 获取完了uid
                mUid = uidEntity.uid;
                // 根据id保存user
                saveUserByUid(mUid);
            }
        });
    }


    public void saveUserByUid(String id) {
        // 请求参数
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put(WBConstants.AUTH_ACCESS_TOKEN, SPUtils.getIntantce(mContext).getToken()
                .getToken());
        linkedHashMap.put(WBConstants.GAME_PARAMS_UID, id);
        String urlGetUser = OkHttpUtil.attachHttpGetParams(CWUrls.GET_USER, linkedHashMap);
        LogUtils.e("urlUser  "+ urlGetUser);
        Request request = new Request.Builder().url(urlGetUser).build();
        OkHttpUtil.doEnqueue(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e("fail");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mUserEntity = new Gson().fromJson(response.body().string(), UserEntity.class);
                SPUtils.getIntantce(mContext).saveUser(mUserEntity);
                Message message = new Message();
                message.what = UPDATE_UI;
                // 更新UI
                mHandler.sendMessage(message);
            }
        });
    }
}
