package cn.denua.v2ex.api;

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
    Call<String> getTopicDetail(@Query("id") String id);

}
