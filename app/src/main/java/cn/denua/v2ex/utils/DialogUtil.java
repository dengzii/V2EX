/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.utils;

import android.app.AlertDialog;
import android.content.Context;

import cn.denua.v2ex.R;

/*
 * @author denua
 * @email denua@foxmail.com
 * @date 2018/12/11 20
 */
public class DialogUtil {

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
