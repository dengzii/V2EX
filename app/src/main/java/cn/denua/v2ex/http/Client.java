package cn.denua.v2ex.http;

import android.content.Context;
import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.X509TrustManager;

import cn.denua.v2ex.http.converters.ConverterFactory;
import cn.denua.v2ex.http.cookie.CookiesManager;
import cn.denua.v2ex.http.cookie.TransientCookieJar;
import cn.denua.v2ex.utils.HttpsUtil;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/*
 * Retrofit client
 *
 * @user denua
 * @date 2018/10/18
 */
public class Client {

    private static final long READ_TIMEOUT = 5000L;
    private static final long WRITE_TIMEOUT = 5000L;
    private static final long CONNECT_TIMEOUT = 5000L;

    private static final String BASE_URL = "https://www.v2ex.com/";

    private static Retrofit retrofit;
    private static CookiesManager cookiesManager;
    private static TransientCookieJar transientCookieJar;

    private Client(){}

    public static Retrofit getRetrofit(){
        if (retrofit == null){
            throw new NullPointerException("Retrofit need init, call Client.init(Context) first.");
        }
        return retrofit;
    }

    public static void init(@Nullable Context context){

        X509TrustManager trustManager = HttpsUtil.getX509TrustManager();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(
                        context == null
                        ? new TransientCookieJar()
                        : (cookiesManager = new CookiesManager(context)))
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(HeadersInterceptor.getInstance())
                .sslSocketFactory(HttpsUtil.getSslSocketFactory(trustManager), trustManager)
                .hostnameVerifier((hostname, session) -> true)
//                .followRedirects(false)
//                .followSslRedirects(false)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .callFactory(okHttpClient)
                .addConverterFactory(ConverterFactory.getInstance())
                .build();
    }

    public void clearCookies(){

        cookiesManager.removeAll();
    }

}