package com.hjt.mydouya.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by ougonden on 17/11/30.
 */

public class RoundTransform extends BitmapTransformation {
    private static float radius = 0f;

    public RoundTransform(Context context) {
        this(context,2);
    }


    public RoundTransform(BitmapPool bitmapPool) {
        super(bitmapPool);
    }

    public RoundTransform(Context context, int dp) {
        super(context);
        // px 与 dp 的换算公式
        // 这是根据不同机型的换算公式，dp是独立密度像素
        this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
    }


    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) {
            return null;
        }
        // 8888相当于32位，位数越高颜色越多
        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        // 设置平滑
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f,0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF,radius,radius,paint);

        return result;
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}
