package com.hjt.mydouya.views.mvpviews;

import com.hjt.mydouya.entities.CommentEntity;

import java.util.List;

/**
 * Created by ougonden on 17/11/23.
 */

public interface ArticleCommentView extends BaseView {
    void onSuccess(List<CommentEntity> list);
}
