/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.interfaces;

/*
 * 网络请求结果监听接口
 *
 * @author denua
 * @date 2018/10/30 14
 */
public interface ResponseListener<T> extends FailedListener{
    void onComplete(T result);
}
