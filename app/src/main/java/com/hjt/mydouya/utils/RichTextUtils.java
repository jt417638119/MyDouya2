package com.hjt.mydouya.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;

import com.hjt.mydouya.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ougonden on 17/11/20.
 */

public class RichTextUtils {
    private static String regexWeb = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%?=~_()|!:,.;]";
    private static String regexAt = "@[\\u4e00-\\u9fa5a-zA-Z0-9_-]+";
    private static final Pattern WEB_PATTERN = Pattern.compile(regexWeb);
    private static final Pattern MENTION_PATTERN = Pattern.compile(regexAt);

    public static SpannableString getRichText(Context context, String text) {
        SpannableString string = new SpannableString(text);
        if (!TextUtils.isEmpty(text)) {
            final int link_color = ContextCompat.getColor(context, R.color.cw_blue);
            final int mention_color = ContextCompat.getColor(context, R.color.cw_blue);
            Matcher matcher = WEB_PATTERN.matcher(text);
            // 找到超链接文本
            while (matcher.find()) { // 找到一个则变颜色
                final String url = matcher.group();
                string.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        //LogUtils.e(url);
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setUnderlineText(false);
                        ds.setColor(mention_color);
                    }
                },matcher.start(),matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            Matcher matcher_two = MENTION_PATTERN.matcher(text);
            // 在文本中找到@
            while (matcher_two.find()) { // 找到一个则变颜色
                final String url = matcher_two.group();
                string.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        //LogUtils.e(url);
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setUnderlineText(false);
                        ds.setColor(mention_color);
                    }
                },matcher_two.start(),matcher_two.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return string;
    }


}
