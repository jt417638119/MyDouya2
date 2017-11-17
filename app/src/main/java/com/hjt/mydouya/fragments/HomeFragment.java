package com.hjt.mydouya.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.hjt.mydouya.R;
import com.hjt.mydouya.activities.CWConstant;
import com.hjt.mydouya.adapters.HomepageListAdapter;
import com.hjt.mydouya.entities.HttpResponse;
import com.hjt.mydouya.entities.StatusEntity;
import com.hjt.mydouya.networks.BaseNetWork;
import com.hjt.mydouya.networks.CWUrls;
import com.hjt.mydouya.utils.LogUtils;
import com.hjt.mydouya.utils.SPUtils;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HJT on 2017/11/14.
 */

public class HomeFragment extends BaseFragment {
    private WeiboParameters mWeiboParameters;
    private SPUtils mSPUtils;
    private RecyclerView rlv;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.ItemDecoration mItemDecoration;
    private List<StatusEntity> mEntityList;
    private HomepageListAdapter mHomepageListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSPUtils = SPUtils.getIntantce(getActivity().getApplicationContext());
        mEntityList = new ArrayList<StatusEntity>();
        mHomepageListAdapter = new HomepageListAdapter(mEntityList,getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rlv = (RecyclerView) inflater.inflate(R.layout.v_common_recycleview, container, false);
        init();

//        View view = inflater.inflate(R.layout.fg_home,container,false);
        /*mAsyncWeiboRunner.requestAsync(url, mWeiboParameters, httpMethod, new RequestListener() {
            @Override
            public void onComplete(String s) {
                Log.e("onComplete", "onComplete() call with" + " s = [" + s + " ]");
                JsonParser parser = new JsonParser();
                JsonObject object = parser.parse(s).getAsJsonObject();
                JsonArray array = object.get("statuses").getAsJsonArray();
                List<StatusEntity> list = new ArrayList<StatusEntity>();
                // Type是代表所有类型，TypeToken是google中用于gson的类
                //　是匿名内部类
                Type type = new TypeToken<ArrayList<StatusEntity>>() {
                }.getType();
                list = new Gson().fromJson(array, type);
                Log.e("onComplet", list.size() + "");
            }

            @Override
            public void onWeiboException(WeiboException e) {

            }
        });*/
        // 网络请求
        new BaseNetWork(getActivity(), CWUrls.HOME_TIME_LINE) {
            @Override
            public WeiboParameters onPrepare() {
                mWeiboParameters = new WeiboParameters(CWConstant.APP_KEY);
                mWeiboParameters.put(WBConstants.AUTH_ACCESS_TOKEN, mSPUtils.getToken().getToken());// 第二次getToken是get Oauth2AccessToken里的mAccesToken
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
                    if (list != null && list.size() > 0) {
                        mEntityList.clear();
                        mEntityList.addAll(list);
                    }
                    // 提醒Adapter数据更新了，重新更新
                    mHomepageListAdapter.notifyDataSetChanged();
                    LogUtils.e(list.size() + "");
                }else {
                    LogUtils.e(httpResponse.message + "");
                }
            }

        }.get();

        return rlv;
    }
    // 初始化RecyclerView
    private void init() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        rlv.setLayoutManager(mLayoutManager);
        rlv.addItemDecoration( new DividerItemDecoration(getActivity(),1));
        rlv.setAdapter(mHomepageListAdapter);
        // 这是自定义的Item监听器，在HomepageListAdapter有匿名内部类
        mHomepageListAdapter.setOnItemClickListener(new HomepageListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                LogUtils.e(position+"");
            }
        });
    }
}
