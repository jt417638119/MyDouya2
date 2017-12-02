package com.hjt.mydouya.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.hjt.mydouya.R;
import com.hjt.mydouya.entities.UserEntity;
import com.hjt.mydouya.model.UserModel;
import com.hjt.mydouya.model.UserModelImpl;
import com.hjt.mydouya.utils.LogUtils;
import com.hjt.mydouya.utils.SPUtils;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * Created by HJT on 2017/11/15.
 */

public class LandingPageActivity extends BaseActivity {
    private SsoHandler mSsoHandler;
    private AuthInfo mAuthInfo;
    private SPUtils mSPUtils;
    private UserEntity mUserEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolBar().hide();

        mSPUtils = SPUtils.getIntantce(getApplicationContext());
        mAuthInfo = new AuthInfo(getApplicationContext(), CWConstant.APP_KEY, CWConstant.REDIRECT_URL, CWConstant.SCOPE);
        mSsoHandler = new SsoHandler(this, mAuthInfo);
        mUserEntity = new UserEntity();

        new Handler().postDelayed(new Runnable() { // 半秒钟后执行，检查是否登录
            @Override
            public void run() {
                checkLogin();
            }
        }, 500);

    }


    private void checkLogin() {
        if (mSPUtils.isLogin()) { // 如果已经在缓存中有token则已经是登录的了
            new Handler().postDelayed(new Runnable() { // 半秒钟后执行
                @Override
                public void run() {
                    startActivity(new Intent(LandingPageActivity.this, HomePageActivity.class));
                    finish();
                }
            }, 500);
        }else { // 没登陆过
            mSsoHandler.authorize(new WeiboAuthListener() {
                @Override
                public void onComplete(Bundle bundle) {// 登录成功
                    Log.e("oncreate111", bundle + "");
                    // 将Bundle解析成AccesToken
                    Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(bundle);
                    // 保存Token
                    mSPUtils.saveToken(accessToken);
                    // 保存用户基本信息
//                    UserModelImpl userModel = new UserModelImpl(getApplicationContext());
//                    userModel.saveLocalUser();
                    startActivity(new Intent(LandingPageActivity.this, HomePageActivity.class));
                    finish();
                }

                @Override
                public void onWeiboException(WeiboException e) {
                    Log.e("onCancel", "onCancel");
                }

                @Override
                public void onCancel() {
                    Log.d("onCancel", "onCancel");
                }
            });
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_landing;
    }

    // 当子Activity回调到父Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

}
