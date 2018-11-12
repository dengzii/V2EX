/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.wiget;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;

import cn.denua.v2ex.R;

/*
 * 消息对话框
 *
 * @author denua
 * @date 2018/11/03 21
 */
public class MessageDialog extends android.support.v7.app.AlertDialog {

    public MessageDialog(Context context){
        super(context);
    }
    public MessageDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    public void showDialog(){
        create();
        show();
    }

    public void setNegativeCallBack(OnClickListener negativeCallBack){

        setButton(AlertDialog.BUTTON_NEGATIVE,
                getContext().getResources().getText(R.string.cancel),
                negativeCallBack);
    }

    public void init(String title, String message, OnClickListener onClickListener){

        setTitle(title);
        setMessage(message);
        setButton(AlertDialog.BUTTON_POSITIVE,
                getContext().getResources().getText(R.string.confirm),
                onClickListener);
    }
}
