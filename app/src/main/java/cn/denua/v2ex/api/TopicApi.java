package cn.denua.v2ex.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/*
 * 话题相关接口
 *
 * @author denua
 * @date 2018/10/20
 */
public interface TopicApi {

    @GET("t/{id}")
    Observable<String> getTopicDetail(@Path("id") int id, @Query("p") int page);

    @GET("amp/t/{id}/{page}")
    Observable<String> getTopicAmp(@Path("id") int id, @Path("page") int page);

    @GET("/changes")
    Observable<String> getLatestTopic2();

    @GET("/go/{node}")
    Observable<String> getTopicsByNode(@Path("node") String node, @Query("p") int page);

    @GET("/new/{node}")
    Observable<String> getPostTopicPage(@Path("node") String node);

    @GET("api/topics/hot.json")
    Observable<JsonArray> getHotTopic();

    @GET("api/topics/latest.json")
    Observable<JsonArray> getLatestTopic();

    @GET("/api/topics/show.json")
    Observable<JsonArray> getTopic(@Query("id")int id);

    @GET("/api/replies/show.json")
    Observable<JsonArray> getReplies(@Query("topic_id") int id, @Query("p") int page);

    @POST("/down/topic")
    Observable<String> downVote(@Path("id") int id, @Field("t") String v);

    @POST("/up/topic")
    Observable<String> upVote(@Path("id") int id, @Field("t") String v);

    @POST("/api/topics/create.json")
    Observable<JsonObject> createTopic(@FieldMap HashMap<String, String> fields);

    @POST("/new/{node}")
    Observable<String> createTopic(@Path("node") String node,
                                   @Query("title") String title,
                                   @Query("content") String content,
                                   @Query("once") int once,
                                   @Query("syntax") int syntax);

    @POST("/t/{id}")
    Observable<String> postReply(@Path("id") int id,
                                 @Query("once") int once,
                                 @Query("content") String content);
}
