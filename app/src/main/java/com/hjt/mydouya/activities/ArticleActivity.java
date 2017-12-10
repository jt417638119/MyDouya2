package com.hjt.mydouya.activities;


import android.content.Intent;
import android.os.Bundle;

import com.hjt.mydouya.R;
import com.hjt.mydouya.entities.StatusEntity;
import com.hjt.mydouya.utils.LogUtils;

/**
 * Created by ougonden on 17/11/23.
 */

public class ArticleActivity extends BaseActivity {
    private StatusEntity mStatusEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStatusEntity = (StatusEntity) getIntent().getSerializableExtra(StatusEntity.class.getSimpleName());
        LogUtils.e("Testttttt" + mStatusEntity.created_at);

    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_article;
    }
}
