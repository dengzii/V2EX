/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.utils;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/*
 * 状态栏工具类
 *
 * @author denua
 * @date 2018/11/04 13
 */
public class StatusBarUtil {


    public static void setStatusBarColor(Activity activity, int color){

        Window window = activity.getWindow();
        window.setStatusBarColor(color);
    }

    public static void setTranslucentStatusBar(Activity activity){
        Window window = activity.getWindow();
        View view = window.getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        view.setSystemUiVisibility(option);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);
    }

    public static void addStatusViewWithColor(Activity activity, int color) {
        ViewGroup contentView =
                (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        View statusBarView = new View(activity);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setBackgroundColor(color);
        contentView.addView(statusBarView, layoutParams);
    }

    public static void fitSystemWindow(Activity activity){
        ViewGroup viewGroup = (ViewGroup) activity.findViewById(android.R.id.content);
        if (viewGroup.getChildCount()>0){
            ViewGroup root = (ViewGroup) viewGroup.getChildAt(0);
            if (root != null) root.setFitsSystemWindows(true);
        }
    }

    public static void hideActionBar(Activity activity){
        ActionBar actionBar = activity.getActionBar();
        if (actionBar != null)
            actionBar.hide();
    }

    public static int getStatusBarHeight(Context context){

        int resId = context.getResources().getIdentifier( "status_bar_height", "dimen", "android");
        if (resId > 0){
            return context.getResources().getDimensionPixelSize(resId);
        }
        return -1;
    }
}
