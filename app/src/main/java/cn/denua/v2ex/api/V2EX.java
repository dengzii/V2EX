package cn.denua.v2ex.api;

import android.graphics.Bitmap;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.denua.v2ex.http.Client;
import cn.denua.v2ex.utils.V2exUtil;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 注册 /signup   webview 实现
 *
 */

@SuppressWarnings("ALL")
public class V2EX {

    private static final String TAG = "V2EX";
    private static final String URL_BASE = "https://www.v2ex.com/";

    private static String[] fields;

    private static V2exApi v2exApi;

    public static void init(){

        v2exApi = Client.getRetrofit().create(V2exApi.class);
    }

    private V2EX(){

    }

    public static void preLogin(CaptchaLinstener captchaLinstener){

        try {
            Response<String> response = v2exApi.getLoginPage().execute();
            fields = V2exUtil.washLoginFieldName(response.body());
            captchaLinstener.onCaptcha(getCaptcha());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Bitmap getCaptcha() throws IOException {

        return v2exApi.getCaptcha2(fields[3]).execute().body();
    }

    public interface CaptchaLinstener{
        void onCaptcha(Bitmap bitmap);
    }

    public static void login(String account, String password, String checkCode){

        Map<String, String> form= new HashMap<>();

        form.put(fields[0], account);
        form.put(fields[1], password);
        form.put(fields[2], checkCode);
        form.put("once", fields[3]);
        form.put("next", "/");

        try {
            Request request = v2exApi.postLogin(form).request();
            Response<String> response= v2exApi.postLogin(form).execute();

            for (String h :
                    response.headers().names()) {
                Logger.i(h + "--"+ response.headers().get(h));
            }
//            Logger.d(response.body());
            String profile = v2exApi.getMyTopic().execute().body();
            Logger.t("over");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface OnCaptcha{
        void onCaptcha(Bitmap bitmap);
    }
}

interface V2exApi{

    @GET("/")
    Call<String> getHome();

    @GET("signin")
    Call<String> getLoginPage();

    @GET("_captcha")
    Call<ResponseBody> getCaptcha(@Query("once") String once);

    @GET("_captcha")
    Call<Bitmap> getCaptcha2(@Query("once") String once);

    @POST("signin")
    @Headers({"Referer:https://www.v2ex.com/signin",
              "Origin:https://www.v2ex.com"})
    @FormUrlEncoded
    Call<String> postLogin(@FieldMap Map<String, String> form);

    @GET("member/{user}")
    Call<String> getProfile(@Path("user")String user);

    @GET("my/topics")
    Call<String> getMyTopic();
}
