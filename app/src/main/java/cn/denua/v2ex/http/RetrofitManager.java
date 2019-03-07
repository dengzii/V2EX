package cn.denua.v2ex.http;

import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.X509TrustManager;

import cn.denua.v2ex.http.converters.BitmapConverterFactory;
import cn.denua.v2ex.http.cookie.CookiesManager;
import cn.denua.v2ex.http.cookie.TransientCookieJar;

import cn.denua.v2ex.utils.HttpsUtil;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/*
 * Retrofit client
 *
 * @user denua
 * @date 2018/10/18
 */
public class RetrofitManager {

    public static boolean DEBUG = false;
    private static final long READ_TIMEOUT = 5000L;
    private static final long WRITE_TIMEOUT = 5000L;
    private static final long CONNECT_TIMEOUT = 5000L;

    private static final String         BASE_URL = "https://www.v2ex.com/";
    private static final List<String>   VERIFIED_HOST = new ArrayList<String>(){{
        add("www.v2ex.com");
        add("cdn.v2ex.com");
        add("*.v2ex.com");
    }};

    private static Retrofit retrofit;
    private static CookiesManager cookiesManager;
    private static Gson sGson;

    private RetrofitManager(){}

    public static Retrofit getRetrofit(){
        if (retrofit == null){
            throw new IllegalStateException("Retrofit need init, call RetrofitManager.init(Context) first.");
        }
        return retrofit;
    }

    public static void init(@Nullable Context context){

        DEBUG = context == null;
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .cookieJar(
                        DEBUG
                        ? new TransientCookieJar()
                        : (cookiesManager = new CookiesManager(context)))
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(HeadersInterceptor.getInstance())
                .hostnameVerifier((hostname, session) -> VERIFIED_HOST.contains(hostname));

        if (!DEBUG){
            X509TrustManager trustManager = HttpsUtil.getX509TrustManager(context);
            okHttpClientBuilder.sslSocketFactory(
                    HttpsUtil.getSslSocketFactory(trustManager),
                    trustManager);
        }
        sGson = new GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .setDateFormat("MM-dd HH:mm")
                .setLenient()
                .create();

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClientBuilder.build())
                .callFactory(okHttpClientBuilder.build());
        if (!DEBUG){
            retrofitBuilder.addConverterFactory(BitmapConverterFactory.create());
        }
        retrofitBuilder.addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(sGson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        retrofit = retrofitBuilder.build();
    }

    public static Gson getGson(){
        return sGson;
    }

    /**
     * 创建 api
     *
     * @param tClass api 对应接口
     * @param <T> 类型
     * @return api
     */
    public static <T> T create(Class<T> tClass){
        if (retrofit == null){
            throw new IllegalStateException("Retrofit need be init, call RetrofitManager.init(Context) first.");
        }
        return retrofit.create(tClass);
    }

    public static void clearCookies(){

        cookiesManager.removeAll();
    }

}