package com.hjt.mydouya.activities;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.widget.EditText;

import com.hjt.mydouya.R;

/**
 * Created by ougonden on 17/12/3.
 */

public class WriteArticleActivity extends BaseActivity {
    private EditText etWriteConent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        etWriteConent = (EditText) findViewById(R.id.etWriteContent);

        etWriteConent.setHint("分享微博....");
        init();

    }

    private void init() {
        /*
        设置toolbar
         */
        getToolBar().setTitleVisible(false);
        getToolBar().setCustomTitle(R.string.lbl_write_article);
        getToolBar().setToolbarRightIcon(R.mipmap.ic_toolbar_write);

        // 禁止drawerlayout手势滑动
        getmDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_weibo_write;
    }
}
