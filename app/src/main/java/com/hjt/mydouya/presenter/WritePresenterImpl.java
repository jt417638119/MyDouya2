package com.hjt.mydouya.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.hjt.mydouya.activities.CWConstant;
import com.hjt.mydouya.entities.HttpResponse;
import com.hjt.mydouya.networks.CWUrls;
import com.hjt.mydouya.networks.OkHttpUtil;
import com.hjt.mydouya.networks.OkhttpBaseNetWork;
import com.hjt.mydouya.networks.ParameterKeySet;
import com.hjt.mydouya.utils.LogUtils;
import com.hjt.mydouya.utils.SPUtils;
import com.hjt.mydouya.views.mvpviews.WriteView;
import com.sina.weibo.sdk.constant.WBConstants;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ougonden on 17/11/20.
 */

public class WritePresenterImpl implements WritePresenter{
    private WriteView mWriteView;
    private Activity mActivity;
    private Context mContext;
    private String s;

    public WritePresenterImpl(Context context, WriteView writeView, Activity activity) {
        this.mActivity = activity;
        this.mWriteView = writeView;
        this.mContext = context;
    }

    @Override
    public void writeArticle(String content) {
        String mContent = null; // 需加安全域名才能发送
//        try {
//            mContent = URLEncoder.encode(content + CWConstant.SECURITY_URL,"utf-8");
//            LogUtils.e("content ==" + mContent);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        mContent= content + CWConstant.SECURITY_URL;



        LinkedHashMap<String, String> params = new LinkedHashMap<String,String>();
        params.put(ParameterKeySet.AUTH_ACCESS_TOKEN, SPUtils.getIntantce(mContext).getToken().getToken());
        params.put(ParameterKeySet.STATUS, mContent);

        new OkhttpBaseNetWork() { // 需要传递json的参数
            @Override
            public void onFinish(HttpResponse httpResponse, boolean success) {
                if (success) {
                    LogUtils.e("write +++++ " + httpResponse.response);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mWriteView.onSuccess();
                        }
                    });

                }else {
                    LogUtils.e("write error" + httpResponse.message + httpResponse.code + "  url  " + httpResponse.request);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mWriteView.onError("error");
                        }
                    });
                }
            }
        }.setHttpPostParams(CWUrls.SHARE,params).setPostRequest().doEqueue();
    }


}
