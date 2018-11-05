package cn.denua.v2ex.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/*
 * 话题相关接口
 *
 * @author denua
 * @date 2018/10/20
 */
public interface TopicApi {

    @GET("t/{id}")
    Observable<String> getTopicDetail(@Query("id") int id, @Query("p") int page);

    @GET("api/topics/hot.json")
    Observable<JsonArray> getHotTopic();

    @GET("api/topics/latest.json")
    Observable<JsonArray> getLatestTopic();
}
