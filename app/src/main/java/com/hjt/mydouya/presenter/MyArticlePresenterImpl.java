package com.hjt.mydouya.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjt.mydouya.activities.CWConstant;
import com.hjt.mydouya.entities.HttpResponse;
import com.hjt.mydouya.entities.StatusEntity;
import com.hjt.mydouya.networks.CWUrls;
import com.hjt.mydouya.networks.OkHttpUtil;
import com.hjt.mydouya.networks.OkhttpBaseNetWork;
import com.hjt.mydouya.networks.ParameterKeySet;
import com.hjt.mydouya.utils.LogUtils;
import com.hjt.mydouya.utils.SPUtils;
import com.hjt.mydouya.views.mvpviews.MyArticleActivityView;
import com.sina.weibo.sdk.constant.WBConstants;

import java.lang.reflect.Type;
import java.security.AccessControlContext;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Request;

/**
 * Created by ougonden on 17/11/20.
 */

public class MyArticlePresenterImpl implements MyArticlePresenter{
    private int page = 1;
    private static final int UPDATE_RECECLE = 1;
    private Context mContext;
    private MyArticleActivityView mMyArticleActivityView;
    private List<StatusEntity> mList;
    private Handler mHandler;
    private Activity mActivity;

    public MyArticlePresenterImpl(Context context,MyArticleActivityView myArticleActivityView, Activity activity) {
        this.mContext = context;
        this.mList = new ArrayList<StatusEntity>();
        this.mActivity = activity;
        this.mMyArticleActivityView = myArticleActivityView;
    }
    @Override
    public void loadData() {
        page = 1;
        loadData(false);
    }


    @Override
    public void loadMore() {
        page++;
        loadData(true);
    }

    @Override
    public void requestHomeTimeLine() {

    }

    @Override
    public void requestUserTimeLine() {

    }

    private void loadData(final boolean loadMore) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String,String>();
        map.put(WBConstants.AUTH_ACCESS_TOKEN, SPUtils.getIntantce(mContext).getToken().getToken());
        map.put(ParameterKeySet.PAGE, String.valueOf(page));
        map.put(ParameterKeySet.COUNT,"15");
        new OkhttpBaseNetWork() {
            @Override
            public void onFinish(final HttpResponse httpResponse, boolean success) {
                if (success) {
                    LogUtils.e("onFinish" + httpResponse.response);
                    Type type = new TypeToken<ArrayList<StatusEntity>>(){}.getType();
                    ArrayList<StatusEntity> mDataSet = new Gson().fromJson(httpResponse.response, type);
                    if (!loadMore) {
                        mList.clear();
                    }

                    mList.addAll(mDataSet);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMyArticleActivityView.onSuccess(mList);
                        }
                    });
                }else  {
                    LogUtils.e("error" + httpResponse.message);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMyArticleActivityView.onError(httpResponse.message);
                        }
                    });
                }
            }
        }.setHttpGetParams(CWUrls.USER_TIME_LINE,map).setGetRequest().doEqueue();
    }
}
