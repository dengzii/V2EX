package cn.denua.v2ex.api;

import android.graphics.Bitmap;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import cn.denua.v2ex.http.Client;
import cn.denua.v2ex.utils.V2exUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

    private static V2exApi v2exApi;

    public static void init(){

        v2exApi = Client.getRetrofit().create(V2exApi.class);
    }

    private V2EX(){

    }

    public static void preLogin(){

        Call<String> call = v2exApi.getLoginPage();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Log.d(TAG, "onResponse: "+ call.request().url().toString());
                if (response.isSuccessful()){
                    Document document = Jsoup.parse(response.body());
                    Elements elements = document.select("input");

                    for (Iterator<Element> it = elements.iterator(); it.hasNext(); ) {
                        Element e = it.next();
                        Log.d(TAG, "name: " + e.attr("name") + "val:" + e.attr("value"));
                    }
                }else {
                    Log.e(TAG, "onResponse: " + response.message() + response.code());
                }
            }

            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "onFailure: ");
            }
        });
    }
    public static void login(String account, String password){


    }

    public static void main(String[] args) throws IOException {
        Client.init(null);
        test();
    }

    public static void test() throws IOException {

        V2exApi v2exApi = Client.getRetrofit().create(V2exApi.class);

        Response<String> response = v2exApi.getLoginPage().execute();
        if (!response.isSuccessful()){
//            Log.e(TAG, (response.code() + response.message());
            System.err.println("====");
        }
//        System.out.println(response.body());
        String[] fieldsName = V2exUtil.getLoginFieldNameFromHtml(response.body());
        for (String s:
             fieldsName) {
//            Log.d(TAG, "test: " + s);
            System.out.println(s);
        }

        Response<ResponseBody> captcha = v2exApi.getCaptcha(fieldsName[3]).execute();
//        Log.d(TAG, "test: " + captcha.headers().get("content-length"));
        System.out.println(captcha.headers().get("content-length"));
        System.out.println(captcha.headers().get("content-type"));
        if (!captcha.isSuccessful()){
            Log.e(TAG,captcha.code() + captcha.message());
        }
//        onCaptcha.onCaptcha(captcha.body());
        InputStream inputStream = captcha.body().byteStream();
        File file = new File("D:\\captcha.png");
        file.deleteOnExit();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(bytes)) != -1){
            fileOutputStream.write(bytes, 0, len);
        }
        inputStream.close();
        fileOutputStream.close();

        Map<String, String> form = new HashMap<>();
//        System.out.println("check code:");
        form.put(fieldsName[0], "denua1");
        form.put(fieldsName[1], "vxmm1713.");
        form.put(fieldsName[2], new Scanner(System.in).nextLine());
        form.put("once", fieldsName[3]);
        form.put("next", "/");

        Response<String> login= v2exApi.postLogin(form).execute();
        if (!login.isSuccessful()){
            System.out.println(login.code() + login.message());
        }
        System.out.println(login.body());
    }

    public interface OnCaptcha{
        void onCaptcha(Bitmap bitmap);
    }
}

//@SuppressWarnings("ALL")
interface V2exApi{

    @GET("signin")
    Call<String> getLoginPage();

    @GET("_captcha")
    Call<ResponseBody> getCaptcha(@Query("once") String once);

    @GET("_captcha")
    Call<Bitmap> getCaptcha2(@Query("once") String once);

    @POST("signin")
    @FormUrlEncoded
    Call<String> postLogin(@FieldMap Map<String, String> form);
}
