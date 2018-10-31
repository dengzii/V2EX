/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.base;


import cn.denua.v2ex.interfaces.IResponsibleView;

/*
 * 网络请求 Fragment 的父类
 *
 * @author denua
 * @date 2018/10/30 14
 */

public class BaseNetworkFragment extends BaseFragment implements IResponsibleView {

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
        return isAdded()
                ?VIEW_STATUS_ACTIVATED
                :isDetached()?VIEW_STATUS_DESTROYED:VIEW_STATUS_WAITING;
    }


}
