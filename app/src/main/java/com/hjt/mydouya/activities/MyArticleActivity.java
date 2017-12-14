package com.hjt.mydouya.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.hjt.mydouya.R;
import com.hjt.mydouya.adapters.HomepageListAdapter;
import com.hjt.mydouya.entities.StatusEntity;
import com.hjt.mydouya.presenter.MyArticlePresenter;
import com.hjt.mydouya.presenter.MyArticlePresenterImpl;
import com.hjt.mydouya.utils.LogUtils;
import com.hjt.mydouya.views.PullToRefreshRecyclerView;
import com.hjt.mydouya.views.mvpviews.MyArticleActivityView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HJT on 2017/11/12.
 */

public class MyArticleActivity extends BaseActivity implements MyArticleActivityView{
    private static final int UPDATE_RECECLE = 1;
    private PullToRefreshRecyclerView rlv;
    private StatusEntity mStatusEntity;
    private RecyclerView.LayoutManager mLayoutManager;
    private HomepageListAdapter mAdapter;
    private List<StatusEntity> mDataset;
    private MyArticlePresenter mMyArticlePresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rlv = (PullToRefreshRecyclerView) findViewById(R.id.rlv);
        mLayoutManager = new LinearLayoutManager(this);
        mDataset = new ArrayList<StatusEntity>();
        mAdapter = new HomepageListAdapter(mDataset, getActivity());
        mMyArticlePresenter = new MyArticlePresenterImpl(getApplicationContext(),this,getActivity());
        // 加载信息
        mMyArticlePresenter.loadData();
        init();
    }

    private void init() {
        /**
         * 设置显示列表
         */
        rlv.getRefreshableView().setLayoutManager(mLayoutManager);
        rlv.getRefreshableView().setAdapter(mAdapter);
        rlv.setMode(PullToRefreshBase.Mode.BOTH);
        // 添加分割线
        rlv.getRefreshableView().addItemDecoration(new DividerItemDecoration(getActivity(),1));
        rlv.setMode(PullToRefreshBase.Mode.BOTH);
        rlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                mMyArticlePresenter.loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                mMyArticlePresenter.loadMore();
            }
        });

        // 这是自定义的Item监听器，在HomepageListAdapter有匿名内部类
        mAdapter.setOnItemClickListener(new HomepageListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                LogUtils.e("The itemView position which you just touched is " + position + "");
                // 跳转至文字本文
                Intent intent = new Intent(getActivity(), ArticleActivity.class);
                intent.putExtra(StatusEntity.class.getSimpleName(), mDataset.get(position));
                getActivity().startActivity(intent);
            }
        });
        // 这是自定义的CommentItem监听器，在HomepageListAdapter有匿名内部类
        mAdapter.setOnCommentItemClickListener(new HomepageListAdapter
                .OnCommentItemClickListener() {
            @Override
            public void onCommentItemClick(View v, int position) {
                mStatusEntity = mDataset.get(position);
                long id = mStatusEntity.id;
                Intent intent = new Intent(getActivity(), ArticleCommentActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        // 这是自定义的RepostItem（转发）监听器，在HomepageListAdapter有匿名内部类
        mAdapter.setOnRetweetItemClickListener(new HomepageListAdapter
                .OnRetweetItemClickListener() {
            @Override
            public void onRetweetItemClick(View v, int position) {
                Toast.makeText(getActivity(), getResources().getText(R.string.lbl_uncomolete),
                        Toast.LENGTH_SHORT).show();
            }
        });
        // 这是自定义的LikeItem（转发）监听器，在HomepageListAdapter有匿名内部类
        mAdapter.setOnLikeItemClickListener(new HomepageListAdapter
                .OnLikeItemClickListener() {
            @Override
            public void onLikeItemClick(View v, int position) {
                Toast.makeText(getActivity(), getResources().getText(R.string.lbl_uncomolete),
                        Toast.LENGTH_SHORT).show();
            }
        });


        /**
         * 设置toolbar
         */
        getToolBar().setTitleVisible(false);
        getToolBar().setCustomTitle(R.string.lbl_my_weibo);

        // 禁止drawerlayout手势滑动
        getmDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public int getLayoutId() {
        return R.layout.v_common_recycleview;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onError(String error) {
        LogUtils.e(error);
    }

    @Override
    public void onSuccess(List<StatusEntity> list) {
        rlv.onRefreshComplete();
        mDataset.clear();
        mDataset.addAll((ArrayList<StatusEntity>)list);
        // 更新适配器
        mAdapter.notifyDataSetChanged();
    }

}













