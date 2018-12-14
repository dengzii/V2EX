/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.widget;

import android.app.Activity;
import android.app.AlertDialog;

import cn.denua.v2ex.Config;
import cn.denua.v2ex.ConfigRefEnum;
import cn.denua.v2ex.R;

/*
 * @author denua
 * @email denua@foxmail.com
 * @date 2018/12/11 20
 */
public class DialogUtil {


    public static void showThemeDialog(Activity owner){

        AlertDialog.Builder builder = new AlertDialog.Builder(owner);
        builder.setTitle(R.string.theme);
        Config.ThemeEnum[] themeEnum = Config.ThemeEnum.values();
        String[] themes = new String[themeEnum.length];
        for (int i = 0; i < themes.length; i++) {
            themes[i] = themeEnum[i].getName();
        }
        int currentTheme = Config.getConfig(ConfigRefEnum.CONFIG_THEME);
        builder.setSingleChoiceItems(themes, Config.ThemeEnum.indexOf(currentTheme), (dialog, which) -> {
            dialog.dismiss();
            Config.setConfig(ConfigRefEnum.CONFIG_THEME, themeEnum[which].getRes());
            owner.recreate();
        });

        builder.setPositiveButton(R.string.confirm, (d, w) -> d.dismiss());
        builder.setNegativeButton(R.string.cancel, (dialog1, which) -> dialog1.dismiss());
        builder.create().show();
    }

}
