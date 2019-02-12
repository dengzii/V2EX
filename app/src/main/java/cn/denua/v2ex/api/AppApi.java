package cn.denua.v2ex.api;

import com.google.gson.JsonObject;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * cn.denua.v2ex.api
 *
 * @author denua
 * @email denua@foxmail.com
 * @date 2019/2/10
 */
public interface AppApi {

    @POST("/v2ex/app/")
    @Headers({"base-url:https://v2ex.denua.cn"})
    Observable<JsonObject> checkNotifycation(@QueryMap Map<String, String> form);
}
