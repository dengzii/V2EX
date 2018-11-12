package cn.denua.v2ex.service;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static final String STATUS_NEED_LOGIN = "未登录";
    public static final String STATUS_IP_BANED = "IP在一天内被禁止登录";
    public static final String STATUS_GET_INFO_FAILED = "获取用户信息失败";
    public static final String STATUS_WRONG_FIELDS = "登录字段错误";
    public static final String STATUS_EMPTY_RESPONSE_BODY = "响应体为空";


    private LoginApi loginApi;
    private String[] fieldNames;
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
                    callBack.onFailed(!success ? msg : STATUS_IP_BANED);
                    return;
                }
                try{
                    fieldNames = HtmlUtil.getLoginFieldName(result);
                }catch (Exception e){
                    callBack.onFailed(e.getLocalizedMessage());
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
                if (result.matches("[\\S\\s]+登录受限[\\S\\s]+")){
                    callBack.onFailed(STATUS_IP_BANED);
                    return;
                }
                if (result.matches("[\\S\\s]+登录有点问题[\\S\\s]+")){
                    callBack.onFailed(STATUS_WRONG_FIELDS);
                    return;
                }
                getInfo(callBack);
            }
        });
    }

    /**
     * 从设置页面获取用户信息并返回结果
     *
     * @param responseListener 请求结果回调接口
     */
    public void getInfo(ResponseListener<Account> responseListener){

        loginApi.getInfo().enqueue(new ResponseHandler<String>() {
            @Override
            public void handle(boolean success, String result, Call<String> call, String msg) {
                if (!success){
                    responseListener.onFailed(msg);
                    return;
                }
                if (result == null){
                    responseListener.onFailed(STATUS_EMPTY_RESPONSE_BODY);
                    return;
                }
                if (result.matches("[\\S\\s]+你要查看的页面需要先登录[\\S\\s]+")){
                    responseListener.onFailed(STATUS_NEED_LOGIN);
                    return;
                }
                Matcher matcher = Pattern.compile("href=\"/member/([^\"]+)\"").matcher(result);
                if (matcher.find()){
                    RetrofitManager.create(MemberApi.class)
                            .getMember(matcher.group(1))
                            .compose(RxUtil.io2main())
                            .subscribe(new RxObserver<JsonObject>() {
                                @Override
                                public void _onNext(JsonObject jsonObject) {
                                    Account account = new Gson().fromJson(jsonObject, Account.class);
                                    HtmlUtil.attachAccountInfo(account, result);
                                    responseListener.onComplete(account);
                                }
                                @Override
                                public void _onError(String msg) {
                                    responseListener.onFailed(msg);
                                }
                            });
                    return;
                }
                responseListener.onFailed(STATUS_GET_INFO_FAILED);
            }
        });
    }
}
