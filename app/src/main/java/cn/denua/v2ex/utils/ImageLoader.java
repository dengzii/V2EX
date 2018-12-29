/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.module.GlideModule;

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
        new GlideBuilder().setLogLevel(Log.DEBUG);
    }

    public static void load(String url, ImageView imageView, Context context){

        Glide.with(context)
                .load(url)
                .into(imageView)
                .onLoadStarted(defaultImage);
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
