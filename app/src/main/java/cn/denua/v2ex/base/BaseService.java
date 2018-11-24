/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.base;

import cn.denua.v2ex.interfaces.IResponsibleView;
import cn.denua.v2ex.interfaces.ResponseListener;

/*
 * 所有网络请求服务的父类
 *
 * @author denua
 * @date 2018/10/30 14
 */
public class BaseService<V extends IResponsibleView, T> {

    protected V view;
    private ResponseListener<T> responseListener;

    protected BaseService(){}

    public BaseService(V iResponsibleView){
        this.view = iResponsibleView;
    }

    public BaseService(V iResponsibleView, ResponseListener<T> responseListener){
        this(iResponsibleView);
        this.responseListener = responseListener;
    }

    protected void attachView(V v){
        this.view = v;
    }

    public void detachView(V v){

        this.view = null;
    }

    protected boolean isAttachView(){
        return view != null;
    }

    protected V getView(){
        return view;
    }

    protected void returnFailed(String msg){

        if (view.getContextStatus() == IResponsibleView.VIEW_STATUS_ACTIVATED){
            responseListener.onFailed(msg);
        }
        view.onCompleteRequest();
    }

    protected void returnSuccess(T result){
        if (view.getContextStatus() == IResponsibleView.VIEW_STATUS_ACTIVATED) {
            responseListener.onComplete(result);
        }
        view.onCompleteRequest();
    }

    protected void setResponseListener(ResponseListener<T> responseListener) {
        this.responseListener = responseListener;
    }

    protected void onStartRequest(){
        view.onStartRequest();
    }

    protected void onCompleteRequest(){
        view.onCompleteRequest();
    }
}
