package cn.denua.v2ex.api;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/*
 * v2ex member api
 *
 * @author denua
 * @date 2018/10/25
 */
public interface MemberApi {


    @GET("api/members/show.json")
    Observable<JsonObject> getMemberDetail(@Query("username") String username);

    @GET("api/members/show.json")
    Observable<JsonObject> getMemberDetail(@Query("id") int id);
}
