package cn.denua.v2ex.http.converters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class BitmapConverter implements Converter<ResponseBody, Bitmap> {

    private static final BitmapConverter INSTANCE = new BitmapConverter();

    @Override
    public Bitmap convert(@NonNull ResponseBody value) throws IOException {
        return BitmapFactory.decodeStream(value.byteStream());
    }

    public static class Factory extends Converter.Factory{

        private static final Factory INSTANCE = new Factory();

        public static Factory create(){
            return INSTANCE;
        }

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            if (type == Bitmap.class)
                return BitmapConverter.INSTANCE;
            return null;
        }
    }
}
