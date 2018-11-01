/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.interfaces;


/*
 * 网络请求的 view 实现此结构
 *
 * @author denua
 * @date 2018/10/30 14
 */
public interface IResponsibleView {

    int VIEW_STATUS_DESTROYED = -1;
    int VIEW_STATUS_ACTIVATED = 1;
    int VIEW_STATUS_WAITING = 0;

    void onStartRequest();
    void onProcessData(int progress);
    void onCompleteRequest();
    int getContextStatus();
}
