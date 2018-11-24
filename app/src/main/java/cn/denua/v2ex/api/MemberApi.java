package cn.denua.v2ex.api;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/*
 * v2ex member api
 *
 * @author denua
 * @date 2018/10/25
 */
public interface MemberApi {

    @GET("api/members/show.json")
    Observable<JsonObject> getMember(@Query("username") String username);

    @GET("api/members/show.json")
    Observable<JsonObject> getMember(@Query("id") int id);

    @GET("/member/{username}")
    Observable<String> getMemberPage(@Path("username") String username);

    @GET("member/{username}/topics")
    Observable<String> getMemberTopics(@Path("username") String username, @Query("p") int page);

    @GET("member/{username}/replies")
    Observable<String> getMemberReplies(@Path("username") String username, @Query("p") int page);

    @GET("member/{username}/qna")
    Observable<String> getMemberQna(@Path("username") String username, @Query("p") int page);
}
