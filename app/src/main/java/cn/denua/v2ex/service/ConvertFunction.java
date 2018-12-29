/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.service;

import io.reactivex.functions.Function;

/*
 * @author denua
 * @email denua@foxmail.com
 * @date 2018/12/29 12
 */
public abstract class ConvertFunction<T, R> implements Function<T, R> {

    private BaseService<?> mBaseService;

    public ConvertFunction(BaseService<?> mBaseService) {
        this.mBaseService = mBaseService;
    }

    @Override
    public R apply(T t) throws Exception {

        if (t == null){
            mBaseService.returnFailed(ErrorEnum.ERR_EMPTY_RESPONSE.getReadable());
        }
        if (t instanceof String
                && ((String) t).contains(ErrorEnum.ERR_PAGE_NEED_LOGIN.getPattern())){
            mBaseService.returnFailed(ErrorEnum.ERR_PAGE_NEED_LOGIN.getReadable());
        }
        return convert(t);
    }

    protected abstract R convert(T t);
}
