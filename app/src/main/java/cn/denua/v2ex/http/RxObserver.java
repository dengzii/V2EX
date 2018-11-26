package cn.denua.v2ex.http;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class RxObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    public void onNext(T t){
        if (t == null){
            _onError(ErrorEnum.ERR_EMPTY_RESPONSE.getReadable());
        }
        if (t instanceof String
                && ((String) t).contains(ErrorEnum.ERR_PAGE_NEED_LOGIN.getPattern())){
            _onError(ErrorEnum.ERR_PAGE_NEED_LOGIN.getReadable());
        }
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
