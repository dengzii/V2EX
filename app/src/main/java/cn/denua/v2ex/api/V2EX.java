package cn.denua.v2ex.api;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 注册 /signup   webview 实现
 *
 * 登录 /signin
 * {
 *     'username'
 *     'password'
 *     'checkcode'
 *     next /
 *     once number
 * }
 */

@SuppressWarnings("ALL")
public class V2EX {

    private static final String TAG = "V2EX";
    private static final String URL_BASE = "https://www.v2ex.com/";

    private static Retrofit client;

    public static void init(){

        client = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .build();
    }

    private V2EX(){

    }

    public static void login(){}
    public static void login(String account, String password){

        api api = client.create(cn.denua.v2ex.api.api.class);
        Call<ResponseBody> call = api.getLoginPage();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: "+ call.request().url().toString());
                if (response.isSuccessful()){
                    Document document = null;
                    try {
                        document = Jsoup.parse(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Elements elements = document.select("input");

                    for (Iterator<Element> it = elements.iterator(); it.hasNext(); ) {
                        Element e = it.next();
                        Log.d(TAG, "name: " + e.attr("name") + "val:" + e.attr("value"));
                    }
                }else {
                    Log.e(TAG, "onResponse: " + response.message() + response.code());
                }
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: ");
            }
        });
    }

    public static void main(String[] args) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://www.baidu.com/").build();
        baidu baidu = retrofit.create(cn.denua.v2ex.api.baidu.class);
        Call<ResponseBody> call = baidu.search("hello");
        try {
            Response<ResponseBody> response= call.execute();
            if (response.isSuccessful()){
                System.out.println("main: " + response.body().string());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

interface baidu{

    @GET("s")
    Call<ResponseBody> search(@Query("wd") String keyword);
}
//@SuppressWarnings("ALL")
interface api{

    @GET("signin")
    Call<ResponseBody> getLoginPage();

    @GET("_captcha")
    Call<ResponseBody> getCaptcha(@Query("once") String once);

    @POST
    @FormUrlEncoded
    Call<ResponseBody> postLogin(@FieldMap Map<String, String> form);
}
