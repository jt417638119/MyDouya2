package com.hjt.mydouya.views;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.hjt.mydouya.R;

/**
 * Created by HJT on 2017/11/13.
 */

public class ToolbarX {
    private Toolbar mToolbar;
    private AppCompatActivity mActivity;
    private ActionBar mActionBar;
    private RelativeLayout rlCustom;

    public ToolbarX(Toolbar toolbar, AppCompatActivity activity) {
        mToolbar = toolbar;
        mActivity = activity;
        rlCustom = (RelativeLayout) mToolbar.findViewById(R.id.rlCustom);
        mActivity.setSupportActionBar(mToolbar);
        mActionBar = mActivity.getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);// 设置左上角有返回标志可见
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
    }

    public void setTitle(String text) {
        mActionBar.setTitle(text);
    }

    public void setSubTite(String text) {
        mActionBar.setSubtitle(text);
    }

    public void setTitle(int resId) {
        mActionBar.setTitle(resId);
    }

    public void setSubTite(int resId) {
        mActionBar.setSubtitle(resId);
    }

    // 不同的Activity设置不同的监听
    public void setNavigationOnClickListener(View.OnClickListener listener) {
        mToolbar.setNavigationOnClickListener(listener);
    }

    // 不同的Activity设置不同setNavigationIcon图标
    public void setNavigationIcon(int resId) {
        mToolbar.setNavigationIcon(resId);
    }

    // 不同的Activity设置是否呈现
    public void setDisplayHomeAsUpEnabled(Boolean show) {
        mActionBar.setDisplayHomeAsUpEnabled(show);
    }

    public void setCustomView(View view) {
        rlCustom.removeAllViews();
        rlCustom.addView(view);
    }

    public void hide() {
        mActionBar.hide();
    }

}
