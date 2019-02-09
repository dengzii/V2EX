package cn.denua.v2ex.api;

import android.graphics.Bitmap;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Query;

/*
 * V2EX 登录接口
 *
 * @author denua
 * @date 2018/10/20
 */
public interface UserApi {

    @GET("signin")
    Observable<String> getLoginPage();

    @GET("_captcha")
    Observable<Bitmap> getCaptcha(@Query("once") String once);

    @POST("signin")
    @Headers({"Referer:https://www.v2ex.com/signin",  "Origin:https://www.v2ex.com"})
    @FormUrlEncoded
    Observable<String> postLogin(@FieldMap Map<String, String> form);

    @GET("settings")
    Observable<String> getSettingPage();

    @GET("/mission/daily")
    Observable<String> preSignIn();
    @GET("/mission/daily/redeem")
    Observable<String> signIn(@Query("once") int once);
}
