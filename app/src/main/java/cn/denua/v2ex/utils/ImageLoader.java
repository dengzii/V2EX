/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cn.denua.v2ex.R;

/*
 * load image into imageView
 *
 * @author denua
 * @date 2018/11/09 19
 */
public class ImageLoader {

    public static void init(Context context){

    }

    public static void load(String url, ImageView imageView, Context context){

        Glide.with(context)
                .load(url)
                .into(imageView)
                .onLoadFailed(context.getDrawable(R.drawable.ic_offline));
    }

    public static void load(String url, ImageView imageView, View view){

        Glide.with(view).load(url).into(imageView);
    }

}
