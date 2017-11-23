package com.hjt.mydouya.activities;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.hjt.mydouya.R;
import com.hjt.mydouya.fragments.HomeFragment;
import com.hjt.mydouya.fragments.MessageFragment;
import com.hjt.mydouya.fragments.ProfileFragment;

/**
 * Created by HJT on 2017/11/14.
 */

public class HomePageActivity extends BaseActivity {
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
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(String.valueOf(i)).setIndicator(String.valueOf(i));
            tabHost.addTab(tabSpec,fragment[i],null);
        }
        // 设置当前对象为0
        tabHost.setCurrentTab(0);

        // toolbar
        getToolBar().setDisplayHomeAsUpEnabled(false);
        getToolBar().setTitle(R.string.lbl_home);

        // 设置tabgroup监听
        rgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rbHome:
                        tabHost.setCurrentTab(0);
//                        menu_id = R.menu.menu_home;
                        menu_id = -1;
                        getToolBar().setTitle(R.string.lbl_home);
                        break;
                    case R.id.rbMessage:
                        menu_id = -1; // 不需要menu
                        tabHost.setCurrentTab(1);
                        getToolBar().setTitle(R.string.lbl_message);
                        break;
                    case R.id.rbProfile:
                        menu_id = -1; // 不需要menu
                        tabHost.setCurrentTab(2);
                        getToolBar().setTitle(R.string.lbl_profile);
                        break;
                }
                supportInvalidateOptionsMenu(); // 重新加载onCreateOptionsMenu
            }
        });

    }

    // 将menu绑定到当前toolbar下
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu_id == -1 ) {
            menu.clear();// 不需要menu
        }else {
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
}
