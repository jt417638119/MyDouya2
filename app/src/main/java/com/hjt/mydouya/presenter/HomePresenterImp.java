package com.hjt.mydouya.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjt.mydouya.activities.CWConstant;
import com.hjt.mydouya.entities.HttpResponse;
import com.hjt.mydouya.entities.StatusEntity;
import com.hjt.mydouya.networks.BaseNetWork;
import com.hjt.mydouya.networks.CWUrls;
import com.hjt.mydouya.networks.ParameterKeySet;
import com.hjt.mydouya.utils.LogUtils;
import com.hjt.mydouya.utils.SPUtils;
import com.hjt.mydouya.views.mvpviews.HomeView;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.net.WeiboParameters;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ougonden on 17/11/20.
 */

public class HomePresenterImp implements HomePresenter {
    private String url = CWUrls.HOME_TIME_LINE;
    private int page = 1;
    private WeiboParameters mWeiboParameters;
    private SPUtils mSPUtils;
    private List<StatusEntity> mEntityList;
    private HomeView mHomeView;

    public HomePresenterImp(HomeView homeView) {
        this.mHomeView = homeView;
        this.mEntityList = new ArrayList<>();
        this.mWeiboParameters = new WeiboParameters(CWConstant.APP_KEY);
        this.mSPUtils = SPUtils.getIntantce(mHomeView.getActivity());
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
        url = CWUrls.HOME_TIME_LINE;
        loadData();
    }

    @Override
    public void requestUserTimeLine() {
        url = CWUrls.USER_TIME_LINE;
        loadData();
    }

    private void loadData(final boolean loadMore) {
        // 网络请求
        new BaseNetWork(mHomeView.getActivity(), CWUrls.HOME_TIME_LINE) {
            @Override
            public WeiboParameters onPrepare() {
                mWeiboParameters.put(WBConstants.AUTH_ACCESS_TOKEN, mSPUtils.getToken().getToken());// 第二次getToken是get Oauth2AccessToken里的mAccesToken
                // 加了页码后，第一页下拉更新可以更新最新的东西。
                mWeiboParameters.put(ParameterKeySet.PAGE, page);
                mWeiboParameters.put(ParameterKeySet.COUNT, 3);
                return mWeiboParameters;
            }

            @Override
            public void onFinish(HttpResponse httpResponse, boolean success) {
                if (success) { // url已经固定，所以只有一种成功的情况
                    LogUtils.e(httpResponse.response);
                    List<StatusEntity> list = new ArrayList<StatusEntity>();
                    // Type是代表所有类型，TypeToken是google中用于gson的类
                    //　是匿名内部类
                    Type type = new TypeToken<ArrayList<StatusEntity>>() {
                    }.getType();
                    list = new Gson().fromJson(httpResponse.response, type);
                    // null表示没有指向任何对象，size=0说明有指向集合元素为0的对象
                    /*if (list != null && list.size() > 0) {
                        mEntityList.clear();
                        mEntityList.addAll(list);
                    }*/
                    if (!loadMore) {
                        // 不clear掉会一直加载重复的东西
                        mEntityList.clear();
                    }
                    // 提醒Adapter数据更新了，重新更新
//                    mHomepageListAdapter.notifyDataSetChanged();
//                    LogUtils.e(list.size() + "");
                    mEntityList.addAll(list);
                    mHomeView.onSuccess(mEntityList);
                }else {
                    LogUtils.e(httpResponse.message + "");
                    mHomeView.onError(httpResponse.message);
                }
            }

        }.get();
    }

}
