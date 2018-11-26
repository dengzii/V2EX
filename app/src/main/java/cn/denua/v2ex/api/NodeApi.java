package cn.denua.v2ex.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NodeApi {

    @GET("api/nodes/show.json")
    Observable<JsonObject> getNode(@Query("name")String name);

    @GET("/api/nodes/all.json")
    Observable<JsonArray> getAllNode();
}
