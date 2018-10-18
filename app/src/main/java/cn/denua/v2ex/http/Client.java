package cn.denua.v2ex.http;

import android.content.Context;
import android.support.annotation.Nullable;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import cn.denua.v2ex.http.converters.StringConverterFactory;
import cn.denua.v2ex.http.cookie.CookiesManager;
import cn.denua.v2ex.http.cookie.TransientCookieJar;
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

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(
                        context == null
                        ? new TransientCookieJar()
                        : (cookiesManager = new CookiesManager(context)))
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(HeadersInterceptor.instance())
                .followRedirects(false)
                .followSslRedirects(false)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .callFactory(okHttpClient)
//                .addConverterFactory(BitmapConverterFactory.getInstance())
                .addConverterFactory(StringConverterFactory.getInstance())
                .build();
    }

    public void clearCookies(){

        cookiesManager.removeAll();
    }

    private SSLSocketFactory getSslFactory(){


        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

        SSLContext sslContext = null;

        try {
            sslContext = SSLContext.getInstance("SSL");
            X509TrustManager[] trustManagers = new X509TrustManager[]{trustManager};
            sslContext.init(null, trustManagers, new SecureRandom());

        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return sslContext != null ? sslContext.getSocketFactory() : null;
    }
}