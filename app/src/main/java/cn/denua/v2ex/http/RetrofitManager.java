package cn.denua.v2ex.http;

import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import cn.denua.v2ex.http.cookie.CookiesManager;
import cn.denua.v2ex.http.cookie.TransientCookieJar;

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

    private static final long READ_TIMEOUT = 5000L;
    private static final long WRITE_TIMEOUT = 5000L;
    private static final long CONNECT_TIMEOUT = 5000L;

    private static final String BASE_URL = "https://www.v2ex.com/";

    private static Retrofit retrofit;
    private static CookiesManager cookiesManager;
    private static TransientCookieJar transientCookieJar;

    private RetrofitManager(){}

    public static Retrofit getRetrofit(){
        if (retrofit == null){
            throw new IllegalStateException("Retrofit need init, call RetrofitManager.init(Context) first.");
        }
        return retrofit;
    }

    /**
     * 初始化 retrofit
     *
     * @param context 当前APP上下文
     */
    public static void init(@Nullable Context context){

//        X509TrustManager trustManager = HttpsUtil.getX509TrustManager();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(
                        context == null
                        ? new TransientCookieJar()
                        : (cookiesManager = new CookiesManager(context)))
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(HeadersInterceptor.getInstance())
//                .sslSocketFactory(HttpsUtil.getSslSocketFactory(trustManager), trustManager)
//                .hostnameVerifier((hostname, session) -> true)
//                .followRedirects(false)
//                .followSslRedirects(false)
                .build();

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .setDateFormat("MM-dd HH:mm")
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .callFactory(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
//                .addConverterFactory(BitmapConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     *
     *
     * @param tClass api
     * @param <T> the call api
     * @return call instance
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