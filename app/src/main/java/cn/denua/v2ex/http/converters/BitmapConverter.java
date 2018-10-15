package cn.denua.v2ex.http.converters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class BitmapConverter implements Converter<ResponseBody, Bitmap> {

    @Override
    public Bitmap convert(@NonNull ResponseBody value) throws IOException {
        return BitmapFactory.decodeStream(value.byteStream());
    }
}
