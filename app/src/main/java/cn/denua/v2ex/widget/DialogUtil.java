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
        builder.setSingleChoiceItems(new String[]{"BlueGrey", "Green", "Teal", "Indigo", "Orange"}, -1, (dialog, which) -> {
            dialog.dismiss();
            switch (which){
                case 0:
                    Config.setConfig(ConfigRefEnum.CONFIG_THEME, R.style.MainTheme);
                    break;
                case 1:
                    Config.setConfig(ConfigRefEnum.CONFIG_THEME, R.style.GreenTheme);
                    break;
                case 2:
                    Config.setConfig(ConfigRefEnum.CONFIG_THEME, R.style.TealTheme);
                    break;
                case 3:
                    Config.setConfig(ConfigRefEnum.CONFIG_THEME, R.style.IndigoTheme);
                    break;
                case 4:
                    Config.setConfig(ConfigRefEnum.CONFIG_THEME, R.style.OrangeTheme);
                    break;
                    default:break;
            }
            owner.recreate();
        });

        builder.setPositiveButton(R.string.confirm, (d, w) -> d.dismiss());
        builder.setNegativeButton(R.string.cancel, (dialog1, which) -> dialog1.dismiss());
        builder.create().show();
    }

}
