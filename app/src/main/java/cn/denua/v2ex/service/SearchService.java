package cn.denua.v2ex.service;

import org.json.JSONObject;

import cn.denua.v2ex.api.SearchApi;
import cn.denua.v2ex.http.Client;
import cn.denua.v2ex.http.ResponseHandler;
import retrofit2.Call;

/*
 * Search the v2ex
 *
 * @author denua
 * @date 2018/10/21
 */
public class SearchService {

    private SearchApi searchApi;

    public SearchService(){
        searchApi = Client.getRetrofit().create(SearchApi.class);
    }

    public void search(String keywords){

        searchApi.search("", "",0, 30, "", 0, 0,0,"","").enqueue(new ResponseHandler<JSONObject>() {
            @Override
            public void handle(boolean success, JSONObject result, Call<JSONObject> call, String msg) {

            }
        });
    }

}
