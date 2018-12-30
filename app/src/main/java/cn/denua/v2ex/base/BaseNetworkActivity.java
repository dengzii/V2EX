/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.CallSuper;

import cn.denua.v2ex.interfaces.IResponsibleView;

/*
 * 带网络请求的 Activity base 类
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
    public void onCompleteRequest() {
    }

    @Override
    @CallSuper
    public int getContextStatus() {
        return isInForeground()
                ? VIEW_STATUS_ACTIVATED
                : VIEW_STATUS_ACTIVATED;
    }

    @Override
    public Context getContext() {
        return this;
    }
}
