/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import cn.denua.v2ex.Config;
import cn.denua.v2ex.ConfigRefEnum;
import cn.denua.v2ex.R;
import cn.denua.v2ex.ThemeEnum;

/*
 * @author denua
 * @email denua@foxmail.com
 * @date 2018/12/11 20
 */
public class DialogUtil {


    public static void showThemeDialog(Context context, OnSelectItemListener<Integer> onSelectItemListener){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.theme);
        ThemeEnum[] themeEnum = ThemeEnum.values();
        String[] themes = new String[themeEnum.length];
        for (int i = 0; i < themes.length; i++) {
            themes[i] = themeEnum[i].getName();
        }
        int currentTheme = Config.getConfig(ConfigRefEnum.CONFIG_THEME);
        builder.setSingleChoiceItems(themes, ThemeEnum.getByThemeRes(currentTheme).getIndex(),
                (dialog, which) -> {
            dialog.dismiss();
            int selected =  themeEnum[which].getRes();
            Config.setConfig(ConfigRefEnum.CONFIG_THEME,  selected);
            onSelectItemListener.onSelectItem( selected);
        });

        builder.setPositiveButton(R.string.confirm, (d, w) -> d.dismiss());
        builder.setNegativeButton(R.string.cancel, (dialog1, which) -> dialog1.dismiss());
        builder.create().show();
    }

    public static void showDialogWithList(Context context,
                                              String[] arr,
                                              int defaultVal,
                                              String title,
                                              OnSelectItemListener<Integer> onSelectItemListener){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setSingleChoiceItems(arr, defaultVal, (dialog, which) -> {
            onSelectItemListener.onSelectItem(which);
            dialog.dismiss();
        });
        builder.setPositiveButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    public interface OnSelectItemListener<T>{
        void onSelectItem(T value);
    }
}
