package com.hjt.mydouya.presenter;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjt.mydouya.entities.CommentEntity;
import com.hjt.mydouya.entities.HttpResponse;
import com.hjt.mydouya.networks.CWUrls;
import com.hjt.mydouya.networks.OkhttpBaseNetWork;
import com.hjt.mydouya.networks.ParameterKeySet;
import com.hjt.mydouya.utils.SPUtils;
import com.hjt.mydouya.views.mvpviews.MyAllCommentsActivityView;
import com.sina.weibo.sdk.constant.WBConstants;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by ougonden on 17/11/20.
 */

public class AllCommentsPresenterImpl implements AllCommentsPresenter{
    private MyAllCommentsActivityView mMyAllCommentsActivityView;
    private List<CommentEntity> mDataSet;
    private Activity mActivity;
    private Context mContext;
    private int page = 1;

    public AllCommentsPresenterImpl(Context context, Activity activity, MyAllCommentsActivityView myAllCommentsActivityView) {
        this.mActivity = activity;
        this.mDataSet = new ArrayList<CommentEntity>();
        this.mContext = context;
        this.mMyAllCommentsActivityView = myAllCommentsActivityView;
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

    private void loadData(final boolean loadMore) {
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String >();
        params.put(WBConstants.AUTH_ACCESS_TOKEN, SPUtils.getIntantce(mContext).getToken().getToken());
        params.put(ParameterKeySet.PAGE, String.valueOf(page));
        params.put(ParameterKeySet.COUNT,"15");
        new OkhttpBaseNetWork() {
            @Override
            public void onFinish(HttpResponse httpResponse, boolean success) {
                if (success) {
                    List<CommentEntity> list = new ArrayList<CommentEntity>();
                    Type type = new TypeToken<ArrayList<CommentEntity>>(){}.getType();
                    list = new Gson().fromJson(httpResponse.response,type);
                    if (!loadMore) {
                        mDataSet.clear();
                    }
                    mDataSet.addAll(list);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMyAllCommentsActivityView.onSuccess(mDataSet);
                        }
                    });
                }else {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMyAllCommentsActivityView.onError("Get AllComment error");
                        }
                    });
                }
            }
        }.setHttpGetParams(CWUrls.COMMENT_BY_ME,params).setGetRequest().doEqueue();
    }
}
