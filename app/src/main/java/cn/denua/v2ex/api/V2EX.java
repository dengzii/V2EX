package cn.denua.v2ex.api;

import android.graphics.Bitmap;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.denua.v2ex.http.Client;
import cn.denua.v2ex.utils.HtmlUtil;
import okhttp3.Request;
import retrofit2.Response;

/**
 * 注册 /signup   webview 实现
 *
 */

@SuppressWarnings("ALL")
public class V2EX {

    public static final String ANDROID_HEADER =
            "Mozilla/5.0 (Linux; Android 7.0; PLUS Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.98 Mobile Safari/537.36";
    public static final String PC_HEADER =
            "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0";
    public static final String CLIENT_HEADER =
            "v2ex third-part client for android";


    private static final String TAG = "V2EX";
    private static final String URL_BASE = "https://www.v2ex.com/";

    private static String[] fields;

    private static LoginApi loginApi;

    public static void init(){

        loginApi = Client.getRetrofit().create(LoginApi.class);
    }

    private V2EX(){
    }

    public static void preLogin(CaptchaLinstener captchaLinstener){

        try {
            Response<String> response = loginApi.getLoginPage().execute();
            fields = HtmlUtil.washLoginFieldName(response.body());
            captchaLinstener.onCaptcha(getCaptcha());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Bitmap getCaptcha() throws IOException {

        return loginApi.getCaptcha(fields[3]).execute().body();
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
            Request request = loginApi.postLogin(form).request();
            Response<String> response= loginApi.postLogin(form).execute();

            for (String h :
                    response.headers().names()) {
                Logger.i(h + "--"+ response.headers().get(h));
            }
//            Logger.d(response.body());
            Logger.t("over");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface OnCaptcha{
        void onCaptcha(Bitmap bitmap);
    }
}
