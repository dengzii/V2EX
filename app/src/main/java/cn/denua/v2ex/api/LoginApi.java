package cn.denua.v2ex.api;

import android.graphics.Bitmap;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/*
 * V2EX 登录接口
 *
 * @author denua
 * @date 2018/10/20
 */
public interface LoginApi{

    @GET("signin")
    Call<String> getLoginPage();

    @GET("_captcha")
    Call<Bitmap> getCaptcha(@Query("once") String once);

    @POST("signin")
    @Headers({"Referer:https://www.v2ex.com/signin",
              "Origin:https://www.v2ex.com"})
    @FormUrlEncoded
    Call<String> postLogin(@FieldMap Map<String, String> form);

    @GET("settings")
    Call<String> getInfo();
}
