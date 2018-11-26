/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.api;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.GET;

/*
 *  site api
 *
 * @author denua
 * @date 2018/11/26 10
 */
public interface SiteApi {

    @GET("/api/site/status.json")
    Observable<JsonObject> getStatus();

    @GET("/api/site/info.json")
    Observable<JsonObject> getInfo();


}
