package com.hjt.mydouya.activities;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.hjt.mydouya.R;
import com.hjt.mydouya.adapters.MyAllCommentListAdapter;
import com.hjt.mydouya.entities.CommentEntity;
import com.hjt.mydouya.presenter.AllCommentsPresenter;
import com.hjt.mydouya.presenter.AllCommentsPresenterImpl;
import com.hjt.mydouya.utils.LogUtils;
import com.hjt.mydouya.views.PullToRefreshRecyclerView;
import com.hjt.mydouya.views.mvpviews.MyAllCommentsActivityView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ougonden on 17/11/23.
 */

public class MyAllCommentsAcitivity extends BaseActivity implements MyAllCommentsActivityView {
    private RecyclerView.LayoutManager mLayoutManager;
    private PullToRefreshRecyclerView mRecyclerView;
    private MyAllCommentListAdapter mAdapter;
    private List<CommentEntity> mDataSet;
    private AllCommentsPresenter mAllCommentsPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecyclerView = (PullToRefreshRecyclerView) findViewById(R.id.rlv);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mDataSet = new ArrayList<CommentEntity>();
        mAdapter = new MyAllCommentListAdapter(getActivity(), mDataSet);
        // 加载数据
        mAllCommentsPresenter = new AllCommentsPresenterImpl(getApplicationContext(), getActivity
                (), this);
        mAllCommentsPresenter.loadData();
        init();

    }

    private void init() {
        // 设置toolbar
        getToolBar().setTitleVisible(false);
        getToolBar().setCustomTitle(R.string.lbl_mysend_comment);

        // 不可以侧拉
        getmDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        mRecyclerView.getRefreshableView().setLayoutManager(mLayoutManager);
        mRecyclerView.getRefreshableView().setAdapter(mAdapter);
        // 设置分割线
        mRecyclerView.getRefreshableView().addItemDecoration(new DividerItemDecoration
                (getApplicationContext(), 1));

        // 设置上下拉刷新
        mRecyclerView.setMode(PullToRefreshBase.Mode.BOTH);

        mRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {


            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                mAllCommentsPresenter.loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                mAllCommentsPresenter.loadMore();
            }
        });


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
    public void onSuccess(List<CommentEntity> list) {
        mRecyclerView.onRefreshComplete();
        mDataSet.clear();
        mDataSet.addAll(list);
        mAdapter.notifyDataSetChanged();
    }
}
