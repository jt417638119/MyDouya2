package com.hjt.mydouya.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hjt.mydouya.R;
import com.hjt.mydouya.activities.LandingPageActivity;
import com.hjt.mydouya.utils.CircleTransform;
import com.hjt.mydouya.utils.LogUtils;
import com.hjt.mydouya.utils.SPUtils;
import com.hjt.mydouya.views.CustomDialog;

/**
 * Created by HJT on 2017/11/14.
 */

public class ProfileFragment extends BaseFragment {
    private TextView tvLoginOut;
    private View view;
    private ImageView ivHeader;
    private TextView tvUserName;
    CustomDialog.Builder builder;
    CustomDialog mDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        builder = new CustomDialog.Builder(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_profile, container, false);
        init();
        return view;
    }

    private void init() {
        ivHeader = (ImageView) view.findViewById(R.id.ivHeader);
        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        tvLoginOut = (TextView) view.findViewById(R.id.tvLoginOut);

        // 加载头像
        Glide.with(this).load(SPUtils.getIntantce(getContext()).getUser().profile_image_url)
                .transform(new CircleTransform(getContext())).into(ivHeader);
        // 加载名字
        tvUserName.setText(SPUtils.getIntantce(getContext()).getUser().screen_name);


        tvLoginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTwoButtonDialog("确定退出登录？", "确定", "取消", new PositiveButtonLisentner(), new
                        NegativeButtonLisentner());
            }
        });
    }

    public void showTwoButtonDialog(String message, String pText, String nText, View
            .OnClickListener pListener, View.OnClickListener nListener) {
        mDialog = builder.message(message).setPositiveButton(pText, pListener).setNegativeButton
                (nText,
                nListener).createTwoButtonDialog();
        mDialog.show();

    }

    public class PositiveButtonLisentner implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            SPUtils mSharedPreference = SPUtils.getIntantce(getActivity());
            mSharedPreference.clear();
            Intent intent = new Intent(getActivity(), LandingPageActivity.class);
            startActivity(intent);
        }


    }

    public class NegativeButtonLisentner implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            LogUtils.e("aasdasdasdas" + SPUtils.getIntantce(getActivity()).getText());

            mDialog.dismiss();
        }
    }
}
