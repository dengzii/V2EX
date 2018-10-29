package cn.denua.v2ex.service;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import cn.denua.v2ex.api.LoginApi;
import cn.denua.v2ex.api.MemberApi;
import cn.denua.v2ex.http.ResponseHandler;
import cn.denua.v2ex.http.RetrofitManager;
import cn.denua.v2ex.http.RxObserver;
import cn.denua.v2ex.model.Account;
import cn.denua.v2ex.model.Member;
import cn.denua.v2ex.utils.HtmlUtil;
import cn.denua.v2ex.utils.RxUtil;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;

/*
 * 用于登录到 v2ex
 *
 * @author denua
 * @date 2018/10/20
 */
public class LoginService {

    private LoginApi loginApi;
    private String[] fieldNames;

    private String username;
    private LoginListener callBack;

    public LoginService(LoginListener loginListener){
        this.callBack = loginListener;
        this.loginApi = RetrofitManager.create(LoginApi.class);
    }
    /**
     * 预登录获取验证码
     *
     */
    public void preLogin(){

        loginApi.getLoginPage().enqueue(new ResponseHandler<String>() {
            @Override
            public void handle(boolean success, String result, Call<String> call, String msg) {

                if (!success || result.matches("[\\S\\s]+登录受限[\\S\\s]+")){
                    callBack.onFailed(!success
                            ?msg
                            :"登录次数过多,该IP已被禁止,一天后尝试或切换IP。");
                    return;
                }
                try{
                    fieldNames = HtmlUtil.washLoginFieldName(result);
                }catch (Exception e){
                    callBack.onFailed(e.getLocalizedMessage());
                    return;
                }
                getCaptcha(fieldNames[3]);
            }
        });

    }

    /**
     * 获取验证码
     *
     * @param once once
     */
    private void getCaptcha(String once){

        loginApi.getCaptcha(once).enqueue(new ResponseHandler<Bitmap>() {
            @Override
            public void handle(boolean success, Bitmap result, Call<Bitmap> call, String msg) {
                if (success){
                    callBack.onCaptcha(result);
                    return;
                }
                callBack.onFailed(msg);
            }
        });
    }
    /**
     * 登录到 v2ex
     *
     * @param account 账号
     * @param password 密码
     * @param checkCode 验证码
     */
    public void login(String account, String password, String checkCode){

        this.username = account;
        Map<String, String> form= new HashMap<>();

        form.put(fieldNames[0], account);
        form.put(fieldNames[1], password);
        form.put(fieldNames[2], checkCode);
        form.put("once", fieldNames[3]);
        form.put("next", "/");

        loginApi.postLogin(form).enqueue(new ResponseHandler<String>() {
            @Override
            public void handle(boolean success, String result, Call<String> call, String msg) {
                if (!success || (null==result)){
                    callBack.onFailed(msg);
                    return;
                }
                if (result.matches("[\\S\\s]+登录有点问题[\\S\\s]+")){
                    callBack.onFailed("验证码或账号信息不正确.");
                    return;
                }
                getSettingsPage();
            }
        });
    }

    /**
     * 从设置页面获取用户信息并返回结果
     *
     */
    private void getSettingsPage(){

        loginApi.getInfo().enqueue(new ResponseHandler<String>() {
            @Override
            public void handle(boolean success, String result, Call<String> call, String msg) {
                if (result.matches("[\\S\\s]+你要查看的页面需要先登录[\\S\\s]+")){
                    callBack.onFailed("获取用户信息失败, 登录失败.");
                    return;
                }
                if (result.matches("[\\S\\s]+" + username + "[\\S\\s]+")){
                    RetrofitManager.create(MemberApi.class)
                            .getMember(username)
                            .compose(RxUtil.io2main())
                            .subscribe(new RxObserver<JsonObject>() {
                                @Override
                                public void _onNext(JsonObject jsonObject) {
                                    Account account = new Gson().fromJson(jsonObject, Account.class);
                                    callBack.onSuccess(account);
                                }
                                @Override
                                public void _onError(String msg) {
                                    callBack.onFailed(msg);
                                }
                            });
                    return;
                }
                callBack.onFailed("登录失败.");
            }
        });

    }
}
