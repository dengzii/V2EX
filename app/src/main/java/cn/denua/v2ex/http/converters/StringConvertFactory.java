package cn.denua.v2ex.http.converters;

import android.support.annotation.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class StringConvertFactory extends Converter.Factory {

    private StringConverter stringConverter = new StringConverter();
    private static StringConvertFactory stringConverterFactory = new StringConvertFactory();

    private StringConvertFactory(){}

    public static StringConvertFactory getInstance(){
        return stringConverterFactory;
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == String.class){
            return stringConverter;
        }
        return null;
    }
}
