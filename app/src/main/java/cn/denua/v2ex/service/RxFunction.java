package cn.denua.v2ex.service;

import com.orhanobut.logger.Logger;

import cn.denua.v2ex.interfaces.IResponsibleView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public abstract class RxFunction<T> implements Function<String, ObservableSource<T>> {

    private IResponsibleView mResponseView;
    public RxFunction(IResponsibleView responsibleView){
        this.mResponseView = responsibleView;
    }
    @Override
    public ObservableSource<T> apply(String s) throws VException {

        ErrorEnum.ERR_PAGE_NEED_LOGIN0.check(s);
        if (mResponseView.getContextStatus() != IResponsibleView.VIEW_STATUS_ACTIVATED){
            Logger.e("IResponseView is not activated.");
            return Observable.empty();
        }
        try {
            return apply_(s);
        } catch (Exception e) {
            e.printStackTrace();
            return Observable.create(emitter -> emitter.onError(e));
        }
    }

    abstract ObservableSource<T> apply_(String s) throws Exception;
}
