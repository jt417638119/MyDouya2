package com.hjt.mydouya.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjt.mydouya.activities.CWConstant;
import com.hjt.mydouya.entities.CommentEntity;
import com.hjt.mydouya.entities.HttpResponse;
import com.hjt.mydouya.networks.BaseNetWork;
import com.hjt.mydouya.networks.CWUrls;
import com.hjt.mydouya.networks.ParameterKeySet;
import com.hjt.mydouya.utils.SPUtils;
import com.hjt.mydouya.views.mvpviews.ArticleCommentView;
import com.sina.weibo.sdk.net.WeiboParameters;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ougonden on 17/11/20.
 */

public class ArticleCommentPresenterImpl implements ArticleCommentPresenter{
    private int page = 1;
    private ArticleCommentView mArticleCommentView;
    private WeiboParameters mParameters;
    private SPUtils mSPUtils;
    private long id;
    private List<CommentEntity> mCommentEntityList;

    public ArticleCommentPresenterImpl(ArticleCommentView mArticleCommentView, long id) {
        this.id = id;
        this.mArticleCommentView = mArticleCommentView;
        this.mParameters = new WeiboParameters(CWConstant.APP_KEY);
        this.mSPUtils = SPUtils.getIntantce(mArticleCommentView.getActivity());
        this.mCommentEntityList = new ArrayList<CommentEntity>();


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
        new BaseNetWork(mArticleCommentView.getActivity(), CWUrls.COMMENT_SHOW) {
            @Override
            public WeiboParameters onPrepare() {
                mParameters.put(ParameterKeySet.AUTH_ACCESS_TOKEN, mSPUtils.getToken().getToken());
                mParameters.put(ParameterKeySet.ID,id);
                mParameters.put(ParameterKeySet.PAGE, page);
                mParameters.put(ParameterKeySet.COUNT, 10);
                return mParameters;
            }

            @Override
            public void onFinish(HttpResponse httpResponse, boolean success) {
                if (success) {
                    Type type = new TypeToken<ArrayList<CommentEntity>>(){}.getType();
                    List<CommentEntity> list = new Gson().fromJson(httpResponse.response,type);
                    if (!loadMore) {
                        mCommentEntityList.clear();
                    }
                    mCommentEntityList.addAll(list);
                    mArticleCommentView.onSuccess(mCommentEntityList);
                }else {
                    mArticleCommentView.onError(httpResponse.response);
                }
            }
        }.get();
    }
}
