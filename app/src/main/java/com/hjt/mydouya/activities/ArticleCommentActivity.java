package com.hjt.mydouya.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.hjt.mydouya.R;
import com.hjt.mydouya.adapters.ArticleCommentListAdapter;
import com.hjt.mydouya.entities.CommentEntity;
import com.hjt.mydouya.presenter.ArticleCommentPresenter;
import com.hjt.mydouya.presenter.ArticleCommentPresenterImpl;
import com.hjt.mydouya.utils.SPUtils;
import com.hjt.mydouya.views.PullToRefreshRecyclerView;
import com.hjt.mydouya.views.mvpviews.ArticleCommentView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ougonden on 17/11/23.
 */

public class ArticleCommentActivity extends BaseActivity implements ArticleCommentView{
    private long id;
    private SPUtils mSPUtils;
    private List<CommentEntity> mDataSet;
    private PullToRefreshRecyclerView rlv;
    private ArticleCommentListAdapter mArticleCommentListAdapter;
    private RecyclerView.LayoutManager rlvManager;
    private ArticleCommentPresenter mArticleCommentPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        id = intent.getLongExtra("id",0);
        rlv = (PullToRefreshRecyclerView) findViewById(R.id.rlv);
        rlvManager = new LinearLayoutManager(this);
        mSPUtils = SPUtils.getIntantce(getApplicationContext());
        mDataSet = new ArrayList<CommentEntity>();
        mArticleCommentListAdapter = new ArticleCommentListAdapter(getActivity(),mDataSet);
        mArticleCommentPresenter = new ArticleCommentPresenterImpl(this,id);
        // 开始业务处理
        mArticleCommentPresenter.loadData();
        init();
    }

    private void init() {
        rlv.getRefreshableView().setLayoutManager(rlvManager);
        rlv.getRefreshableView().setAdapter(mArticleCommentListAdapter);
        rlv.setMode(PullToRefreshBase.Mode.BOTH);
        // 加分割线
        rlv.getRefreshableView().addItemDecoration(new DividerItemDecoration(getActivity(),1));

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
        rlv.onRefreshComplete();
        Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
    }

    // 网络请求成功时调用这个
    @Override
    public void onSuccess(List<CommentEntity> list) {
        rlv.onRefreshComplete();
        mDataSet.clear();
        mDataSet.addAll(list);
        mArticleCommentListAdapter.notifyDataSetChanged();
    }
}
