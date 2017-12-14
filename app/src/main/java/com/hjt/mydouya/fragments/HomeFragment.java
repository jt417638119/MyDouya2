package com.hjt.mydouya.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.hjt.mydouya.R;
import com.hjt.mydouya.activities.ArticleActivity;
import com.hjt.mydouya.activities.ArticleCommentActivity;
import com.hjt.mydouya.activities.CWConstant;
import com.hjt.mydouya.adapters.HomepageListAdapter;
import com.hjt.mydouya.entities.HttpResponse;
import com.hjt.mydouya.entities.StatusEntity;
import com.hjt.mydouya.networks.BaseNetWork;
import com.hjt.mydouya.networks.CWUrls;
import com.hjt.mydouya.presenter.HomePresenter;
import com.hjt.mydouya.presenter.HomePresenterImp;
import com.hjt.mydouya.utils.LogUtils;
import com.hjt.mydouya.utils.SPUtils;
import com.hjt.mydouya.views.PullToRefreshRecyclerView;
import com.hjt.mydouya.views.mvpviews.HomeView;
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

public class HomeFragment extends BaseFragment implements HomeView {
    private WeiboParameters mWeiboParameters;
    private SPUtils mSPUtils;
    private PullToRefreshRecyclerView rlv;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.ItemDecoration mItemDecoration;
    private List<StatusEntity> mEntityList;
    private StatusEntity mStatusEntity;
    private HomepageListAdapter mHomepageListAdapter;
    private HomePresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSPUtils = SPUtils.getIntantce(getActivity().getApplicationContext());
        mEntityList = new ArrayList<StatusEntity>();
        mHomepageListAdapter = new HomepageListAdapter(mEntityList, getActivity());
        mPresenter = new HomePresenterImp(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
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
        /*new BaseNetWork(getActivity(), CWUrls.HOME_TIME_LINE) {
            @Override
            public WeiboParameters onPrepare() {
                mWeiboParameters = new WeiboParameters(CWConstant.APP_KEY);
                mWeiboParameters.put(WBConstants.AUTH_ACCESS_TOKEN, mSPUtils.getToken().getToken
                ());// 第二次getToken是get Oauth2AccessToken里的mAccesToken
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

        }.get();*/
        // 初始化RecyclerView
        rlv = (PullToRefreshRecyclerView) inflater.inflate(R.layout.v_common_recycleview,
                container, false);
        init();
        // 第一次加载
        mPresenter.loadData();
        return rlv;
    }

    // 初始化RecyclerView
    private void init() {
        mLayoutManager = new LinearLayoutManager(getActivity());
//        rlv.setLayoutManager(mLayoutManager);
//        rlv.addItemDecoration( new DividerItemDecoration(getActivity(),1));
//        rlv.setAdapter(mHomepageListAdapter);
        rlv.getRefreshableView().setLayoutManager(mLayoutManager);
        rlv.getRefreshableView().addItemDecoration(new DividerItemDecoration(getActivity(), 1));
        rlv.getRefreshableView().setAdapter(mHomepageListAdapter);
        rlv.setMode(PullToRefreshBase.Mode.BOTH);
        rlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                mPresenter.loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                mPresenter.loadMore();
            }
        });
        // 这是自定义的Item监听器，在HomepageListAdapter有匿名内部类
        mHomepageListAdapter.setOnItemClickListener(new HomepageListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                LogUtils.e("The itemView position which you just touched is " + position + "");
                // 跳转至文字本文
                Intent intent = new Intent(getActivity(), ArticleActivity.class);
                intent.putExtra(StatusEntity.class.getSimpleName(), mEntityList.get(position));
                getActivity().startActivity(intent);
            }
        });
        // 这是自定义的CommentItem监听器，在HomepageListAdapter有匿名内部类
        mHomepageListAdapter.setOnCommentItemClickListener(new HomepageListAdapter
                .OnCommentItemClickListener() {
            @Override
            public void onCommentItemClick(View v, int position) {
                mStatusEntity = mEntityList.get(position);
                long id = mStatusEntity.id;
                Intent intent = new Intent(getActivity(), ArticleCommentActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        // 这是自定义的RepostItem（转发）监听器，在HomepageListAdapter有匿名内部类
        mHomepageListAdapter.setOnRetweetItemClickListener(new HomepageListAdapter
                .OnRetweetItemClickListener() {
            @Override
            public void onRetweetItemClick(View v, int position) {
                Toast.makeText(getActivity(), getResources().getText(R.string.lbl_uncomolete),
                        Toast.LENGTH_SHORT).show();
            }
        });
        // 这是自定义的LikeItem（转发）监听器，在HomepageListAdapter有匿名内部类
        mHomepageListAdapter.setOnLikeItemClickListener(new HomepageListAdapter
                .OnLikeItemClickListener() {
            @Override
            public void onLikeItemClick(View v, int position) {
                Toast.makeText(getActivity(), getResources().getText(R.string.lbl_uncomolete),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onError(String error) {
        rlv.onRefreshComplete();
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(List<StatusEntity> list) {
        rlv.onRefreshComplete();
        mEntityList.clear();
        mEntityList.addAll(list);
        mHomepageListAdapter.notifyDataSetChanged();
    }
}
