package cn.denua.v2ex.api;

import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;

import cn.denua.v2ex.http.RetrofitManager;
import cn.denua.v2ex.service.RxObserver;
import retrofit2.Retrofit;

import static org.junit.Assert.*;

/**
 * cn.denua.v2ex.api
 *
 * @author denua
 * @email denua@foxmail.com
 * @date 2019/3/12
 */
public class SearchApiTest {

    @Before
    public void init(){

        RetrofitManager.init(null);
    }

    @Test
    public void search() {

        RetrofitManager.create(SearchApi.class)
                .search("https://www.sov2ex.com/api/search","v2",1,10,"sumup",1,0,0,"","")
                .subscribe(new RxObserver<JsonObject>() {
                    @Override
                    public void _onNext(JsonObject jsonObject) {
                        System.out.println(jsonObject);
                    }
                });
    }

    @Test
    public void search1() {
    }

    @Test
    public void search2() {
    }
}