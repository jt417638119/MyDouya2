package com.hjt.mydouya.views.mvpviews;

import com.hjt.mydouya.entities.StatusEntity;

import java.util.List;

/**
 * Created by ougonden on 17/11/20.
 */

public interface MyArticleActivityView extends BaseView {
    void onSuccess(List<StatusEntity> list);
}
