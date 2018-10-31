/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.base;

import android.annotation.SuppressLint;

import cn.denua.v2ex.interfaces.IResponsibleView;

/*
 *
 *
 * @author denua
 * @date 2018/10/30 15
 */
@SuppressLint("Registered")
public class BaseNetworkActivity extends BaseActivity implements IResponsibleView {

    @Override
    public void onStartRequest() {

    }

    @Override
    public void onProcessData(int progress) {

    }

    @Override
    public void onRequestComplete() {
    }

    @Override
    public int getContextStatus() {
        return 0;
    }
}
