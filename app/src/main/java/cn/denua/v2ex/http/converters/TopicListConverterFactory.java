package cn.denua.v2ex.http.converters;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import cn.denua.v2ex.model.Topic;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/*
 * cn.denua.v2ex.converter
 *
 * @author denua
 * @email denua@foxmail.com
 * @date 2019/1/13
 */
public class TopicListConverterFactory extends Converter.Factory {

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations,
                                                          Retrofit retrofit) {

        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }

    static class TopicConverter implements Converter<ResponseBody, List<Topic>>{

        static TopicConverter INSTANCE = new TopicConverter();

        @Override
        public List<Topic> convert(@NonNull ResponseBody value) throws IOException {

            return null;
        }
    }
}
