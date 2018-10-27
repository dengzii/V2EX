package cn.denua.v2ex.http;

import io.reactivex.Flowable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class RxObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    public void onNext(T t){
        try {
            _onNext(t);
        }catch (Exception e){
            onError(e);
        }
    }

    @Override
    public void onError(Throwable e) {
        _onError(e.getLocalizedMessage() + "\n" + e.getMessage());
    }

    @Override
    public void onComplete() {

    }

    public abstract void _onNext(T t);
    public abstract void _onError(String msg);
}
