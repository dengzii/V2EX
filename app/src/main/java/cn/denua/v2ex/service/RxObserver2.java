package cn.denua.v2ex.service;

import android.support.annotation.CallSuper;

import cn.denua.v2ex.interfaces.IResponsibleView;
import cn.denua.v2ex.interfaces.ResponseListener;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RxObserver2<T> implements Observer<T> {

    private ResponseListener<T> mResponseListener;
    private IResponsibleView mResponsibleView;

    public RxObserver2(IResponsibleView responsibleView, ResponseListener<T> responseListener){
        this.mResponseListener = responseListener;
        this.mResponsibleView = responsibleView;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (isViewReady()) {
            mResponsibleView.onStartRequest();
        }
    }

    @CallSuper
    @Override
    public void onNext(T t) {
        if (isViewReady() && t != null){
            mResponseListener.onComplete(t);
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (isViewReady()){
            mResponseListener.onFailed(e.getMessage());
        }
    }

    @Override
    public void onComplete() {
        if (isViewReady()){
            mResponsibleView.onCompleteRequest();
        }
    }

    private boolean isViewReady(){
        return mResponsibleView.getContextStatus() == IResponsibleView.VIEW_STATUS_ACTIVATED;
    }
}
