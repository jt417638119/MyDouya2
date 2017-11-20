package com.hjt.mydouya.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by HJT on 2017/11/19.
 */

public class DrawCenterTextView extends android.support.v7.widget.AppCompatTextView {
    public DrawCenterTextView(Context context) {
        super(context);
    }

    public DrawCenterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawCenterTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables =  getCompoundDrawables();
        Drawable drawableLeft = drawables[0];
        if (drawableLeft != null ) {
            int drawLeftWidth = drawableLeft.getIntrinsicWidth();
            int textWidth = (int) getPaint().measureText(getText().toString());
            int drawablePadding = getCompoundDrawablePadding();
            int bodyWidth = drawLeftWidth + textWidth +drawablePadding;
            canvas.translate((getWidth() - bodyWidth) / 2,0);
        }
        super.onDraw(canvas);
    }
}
