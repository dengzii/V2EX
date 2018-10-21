package cn.denua.v2ex.http.converters;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class ConverterFactory extends Converter.Factory {

    private static final BitmapConverter bitmapConverter = new BitmapConverter();
    private static final StringConverter stringConvert = new StringConverter();
    private static final JsonConvert jsonConvert = new JsonConvert();

    private static final ConverterFactory bitmapConverterFactory= new ConverterFactory();

    private ConverterFactory(){}

    public static ConverterFactory getInstance(){
        return bitmapConverterFactory;
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {

        if (type == Bitmap.class)
            return bitmapConverter;
        if (type == String.class)
            return stringConvert;
        if (type == JSONObject.class)
            return jsonConvert;

        return null;
    }
}
