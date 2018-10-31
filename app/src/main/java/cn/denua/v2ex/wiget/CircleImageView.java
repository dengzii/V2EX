/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.wiget;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/*
 * 圆形的 ImageView
 *
 * @author denua
 * @date 2018/10/31 12
 */
public class CircleImageView extends Drawable {

    private Bitmap bitmap;

    @Override
    public void draw(@NonNull Canvas canvas) {

    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}
