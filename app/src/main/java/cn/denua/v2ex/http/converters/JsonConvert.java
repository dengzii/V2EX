package cn.denua.v2ex.http.converters;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/*
 * JsonConvert
 *
 * @author denua
 * @date 2018/10/21
 */
public class JsonConvert implements Converter<ResponseBody, JSONObject> {

    @Override
    public JSONObject convert(@NonNull ResponseBody value) throws IOException {
        try {
            return new JSONObject(value.string());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            return new JSONObject("{}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
