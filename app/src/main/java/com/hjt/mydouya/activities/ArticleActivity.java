package com.hjt.mydouya.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.hjt.mydouya.R;
import com.hjt.mydouya.adapters.ArticleCommentListAdapter;
import com.hjt.mydouya.entities.CommentEntity;
import com.hjt.mydouya.entities.PicUrlsEntity;
import com.hjt.mydouya.entities.StatusEntity;
import com.hjt.mydouya.presenter.AllCommentsPresenter;
import com.hjt.mydouya.presenter.ArticleCommentPresenter;
import com.hjt.mydouya.presenter.ArticleCommentPresenterImpl;
import com.hjt.mydouya.utils.CircleTransform;
import com.hjt.mydouya.utils.LogUtils;
import com.hjt.mydouya.utils.RichTextUtils;
import com.hjt.mydouya.utils.TimeFormatUtils;
import com.hjt.mydouya.views.mvpviews.ArticleCommentView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ougonden on 17/11/23.
 */

public class ArticleActivity extends BaseActivity implements ArticleCommentView {
    private StatusEntity mStatusEntity;
    private ImageView ivHeader;
    private TextView tvUserName;
    private TextView tvTime;
    private TextView tvSource;
    private TextView tvContent;
    private TextView tvReContent;
    private ImageView ivContent;
    private ImageView ivReContent;
    private TextView tvCommentsCount;
    private LinearLayout llRe;
    private LinearLayout llNoComments;
    private RecyclerView rlvComments;
    private RecyclerView.LayoutManager layoutManager;
    private ArticleCommentListAdapter mArticleCommentListAdapter;
    private List<CommentEntity> mDataSet;
    private ArticleCommentPresenter mArticleCommentPresenter;
    private PullToRefreshScrollView mRlvArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取上个页面点击后传来的文章对象
        mStatusEntity = (StatusEntity) getIntent().getSerializableExtra(StatusEntity.class
                .getSimpleName());
        ivHeader = (ImageView) findViewById(R.id.ivHeader);
        ivContent = (ImageView) findViewById(R.id.ivContent);
        ivReContent = (ImageView) findViewById(R.id.ivReContent);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvSource = (TextView) findViewById(R.id.tvSource);
        tvContent = (TextView) findViewById(R.id.tvContent);
        tvReContent = (TextView) findViewById(R.id.tvReContent);
        tvCommentsCount = (TextView) findViewById(R.id.tvCommentsCount);
        llRe = (LinearLayout) findViewById(R.id.llRe);
        llNoComments = (LinearLayout) findViewById(R.id.llNoComments);
        rlvComments = (RecyclerView) findViewById(R.id.rlvComments);
        mRlvArticle = (PullToRefreshScrollView) findViewById(R.id.rlvArticle);
        layoutManager = new LinearLayoutManager(getActivity());
        mDataSet = new ArrayList<CommentEntity>();
        mArticleCommentListAdapter = new ArticleCommentListAdapter(getApplicationContext(),
                mDataSet);
        mArticleCommentPresenter = new ArticleCommentPresenterImpl(this,mStatusEntity.id);

        // 加载评论
//        mArticleCommentPresenter.loadData();

        // 呈现文章
        init();

    }

    private void init() {
        // 设置toolbar
        getToolBar().setTitleVisible(false);
        getToolBar().setCustomTitle(R.string.lbl_article);

        // 设置不可侧拉
        getmDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        // 设置上下拉模式，下面再根据有无评论进行上下拉监听
        mRlvArticle.setMode(PullToRefreshBase.Mode.BOTH);

        // 设置RecycleView
        rlvComments.setLayoutManager(layoutManager);
        rlvComments.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        rlvComments.setAdapter(mArticleCommentListAdapter);

        // 设置头像
        Glide.with(getActivity()).load(mStatusEntity.user.profile_image_url).transform(new
                CircleTransform(getActivity())).into(ivHeader);

        tvUserName.setText(mStatusEntity.user.screen_name);
        tvTime.setText(TimeFormatUtils.parseToYYMMDD(mStatusEntity.created_at));
        tvSource.setText(Html.fromHtml(mStatusEntity.source).toString());
        tvContent.setText(RichTextUtils.getRichText(getActivity(), mStatusEntity.text));

        // 设置文章图片内容
        List<PicUrlsEntity> pics = mStatusEntity.pic_urls;
        if (pics != null && pics.size() != 0) { // 有图片显示
            ivContent.setVisibility(View.VISIBLE);
            final PicUrlsEntity pic = pics.get(0);
            pic.original_pic = pic.thumbnail_pic.replace("thumbnail", "large");
            pic.bmiddle_pic = pic.thumbnail_pic.replace("thumbnail", "bmiddle");

            Glide.with(getActivity()).load(pic.original_pic).into(ivContent);
            // 设置点击图片监听
            ivContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PhotoViewActivity.class);
                    intent.putExtra(PicUrlsEntity.class.getSimpleName(), pic);
                    // 实现跳转
                    startActivity(intent);
                }
            });
        } else {// 没有图片
            ivContent.setVisibility(View.GONE);
        }

        // 设置转发内容
        StatusEntity reStatusEntity = mStatusEntity.retweeted_status;
        if (reStatusEntity != null) { // 转发内容不为空
            llRe.setVisibility(View.VISIBLE);
            tvReContent.setText(RichTextUtils.getRichText(getActivity(), ("@"
                    + reStatusEntity.user.screen_name + "："
                    + reStatusEntity.text)));

            List<PicUrlsEntity> rePics = reStatusEntity.pic_urls;
            if (rePics != null && rePics.size() != 0) { // 转发内容图片不为空
                ivReContent.setVisibility(View.VISIBLE);
                final PicUrlsEntity rePic = rePics.get(0);
                rePic.original_pic = rePic.thumbnail_pic.replace("thumbnail", "large");
                rePic.bmiddle_pic = rePic.thumbnail_pic.replace("thumbnail", "bmiddle");
                Glide.with(getActivity()).load(rePic.original_pic).asBitmap().fitCenter().into
                        (ivReContent);
                // 设置点击图片监听
                ivReContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PhotoViewActivity.class);
                        intent.putExtra(PicUrlsEntity.class.getSimpleName(), rePic);
                        // 实现跳转
                        startActivity(intent);
                    }
                });
            } else { // 图片为空
                ivReContent.setVisibility(View.GONE);
            }

        } else {// 转发内容为空
            llRe.setVisibility(View.GONE);
        }

        tvCommentsCount.setText(String.valueOf(mStatusEntity.comments_count));

        if (mStatusEntity.comments_count != 0) { // 评论数不为0
            llNoComments.setVisibility(View.GONE);
            // 加载评论
            mArticleCommentPresenter.loadData();

            // 设置上下拉监听
            mRlvArticle.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {


                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                    // 加载最新评论
                    mArticleCommentPresenter.loadData();
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                    // 加载更多评论
                    mArticleCommentPresenter.loadMore();
                }
            });
        }else { // 评论数为0
            llNoComments.setVisibility(View.VISIBLE);
            // 评论列表隐藏
            rlvComments.setVisibility(View.GONE);
        }


    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_article;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onSuccess(List<CommentEntity> list) {
        mRlvArticle.onRefreshComplete();
        mDataSet.clear();
        mDataSet.addAll(list);
        mArticleCommentListAdapter.notifyDataSetChanged();
    }
}
