package cn.denua.v2ex.http.converters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class BitmapConverterFactory extends Converter.Factory{

    private static final BitmapConverterFactory INSTANCE = new BitmapConverterFactory();

    public static BitmapConverterFactory create(){
        return INSTANCE;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == Bitmap.class)
            return BitmapConverter.INSTANCE;
        return null;
    }

    static class BitmapConverter implements Converter<ResponseBody, Bitmap>{

        private static final BitmapConverter INSTANCE = new BitmapConverter();

        @Override
        public Bitmap convert(@NonNull ResponseBody value) throws IOException {
            try{
                return BitmapFactory.decodeStream(value.byteStream());
            }finally {
                value.close();
            }
        }

    }
}

