package com.hjt.mydouya.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjt.mydouya.R;

/**
 * Created by ougonden on 17/11/22.
 */

public class CustomDialog extends Dialog {
    public CustomDialog(@NonNull Context context) {
        super(context);
    }

    public CustomDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected CustomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private String message;
        private View contentView;
        private String positiveButtonText;
        private String negativeButtonText;
        private String singleButtonText;
        private View.OnClickListener positiveButtonClickListener;
        private View.OnClickListener negativeButtonClickListener;
        private View.OnClickListener singleButtonClickListener;

        private View layout;
        private CustomDialog dialog;

        public Builder(Context context) {
            // 这里传入自定义的style
            dialog = new CustomDialog(context, R.style.Dialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.dialog_layout, null);
            dialog.setContentView(layout);

        }


        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder contentView(View contentView) {
            this.contentView = contentView;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, View.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, View.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setSingleButton(String singleButtonText, View.OnClickListener listener) {
            this.singleButtonText = singleButtonText;
            this.singleButtonClickListener = listener;
            return this;
        }

        /**
         * 创建单按钮对话框
         */
        public CustomDialog createSingleButtonDialog() {
            showSingleButton();
            ((TextView) layout.findViewById(R.id.tvSingleButton)).setOnClickListener(singleButtonClickListener);
            // 如果传入的按钮文字为空，默认返回
            if (singleButtonText != null) {
                ((TextView)layout.findViewById(R.id.tvSingleButton)).setText(singleButtonText);
            } else {
                ((TextView)layout.findViewById(R.id.tvSingleButton)).setText("返回");
            }
            create();
            return dialog;
        }

        /**
         * 显示单按钮布局，隐藏双按钮
         */
        private void showSingleButton() {
            layout.findViewById(R.id.llSingleButton).setVisibility(View.VISIBLE);
            layout.findViewById(R.id.llTwoButton).setVisibility(View.GONE);
        }

        /**
         * 创建双按钮对话框
         */
        public CustomDialog createTwoButtonDialog() {
            showTwoButton();
            ((TextView)layout.findViewById(R.id.tvPositiveButton)).setOnClickListener(positiveButtonClickListener);
            ((TextView)layout.findViewById(R.id.tvNegativeButton)).setOnClickListener(negativeButtonClickListener);
            // 如果传入的按钮文字为空，默认返回
            if (positiveButtonText != null) {
                ((TextView)layout.findViewById(R.id.tvPositiveButton)).setText(positiveButtonText);
            } else {
                ((TextView)layout.findViewById(R.id.tvPositiveButton)).setText("确定");
            }
            if (negativeButtonText != null) {
                ((TextView)layout.findViewById(R.id.tvNegativeButton)).setText(negativeButtonText);
            } else {
                ((TextView)layout.findViewById(R.id.tvNegativeButton)).setText("取消");
            }
            create();
            return dialog;

        }

        private void showTwoButton() {
            layout.findViewById(R.id.llSingleButton).setVisibility(View.GONE);
            layout.findViewById(R.id.llTwoButton).setVisibility(View.VISIBLE);
        }

        /**
         * 单双按钮公共部分设置
         */
        private void create() {
            if (message != null) {
                ((TextView) layout.findViewById(R.id.tvMessage)).setText(message);
            } else if (contentView != null) { // 如果使用Builder的setContentview方法传入了布局，则使用传入的布局
                ((LinearLayout) layout.findViewById(R.id.llContent)).removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.llContent))
                        .addView(contentView,
                                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                        );
            }
            dialog.setContentView(layout);
            dialog.setCancelable(true); // 用户可以点击手机Back取消对话框显示
            dialog.setCanceledOnTouchOutside(false);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = 650;
        win.setAttributes(lp);
    }
}














