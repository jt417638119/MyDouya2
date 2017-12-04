package com.hjt.mydouya.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hjt.mydouya.R;
import com.hjt.mydouya.fragments.HomeFragment;
import com.hjt.mydouya.fragments.MessageFragment;
import com.hjt.mydouya.fragments.ProfileFragment;
import com.hjt.mydouya.model.UserModelImpl;
import com.hjt.mydouya.presenter.HomePageActivityPresenter;
import com.hjt.mydouya.presenter.HomePageActivityPresenterImpl;
import com.hjt.mydouya.utils.CircleTransform;
import com.hjt.mydouya.utils.LogUtils;
import com.hjt.mydouya.utils.SPUtils;
import com.hjt.mydouya.views.mvpviews.HomePageView;

/**
 * Created by HJT on 2017/11/14.
 */

public class HomePageActivity extends BaseActivity implements HomePageView {
    private static final int UPDATE_UI = 1;
    private Handler mHandler;
    private FrameLayout flContainer;
    private FragmentTabHost tabHost;
    private RadioGroup rgTab;
    private RadioButton rbHome;
    private RadioButton rbMessage;
    private RadioButton rbProfile;
    private Class fragment[];
    //    private int menu_id = R.menu.menu_home;
    private int menu_id = -1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_UI:
                        onSuccess();
                        break;

                }
            }
        };
        // 初始化
        flContainer = (FrameLayout) findViewById(R.id.flContainer);
        tabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        rgTab = (RadioGroup) findViewById(R.id.rgTab);
        rbHome = (RadioButton) findViewById(R.id.rbHome);
        rbMessage = (RadioButton) findViewById(R.id.rbMessage);
        rbProfile = (RadioButton) findViewById(R.id.rbProfile);

        fragment = new Class[]{
                HomeFragment.class, MessageFragment.class, ProfileFragment.class
        };
        // 初始化tabHost
        tabHost.setup(getApplicationContext(), getSupportFragmentManager(), R.id.flContainer);
        for (int i = 0; i < fragment.length; i++) {
            // 为tabhost设置显示界面，也就是设置标签，第一个String是标识，第二个是名字
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(String.valueOf(i)).setIndicator(String
                    .valueOf(i));
            tabHost.addTab(tabSpec, fragment[i], null);
        }
        // 设置当前对象为0
        tabHost.setCurrentTab(0);

        // 设置tabgroup监听
        rgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rbHome:
                        tabHost.setCurrentTab(0);
//                        menu_id = R.menu.menu_home;
                        menu_id = -1;
//                        getToolBar().setTitle(R.string.lbl_home);
                        getToolBar().setNavigationIconVisible(View.VISIBLE);
                        getToolBar().setToolbarRightIconVisible(View.VISIBLE);
                        getToolBar().setCustomTitle(R.string.lbl_home);
                        break;
                    case R.id.rbMessage:
                        menu_id = -1; // 不需要menu
                        tabHost.setCurrentTab(1);
//                        getToolBar().setTitle(R.string.lbl_message);
                        getToolBar().setNavigationIconVisible(View.VISIBLE);
                        getToolBar().setToolbarRightIconVisible(View.VISIBLE);
                        getToolBar().setCustomTitle(R.string.lbl_message);
                        break;
                    case R.id.rbProfile:
                        menu_id = -1; // 不需要menu
                        tabHost.setCurrentTab(2);
                        getToolBar().setCustomTitle(R.string.lbl_profile);
                        getToolBar().setNavigationIconVisible(View.GONE);
                        getToolBar().setToolbarRightIconVisible(View.GONE);
                        break;
                }
                supportInvalidateOptionsMenu(); // 重新加载onCreateOptionsMenu
            }
        });

        /**
         * 设置toolbar属性
         */
        getToolBar().setDisplayHomeAsUpEnabled(false);
        // 设置Title不可见
        getToolBar().setTitleVisible(false);
        getToolBar().setCustomTitle(R.string.lbl_home);
        getToolBar().setToolbarRightIcon(R.mipmap.ic_toolbar_write);
        // 设置图标跳转至写微博
        getToolBar().setToolbarRightIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, WriteArticleActivity.class);
                startActivity(intent);
            }
        });

        // 缓存中已经没有本地的User
        if (SPUtils.getIntantce(getApplicationContext()).getUser() == null) {
            HomePageActivityPresenter homePageActivityPresenter = new
                    HomePageActivityPresenterImpl(getApplicationContext(), this, mHandler);
            // 更新ui
            homePageActivityPresenter.saveLocalUser();
        } else {
            // 加载toolbar上的头像
            onSuccess();
        }

    }

    // 将menu绑定到当前toolbar下
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu_id == -1) {
            menu.clear();// 不需要menu
        } else {
            // 将menu绑定到当前toolbar下
            getMenuInflater().inflate(menu_id, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_home_page;
    }

    @Override
    public void onSuccess() {
        getToolBar().setNavigationIcon(SPUtils.getIntantce(getApplicationContext()).getUser()
                .profile_image_url);
        // 设置toolbar左上角头像的监听
        getToolBar().setNavigationIconOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示drawerlayout
                toggle();
            }
        });
        // 初始化drawerlayout中的头像和名字
        Glide.with(this).load(SPUtils.getIntantce(this).getUser().profile_image_url).transform
                (new CircleTransform(this)).into(getDrawerIvHeader());
        setDrawerTvUserName(SPUtils.getIntantce(this).getUser().screen_name);

    }

    private void toggle() {
        // 系统的代码
//        int drawerLockMode = mDrawerLayout.getDrawerLockMode(GravityCompat.START);
//        if (mDrawerLayout.isDrawerVisible(GravityCompat.START)
//                && (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_OPEN)) {
//            mDrawerLayout.closeDrawer(GravityCompat.START);
//        } else if (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
//            mDrawerLayout.openDrawer(GravityCompat.START);
//        }
        int drawerLockMode = getmDrawerLayout().getDrawerLockMode(GravityCompat.START);
        if (getmDrawerLayout().isDrawerVisible(GravityCompat.START) && (drawerLockMode !=
                DrawerLayout.LOCK_MODE_LOCKED_OPEN)) {// 被锁是打开的，所以是关的
            getmDrawerLayout().closeDrawer(GravityCompat.START);
        } else if (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {// 被锁是关的，所以是开的
            getmDrawerLayout().openDrawer(GravityCompat.START);
        }
    }
}
