package cn.denua.v2ex.http;

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
            e.printStackTrace();
            onError(e);
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        _onError(e.getLocalizedMessage());
    }

    @Override
    public void onComplete() {

    }

    public abstract void _onNext(T t);
    public abstract void _onError(String msg);
}
