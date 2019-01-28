/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.lang.reflect.Field;

import cn.denua.v2ex.R;

/*
 * Loading web image to ImageSpan
 *
 * @author denua
 * @date 2018/11/10 21
 */
public class WebImageSpan extends ImageSpan {

    private String mUri;
    private TextView mTextView;
    private boolean isShow;

    public WebImageSpan(@NonNull Context context, String mUri, TextView mTextView) {
        super(context, R.drawable.ic_launcher);
        this.mUri = mUri;
        this.mTextView = mTextView;
    }

    @Override
    public Drawable getDrawable() {
        if (isShow){
            return super.getDrawable();
        }
        Glide.with(mTextView.getContext()).load(mUri).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                Resources resources = mTextView.getContext().getResources();
                int targetWidth = (int) (resources.getDisplayMetrics().widthPixels * 0.8);
                Bitmap zoom = zoom(ConvertUtils.drawable2Bitmap(resource), targetWidth);
                BitmapDrawable b = new BitmapDrawable(resources, zoom);
                b.setBounds(0, 0, b.getIntrinsicWidth(), b.getIntrinsicHeight());
                Field mDrawable;
                Field mDrawableRef;
                try { mDrawable = ImageSpan.class.getDeclaredField("mDrawable");
                    mDrawable.setAccessible(true);
                    mDrawable.set(WebImageSpan.this, b);
                    mDrawableRef = DynamicDrawableSpan.class.getDeclaredField("mDrawableRef");
                    mDrawableRef.setAccessible(true); mDrawableRef.set(WebImageSpan.this, null);
                    isShow = true;
                    mTextView.setText(mTextView.getText());
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }

            }

        });
        return null;
    }

    private Bitmap zoom(Bitmap bitmap, int w){

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scale = w/width;

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }
}

class DelayDrawable extends Drawable implements Animatable {

    private float mWidth = 60f;
    private float mHeight = 60f;

    private Paint mPaint;
    private Context mContext;

    public DelayDrawable(Context context, String uri){
        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mPaint.setAntiAlias(true);
        this.mContext = context;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        canvas.drawText("IMAGE", 30f, 30f, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getIntrinsicHeight() {
        return (int)mHeight;
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) mWidth;
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
