/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.widget;

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
 * 进度框
 *
 * @author denua
 * @date 2018/11/03 20
 */
public class ProgressDialog extends DialogFragment {

    private String title;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_progress_dialog, container);
        ((TextView) view.findViewById(R.id.tv_title)).setText(title);
        view.findViewById(R.id.tv_cancel).setOnClickListener(v -> dismiss());
        return view;
    }
    public void setTitle(String title){
        this.title = title;
    }
}
