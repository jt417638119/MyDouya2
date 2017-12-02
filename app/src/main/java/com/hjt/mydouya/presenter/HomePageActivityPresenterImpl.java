package com.hjt.mydouya.presenter;

import android.content.Context;
import android.os.Handler;

import com.hjt.mydouya.model.UserModel;
import com.hjt.mydouya.model.UserModelImpl;
import com.hjt.mydouya.views.mvpviews.HomePageView;

/**
 * Created by ougonden on 17/12/1.
 */

public class HomePageActivityPresenterImpl implements HomePageActivityPresenter{
    private HomePageView mHomePageView;
    private Context mContext;
    private UserModel mUserModel;
    private Handler mHandler;

    public HomePageActivityPresenterImpl(Context context, HomePageView homePageView, Handler handler) {
        this.mContext = context;
        this.mHomePageView = homePageView;
        this.mUserModel = new UserModelImpl(mContext);
        this.mHandler = handler;
    }

    @Override
    public void saveLocalUser() {
        // 先保存当地User并且显示更新UI
        mUserModel.saveLocalUser(mHandler);

    }
}
