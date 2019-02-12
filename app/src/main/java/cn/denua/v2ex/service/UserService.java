package cn.denua.v2ex.service;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.denua.v2ex.api.UserApi;
import cn.denua.v2ex.api.MemberApi;
import cn.denua.v2ex.http.ResponseHandler;
import cn.denua.v2ex.http.RetrofitManager;
import cn.denua.v2ex.interfaces.IResponsibleView;
import cn.denua.v2ex.interfaces.NextResponseListener;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Account;
import cn.denua.v2ex.utils.HtmlUtil;
import cn.denua.v2ex.utils.RxUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import retrofit2.Call;

/*
 * 用于登录到 v2ex
 *
 * @author denua
 * @date 2018/10/20
 */
public class UserService extends BaseService<Account> {

    public static final String STATUS_IP_BANED = "IP在一天内被禁止登录";
    public static final String STATUS_WRONG_FIELDS = "登录字段错误";

    private static UserApi mUserApi = RetrofitManager.create(UserApi.class);
    private static String[] fieldNames;
    private NextResponseListener<Bitmap, Account> callBack;

    public UserService(IResponsibleView iResponsibleView, NextResponseListener<Bitmap, Account> loginListener){
        super(iResponsibleView, loginListener);
        callBack = loginListener;
    }
    public UserService(IResponsibleView iResponsibleView){
        super(iResponsibleView);
    }

    /**
     * 签到
     * 返回的结果为连续签到的天数
     * 结果为负数则表示今日不可签到, 正数则可签到
     *
     * @param isCheck  检查是否已签到
     * @param responseListener 结果监听
     */
    public static void signIn(boolean isCheck, ResponseListener<Integer> responseListener){

        mUserApi.preSignIn()
                .compose(RxUtil.io2computation())
                .flatMap((Function<String, ObservableSource<Integer>>) s -> {

                    ErrorEnum.ERR_PAGE_NEED_LOGIN0.check(s);
                    int once = HtmlUtil.getOnceFromSignInPage(s);
                    int times = HtmlUtil.matcherGroup1Int(Pattern.compile("已连续登录 (\\d+) 天"), s);
                    boolean isSignIn = !HtmlUtil.matcherGroup1(
                            Pattern.compile("(每日登录奖励已领取)"), s).isEmpty();

                    // 如果为检查 签到次数, 则直接返回签到次数与是否可签到
                    if (isCheck){
                        return Observable.just(times * (isSignIn ? 1:-1));
                    }
                    // 如果今日已签到, 则直接返回签到次数
                    if(isSignIn){
                        return Observable.just(times * -1);
                    }else if (once == 0){
                        return Observable.just(Integer.MAX_VALUE);
                    }
                    return mUserApi.signIn(once).map(s1 ->
                            HtmlUtil.matcherGroup1Int(Pattern.compile("已连续登录 (\\d+) 天"), s1));
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<Integer>() {
                    @Override
                    public void _onNext(Integer s) {
                        responseListener.onComplete(s);
                        if (s == Integer.MAX_VALUE){
                            _onError("签到失败");
                        }
                    }
                    @Override
                    public void _onError(String msg) {
                        super._onError(msg);
                        responseListener.onFailed(msg);
                    }
                });
    }

    /**
     * 预登录获取验证码
     *
     */
    public void preLogin(){

        mUserApi.getLoginPage()
                .compose(RxUtil.io2computation())
                .flatMap((Function<String, ObservableSource<Bitmap>>) s -> {
                    ErrorEnum.ERROR_AUTH_IP_BE_BANED.check(s);
                    fieldNames = HtmlUtil.getLoginFieldName(s);
                    return mUserApi.getCaptcha(fieldNames[3]);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<Bitmap>() {
                    @Override
                    public void _onNext(Bitmap bitmap) {
                        callBack.onNextResult(bitmap);
                    }
                    @Override
                    public void _onError(String msg) {
                        super._onError(msg);
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

        Map<String, String> form= new HashMap<>();

        form.put(fieldNames[0], account);
        form.put(fieldNames[1], password);
        form.put(fieldNames[2], checkCode);
        form.put("once", fieldNames[3]);
        form.put("next", "/settings");

        mUserApi.postLogin(form)
                .compose(RxUtil.io2computation())
                .flatMap((Function<String, ObservableSource<Account>>) s -> {
                    ErrorEnum.ERROR_AUTH_LOGIN_PROBLEM.check(s);
                    ErrorEnum.ERROR_AUTH_IP_BE_BANED.check(s);
                    ErrorEnum.ERROR_AUTH_LOGIN_UNKNOWN_PROBLEM.check(s);
                    return getAccount(s);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<Account>() {
                    @Override
                    public void _onNext(Account s) {
                        callBack.onComplete(s);
                    }
                    @Override
                    public void _onError(String msg) {
                        super._onError(msg);
                        returnFailed(msg);
                    }
                });

    }

    /**
     * 从设置页面获取用户信息并返回结果
     *
     * @param responseListener 请求结果回调接口
     */
    public static void getInfo(ResponseListener<Account> responseListener){

        mUserApi.getSettingPage()
                .compose(RxUtil.io2computation())
                .flatMap((Function<String, ObservableSource<Account>>) s -> {
                    ErrorEnum.ERR_PAGE_NEED_LOGIN.check(s);
                    ErrorEnum.ERR_PAGE_NEED_LOGIN0.check(s);
                    return getAccount(s);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<Account>() {
                    @Override
                    public void _onError(String msg) {
                        super._onError(msg);
                        responseListener.onFailed(msg);
                    }
                    @Override
                    public void _onNext(Account account) {
                        responseListener.onComplete(account);
                    }
                });
    }

    /**
     * 尝试从设置页面获取账号， 并返回用于转换的可观察对象
     * 正常情况下，html 为设置页面的内容， 如果未登入，或者登录的时候遇到了问题则抛出异常
     *
     * @param html 获取的 html
     * @return rxjava 观察源
     * @throws VException 可预知的异常
     */
    private static ObservableSource<Account> getAccount(String html) throws VException {

        String username = HtmlUtil.matcherGroup1(
                Pattern.compile("href=\"/member/([^\"]+)\""), html);
        if (username.trim().isEmpty()) ErrorEnum.ERROR_AUTH_LOGIN_UNKNOWN_PROBLEM.throwThis();

        return RetrofitManager.create(MemberApi.class)
                .getMember(username)
                .map(jsonObject -> {
                    Account account = new Gson().fromJson(jsonObject, Account.class);
                    HtmlUtil.attachAccountInfo(account, html);
                    return account;
                });
    }
}
