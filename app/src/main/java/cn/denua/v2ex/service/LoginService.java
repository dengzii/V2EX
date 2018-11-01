package cn.denua.v2ex.service;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import cn.denua.v2ex.api.LoginApi;
import cn.denua.v2ex.api.MemberApi;
import cn.denua.v2ex.base.BaseService;
import cn.denua.v2ex.http.ResponseHandler;
import cn.denua.v2ex.http.RetrofitManager;
import cn.denua.v2ex.http.RxObserver;
import cn.denua.v2ex.interfaces.IResponsibleView;
import cn.denua.v2ex.interfaces.NextResponseListener;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Account;
import cn.denua.v2ex.utils.HtmlUtil;
import cn.denua.v2ex.utils.RxUtil;
import retrofit2.Call;

/*
 * 用于登录到 v2ex
 *
 * @author denua
 * @date 2018/10/20
 */
public class LoginService<V extends IResponsibleView> extends BaseService<V, Account> {

    private LoginApi loginApi;
    private String[] fieldNames;

    private String username;
    private NextResponseListener<Bitmap, Account> callBack;

    public LoginService(V iResponsibleView, NextResponseListener<Bitmap, Account> loginListener){
        super(iResponsibleView, loginListener);
        callBack = loginListener;
        loginApi = RetrofitManager.create(LoginApi.class);
    }
    public LoginService(V iResponsibleView){
        super(iResponsibleView);
        loginApi = RetrofitManager.create(LoginApi.class);
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
                    returnFailed(!success
                            ?msg
                            :"登录次数过多,该IP已被禁止,一天后尝试或切换IP。");
                    return;
                }
                try{
                    fieldNames = HtmlUtil.washLoginFieldName(result);
                }catch (Exception e){
                    returnFailed(e.getLocalizedMessage());
                    return;
                }
                getCaptcha(fieldNames[3]);
            }
        });

    }

    private void getCaptcha(String once){

        loginApi.getCaptcha(once).enqueue(new ResponseHandler<Bitmap>() {
            @Override
            public void handle(boolean success, Bitmap result, Call<Bitmap> call, String msg) {
                if (success){
                    callBack.onNextResult(result);
                    return;
                }
                returnFailed(msg);
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
                    returnFailed(msg);
                    return;
                }
                if (result.matches("[\\S\\s]+登录有点问题[\\S\\s]+")){
                    returnFailed("验证码或账号信息不正确.");
                    return;
                }
                getInfo(username, callBack);
            }
        });
    }

    /**
     * 从设置页面获取用户信息并返回结果
     *
     * @param un    用户名
     * @param responseListener  请求结果回调接口
     */
    public void getInfo(String un, ResponseListener<Account> responseListener){

        loginApi.getInfo().enqueue(new ResponseHandler<String>() {
            @Override
            public void handle(boolean success, String result, Call<String> call, String msg) {
                if (result.matches("[\\S\\s]+你要查看的页面需要先登录[\\S\\s]+")){
                    responseListener.onFailed("获取用户信息失败, 登录失败.");
                    return;
                }
                if (result.matches("[\\S\\s]+" + un + "[\\S\\s]+")){
                    RetrofitManager.create(MemberApi.class)
                            .getMember(un)
                            .compose(RxUtil.io2main())
                            .subscribe(new RxObserver<JsonObject>() {
                                @Override
                                public void _onNext(JsonObject jsonObject) {
                                    Account account = new Gson().fromJson(jsonObject, Account.class);
                                    responseListener.onComplete(account);
                                }
                                @Override
                                public void _onError(String msg) {
                                    responseListener.onFailed(msg);
                                }
                            });
                    return;
                }
                responseListener.onFailed("登录失败.");
            }
        });
    }
}
