package cn.denua.v2ex.http.converters;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class StringConverter implements Converter<ResponseBody, String> {

    @Override
    public String convert(@NonNull ResponseBody value) throws IOException {
        return value.string();
    }
}
