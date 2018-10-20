package cn.denua.v2ex.http;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;

public class HeadersInterceptor implements Interceptor{

    private static final HeadersInterceptor interceptor = new HeadersInterceptor();

    public static HeadersInterceptor getInstance(){
        return interceptor;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Request originRequest = chain.request();
        Request updateRequest = originRequest.newBuilder()
                .addHeader("host", originRequest.url().host())
                .addHeader("upgrade-insecure-requests", "1")
                .addHeader("DNT", "1")
                .addHeader("TE","Trailers")
                .addHeader("Cache-Control", "na-cache")
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2")
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
