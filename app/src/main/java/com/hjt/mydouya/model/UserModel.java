package com.hjt.mydouya.model;

import android.content.Context;
import android.os.Handler;

import com.hjt.mydouya.entities.UserEntity;
import com.hjt.mydouya.views.mvpviews.HomePageView;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * Created by ougonden on 17/11/30.
 */

public interface UserModel {
    public String getUid();
    public UserEntity getUser();
    public void saveLocalUser(Handler handler);
}
