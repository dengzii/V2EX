/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

import cn.denua.v2ex.R;

/*
 * load image into imageView
 *
 * @author denua
 * @date 2018/11/09 19
 */
public class ImageLoader {

    private static Drawable defaultImage;

    public static void init(Context context){

        defaultImage = context.getDrawable(R.drawable.ic_offline);
        RequestOptions requestOptions = new RequestOptions();
    }

    public static void load(String url, ImageView target, Context context){

        Glide.with(context)
                .load(url)
                .into(new DrawableImageViewTarget(target){

                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        target.setImageDrawable(defaultImage);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        super.onLoadCleared(placeholder);
                        target.setImageDrawable(defaultImage);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        target.setImageDrawable(defaultImage);
                    }
                });
    }

    public static void load(String url, ImageView imageView, View view){

        Glide.with(view).load(url).into(imageView);
    }

    private class MGlideModule implements GlideModule{
        @Override
        public void applyOptions(Context context, GlideBuilder builder) {

        }

        @Override
        public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {

        }
    }
}
