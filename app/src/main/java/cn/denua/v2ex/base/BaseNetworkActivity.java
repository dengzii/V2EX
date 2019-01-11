/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

import cn.denua.v2ex.interfaces.IResponsibleView;

/*
 * 带网络请求的 Activity base 类
 *
 * @author denua
 * @date 2018/10/30 15
 */
@SuppressLint("Registered")
public class BaseNetworkActivity extends BaseActivity implements IResponsibleView {

    protected int mViewStatus = VIEW_STATUS_WAITING;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewStatus = VIEW_STATUS_ACTIVATED;
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mViewStatus = VIEW_STATUS_WAITING;
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewStatus = VIEW_STATUS_DESTROYED;
    }

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
        return mViewStatus;
    }

    @Override
    public Context getContext() {
        return this;
    }
}
