package com.hjt.mydouya.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.hjt.mydouya.R;
import com.hjt.mydouya.entities.PicUrlsEntity;
import com.hjt.mydouya.views.ToolbarX;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by HJT on 2017/11/12.
 */

public class PhotoViewActivity extends BaseActivity {
    private PicUrlsEntity mPicUrlsEntity;
    private PhotoView photoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // toolbar隐藏
        getToolBar().hide();

        // 取消侧拉
        getmDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        // 状态栏消失
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mPicUrlsEntity = (PicUrlsEntity) getIntent().getSerializableExtra(PicUrlsEntity.class.getSimpleName());
        photoView = (PhotoView) findViewById(R.id.photoview);

        init();
    }

    private void init() {
        // bitmap是位图，还原每一个位的颜色
        Glide.with(this).load(mPicUrlsEntity.original_pic).asBitmap().fitCenter().into(photoView);
        photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                finish();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_photoview;
    }
}













