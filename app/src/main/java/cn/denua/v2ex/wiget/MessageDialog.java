/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.wiget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.denua.v2ex.R;

/*
 * 消息对话框
 *
 * @author denua
 * @date 2018/11/03 21
 */
public class MessageDialog extends DialogFragment {

    private String title;
    private String message;

    private CancelListener cancelListener;
    private ConfirmListener confirmListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_message_dialog, container);
        view.findViewById(R.id.tv_cancel).setOnClickListener(v ->{
            if (cancelListener!=null) {
                cancelListener.onCancel();
            }else {
                dismiss();
            }

        });
        view.findViewById(R.id.tv_confirm).setOnClickListener(v -> {
            if (confirmListener!=null)
                confirmListener.onConfirm();
            dismiss();
        });

        ((TextView)view.findViewById(R.id.tv_title)).setText(title);
        ((TextView)view.findViewById(R.id.tv_content)).setText(message);

        return view;
    }
    public interface CancelListener{
        void onCancel();
    }
    public interface ConfirmListener{
        void onConfirm();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCancelListener(CancelListener cancel) {
        this.cancelListener = cancel;
    }
    public void setConfirmListener(ConfirmListener confirmListener){
        this.confirmListener = confirmListener;
    }
}
