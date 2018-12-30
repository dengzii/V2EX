/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.service;

import cn.denua.v2ex.interfaces.IResponsibleView;
import cn.denua.v2ex.interfaces.ResponseListener;

/*
 * 所有网络请求服务的基类
 * 类中关联了
 *
 * @author denua
 * @date 2018/10/30 14
 */
public class BaseService<T> {

    protected IResponsibleView view;
    protected boolean isCanceled = false;
    private ResponseListener<T> responseListener;

    protected BaseService(){}

    public BaseService(IResponsibleView iResponsibleView){
        this.view = iResponsibleView;
    }

    public BaseService(IResponsibleView iResponsibleView, ResponseListener<T> responseListener){
        this(iResponsibleView);
        this.responseListener = responseListener;
    }

    protected void attachView(IResponsibleView v){
        this.view = v;
    }

    public void detachView(IResponsibleView v){

        this.view = null;
    }

    protected boolean isAttachView(){
        return view != null;
    }

    protected IResponsibleView getView(){
        return view;
    }

    protected void returnFailed(ErrorEnum errorEnum){
        returnFailed(errorEnum.getReadable());
    }

    public void returnFailed(String msg){
        if (isCanceled){
            return;
        }
        if (view.getContextStatus() == IResponsibleView.VIEW_STATUS_ACTIVATED){
            responseListener.onFailed(msg);
        }
        view.onCompleteRequest();
    }

    public void returnSuccess(T result){
        if (isCanceled){
            return;
        }
        if (view.getContextStatus() == IResponsibleView.VIEW_STATUS_ACTIVATED) {
            responseListener.onComplete(result);
        }else{
            com.orhanobut.logger.Logger.e(getClass().getName()
                    + "IResponseView doesn't ready.");
        }
        view.onCompleteRequest();
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void cancel(){
        this.isCanceled = true;
    }

    protected void setResponseListener(ResponseListener<T> responseListener) {
        this.responseListener = responseListener;
    }

    protected ResponseListener<T> getResponseListener(){
        return responseListener;
    }

    public void onStartRequest(){
        view.onStartRequest();
    }

    public void onCompleteRequest(){
        view.onCompleteRequest();
        cancel();
    }
}
