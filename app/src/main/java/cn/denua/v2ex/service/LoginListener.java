package cn.denua.v2ex.service;

import android.graphics.Bitmap;

import cn.denua.v2ex.model.AccountModel;

/*
 * 登录状态监听
 *
 * @author denua
 * @date 2018/10/20
 */
public interface LoginListener {
    void onCaptcha(Bitmap bitmap);
    void onFailed(String msg);
    void onSuccess(AccountModel accountModel);
}
