package com.hjt.mydouya.views;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hjt.mydouya.R;
import com.hjt.mydouya.utils.CircleTransform;

/**
 * Created by HJT on 2017/11/13.
 */

public class ToolbarX {
    private Toolbar mToolbar;
    private AppCompatActivity mActivity;
    private ActionBar mActionBar;
    private RelativeLayout rlCustom;
    private ImageView ivNavigation;
    private TextView tvCustomTitle;
    private ImageView ivWrite;

    public ToolbarX(Toolbar toolbar, AppCompatActivity activity) {
        mToolbar = toolbar;
        mActivity = activity;
//        rlCustom = (RelativeLayout) mToolbar.findViewById(R.id.rlCustom);
        ivNavigation = (ImageView) mToolbar.findViewById(R.id.ivNavigation);
        ivWrite = (ImageView) mToolbar.findViewById(R.id.ivWrite);
        tvCustomTitle = (TextView) mToolbar.findViewById(R.id.tvTitle);
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

    public void setCustomTitle(int resId) {
        tvCustomTitle.setText(resId);
    }

    public void setSubTite(int resId) {
        mActionBar.setSubtitle(resId);
    }


    public void setTitleVisible(Boolean visible) {
        mActionBar.setDisplayShowTitleEnabled(visible);
    }

    // 不同的Activity设置不同的监听
    public void setNavigationOnClickListener(View.OnClickListener listener) {
        mToolbar.setNavigationOnClickListener(listener);
    }

    // 不同的Activity设置不同setNavigationIcon图标
    public void setNavigationIcon(int resId) {
        mToolbar.setNavigationIcon(resId);
    }

    public void setNavigationIcon(String profile_image_url) {
        Glide.with(mActivity).load(profile_image_url).transform(new CircleTransform(mActivity))
                .into(ivNavigation);
    }

    // 设置toolbar右侧图标的显示
    public void setToolbarRightIcon(int resId) {
        if (ivWrite == null) {
            ivWrite = (ImageView) mToolbar.findViewById(R.id.ivWrite);
        }
        ivWrite.setImageResource(resId);
    }

    // 设置toolbar右侧图标的的监听
    public void setToolbarRightIconOnClickListener(View.OnClickListener listener) {
        if (ivWrite == null) {
            ivWrite = (ImageView) mToolbar.findViewById(R.id.ivWrite);
        }
        ivWrite.setOnClickListener(listener);
    }

    // 设置toolbar右侧图标的的是否可见
    public void setToolbarRightIconVisible(int visible) {
        ivWrite.setVisibility(visible);
    }


    public void setNavigationIconOnclickListener(View.OnClickListener listener) {
        ivNavigation.setOnClickListener(listener);
    }

    // 设置toolbar左侧图标的的是否可见
    public void setNavigationIconVisible(int visible) {
        ivNavigation.setVisibility(visible);
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
