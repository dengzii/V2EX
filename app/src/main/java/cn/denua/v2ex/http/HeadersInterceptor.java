package cn.denua.v2ex.http;

import android.support.annotation.NonNull;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;

/*
 * 给所有请求添加一个 host header
 *
 * @author denua
 */
public class HeadersInterceptor implements Interceptor{

    private static final HeadersInterceptor interceptor = new HeadersInterceptor();

    public static HeadersInterceptor getInstance(){
        return interceptor;
    }

    private HeadersInterceptor(){

    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Request originRequest = chain.request();
        System.out.println("OkHttpInterceptor: " + originRequest.method() + " " + originRequest.url().toString());
        Request updateRequest = originRequest.newBuilder()
                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0")
                .addHeader("host", originRequest.url().host())
                .build();
        return chain.proceed(updateRequest);
    }

    private RequestBody gzip(final RequestBody body) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return body.contentType();
            }

            @Override
            public long contentLength() throws IOException {
                return -1;
            }

            @Override
            public void writeTo(@NonNull BufferedSink sink) throws IOException {
                BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                body.writeTo(gzipSink);
                gzipSink.close();
            }
        };
    }
}
