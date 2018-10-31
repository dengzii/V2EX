/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.interfaces;

/*
 * 连续两次请求回调接口
 *
 * @author denua
 * @date 2018/10/30 15
 */
public interface NextResponseListener<N, T> extends ResponseListener<T> {
    void onNextResult(N next);
}
