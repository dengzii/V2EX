package cn.denua.v2ex.http.converters;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class BitmapConverterFactory extends Converter.Factory {

    private static final BitmapConverter converter = new BitmapConverter();
    private static final BitmapConverterFactory bitmapConverterFactory= new BitmapConverterFactory();

    private BitmapConverterFactory(){}

    public static BitmapConverterFactory getInstance(){
        return bitmapConverterFactory;
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == Bitmap.class){
            return converter;
        }
        return null;
    }
}
