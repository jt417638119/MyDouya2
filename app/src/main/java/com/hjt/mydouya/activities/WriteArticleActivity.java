package com.hjt.mydouya.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hjt.mydouya.R;
import com.hjt.mydouya.entities.StatusEntity;
import com.hjt.mydouya.presenter.WritePresenter;
import com.hjt.mydouya.presenter.WritePresenterImpl;
import com.hjt.mydouya.views.CustomDialog;
import com.hjt.mydouya.views.mvpviews.WriteView;

import java.util.List;

/**
 * Created by ougonden on 17/12/3.
 */

public class WriteArticleActivity extends BaseActivity implements WriteView {
    private EditText etWriteConent;
    private WritePresenter mWritePresenter;
    private CustomDialog.Builder builder;
    private CustomDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        etWriteConent = (EditText) findViewById(R.id.etWriteContent);
        mWritePresenter = new WritePresenterImpl(getApplicationContext(),this,getActivity());
        builder = new CustomDialog.Builder(getActivity());
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

        getToolBar().setToolbarRightIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTwoButtonDialog("确定发送此微博？","取消","确定",new NListner(),new PListner());
            }
        });
    }

    private void showTwoButtonDialog(String message, String nText, String pText, View
            .OnClickListener nListener, View.OnClickListener pListener) {
        mDialog = builder.message(message).setNegativeButton(nText, nListener).setPositiveButton(pText,
                pListener).createTwoButtonDialog();
        mDialog.show();
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_weibo_write;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onError(String error) {
        Toast.makeText(getActivity(),"发送微博失败",Toast.LENGTH_SHORT).show();
        mDialog.dismiss();
        finish();
    }

    @Override
    public void onSuccess() {
        Toast.makeText(getActivity(),"发送微博成功！",Toast.LENGTH_SHORT).show();
        mDialog.dismiss();
        finish();
    }

    // 设置dialog取消按钮的监听
    public class NListner implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mDialog.dismiss();
        }
    }

    // 设置dialog确定按钮的监听
    public class PListner implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mWritePresenter.writeArticle(etWriteConent.getText().toString());
        }
    }

}
