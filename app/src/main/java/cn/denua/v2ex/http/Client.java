package cn.denua.v2ex.http;

import android.content.Context;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import cn.denua.v2ex.http.cookie.CookiesManager;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class Client {

    private static final long TIMEOUT = 300L;
    private static final long READ_TIMEOUT = 300L;
    private static final long WRITE_TIMEOUT = 300L;
    private static final long CONNECT_TIMEOUT = 300L;

    private  Retrofit retrofit;
    private  CookiesManager cookiesManager;

    private static Client client;

    private Client(){}

    public static Client getInstance(){
        if ((null==client)){
            synchronized (Client.class){
                if ((null==client))
                    client = new Client();
            }
        }
        return client;
    }

    public void init(Context context){

        cookiesManager = new CookiesManager(context);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(cookiesManager)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
//                .hostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        return false;
//                    }
//                })
//                .sslSocketFactory(Objects.requireNonNull(getSslFactory()))
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .build();

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
