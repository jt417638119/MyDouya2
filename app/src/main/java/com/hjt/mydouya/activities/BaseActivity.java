package com.hjt.mydouya.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hjt.mydouya.R;
import com.hjt.mydouya.views.ToolbarX;

/**
 * Created by HJT on 2017/11/12.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private RelativeLayout rlContent;
    private ToolbarX mToolbarX;
    private LinearLayout llDrawer;
    private ImageView ivHeader;
    private TextView tvUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_baselayout);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_custom);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rlContent = (RelativeLayout) findViewById(R.id.rlContent);
        llDrawer = (LinearLayout) findViewById(R.id.llDrawer);
        ivHeader = (ImageView) findViewById(R.id.ivHeader);
        tvUserName = (TextView) findViewById(R.id.tvUserName);

        //IOC：控制反转，在父类中调用子类的实现
        View view = getLayoutInflater().inflate(getLayoutId(), rlContent, false);//
        // 将layout填充到rlContent
        rlContent.addView(view);

        mToolbarX = new ToolbarX(toolbar, this);

        // 设置状态栏为透明
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);


        }

    }

    // 子类的抽象方法
    public abstract int getLayoutId();

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        // anim_in_right_left是子Activity进来的参数，anim_out_right_left是父Activity出去的参数
        overridePendingTransition(R.anim.anim_in_right_left, R.anim.anim_out_right_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_out_left_right, R.anim.anim_in_left_right);
    }

    public ToolbarX getToolBar() {
        if (mToolbarX == null) {
            mToolbarX = new ToolbarX(toolbar, this);
        }
        return mToolbarX;
    }

    public DrawerLayout getmDrawerLayout() {
        if (mDrawerLayout == null) {
            mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_custom);
        }
        return mDrawerLayout;
    }

    public LinearLayout getLlDrawer() {
        if (llDrawer == null) {
            llDrawer = (LinearLayout) findViewById(R.id.llDrawer);
        }
        return llDrawer;
    }

    public ImageView getDrawerIvHeader() {
        if (ivHeader == null) {
            ivHeader = (ImageView) findViewById(R.id.ivHeader);
        }
        return ivHeader;
    }

    public TextView getDrawerTvUserName() {
        if (tvUserName == null) {
            tvUserName = (TextView) findViewById(R.id.tvUserName);
        }
        return tvUserName;
    }

    public void setDrawerTvUserName(String userName) {
        if (tvUserName == null) {
            tvUserName = (TextView) findViewById(R.id.tvUserName);
        }
        tvUserName.setText(userName);
    }

}













