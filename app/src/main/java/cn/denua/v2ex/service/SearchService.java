package cn.denua.v2ex.service;

import cn.denua.v2ex.api.SearchApi;
import cn.denua.v2ex.http.RetrofitManager;

/*
 * Search the v2ex
 *
 * @author denua
 * @date 2018/10/21
 */
public class SearchService {

    private SearchApi searchApi;

    public SearchService(){
        searchApi = RetrofitManager.create(SearchApi.class);
    }

    public void search(String keywords){

    }

}
