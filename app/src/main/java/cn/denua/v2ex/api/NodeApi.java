package cn.denua.v2ex.api;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface NodeApi {

    @GET("api/nodes/show.json")
    Observable<JsonObject> getAllNode();
}
