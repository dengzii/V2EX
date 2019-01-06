package cn.denua.v2ex.http;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class ErrorInterceptor implements Interceptor {


    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        String url = chain.request().url().toString();

        return chain.proceed(chain.request());
    }

}
