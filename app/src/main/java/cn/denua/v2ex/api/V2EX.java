package cn.denua.v2ex.api;

import android.graphics.Bitmap;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import cn.denua.v2ex.http.Client;
import cn.denua.v2ex.utils.V2exUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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

            Response<String> response= v2exApi.postLogin(form).execute();

            for (String h :
                    response.headers().names()) {
                Logger.i(h + "--"+ response.headers().get(h));
            }
//            Logger.d(response.body());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Client.init(null);
        test1();
    }
     public static void test1() throws IOException {

        V2exApi v2exApi = Client.getRetrofit().create(V2exApi.class);
        Response<String> res = v2exApi.getMyTopic().execute();
         System.out.println(res.body());
    }

    public static void test() throws IOException {

        V2exApi v2exApi = Client.getRetrofit().create(V2exApi.class);

        Response<String> response = v2exApi.getLoginPage().execute();
        if (!response.isSuccessful()){
//            Log.e(TAG, (response.code() + response.message());
            System.err.println("====");
        }
//        System.out.println(response.body());
        String[] fieldsName = V2exUtil.washLoginFieldName(response.body());

        Response<ResponseBody> captcha = v2exApi.getCaptcha(fieldsName[3]).execute();

        InputStream inputStream = captcha.body().byteStream();
        File file = new File("D:\\captcha.png");
        file.deleteOnExit();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(bytes)) != -1){
            fileOutputStream.write(bytes, 0, len);
        }
        inputStream.close();
        fileOutputStream.close();

        Map<String, String> form = new HashMap<>();

        System.out.println("CheckCode:");
        form.put(fieldsName[0], "denua1");
        form.put(fieldsName[1], "vxmm1713.");
        form.put(fieldsName[2], new Scanner(System.in).nextLine());
        form.put("once", fieldsName[3]);
        form.put("next", "/");

        System.out.println("\n \t THE REQUEST FORM:");
        for (Iterator<Map.Entry<String, String>> it = form.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, String> field = it.next();
            System.out.println(field.getKey() + ":" + field.getValue());
        }
        System.out.println("\t \t END\n");

        Response<String> login= v2exApi.postLogin(form).execute();
        if (!login.isSuccessful()){
            System.out.println(login.code() + login.message());
        }
        Iterator<String> h = login.headers().names().iterator();
        for (Iterator<String> it = h; it.hasNext(); ) {
            String s = it.next();
            System.out.println(s + "===" + login.headers().get(s));
        }

//        System.out.println(login.body());
        System.out.println("V2EX.test" + login.code() + login.message());

        Response<String> home = v2exApi.getMyTopic().execute();
        System.out.println(home.code() + home.body());
    }

    public interface OnCaptcha{
        void onCaptcha(Bitmap bitmap);
    }
}

//@SuppressWarnings("ALL")
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
    @FormUrlEncoded
    Call<String> postLogin(@FieldMap Map<String, String> form);

    @GET("member/{user}")
    Call<String> getProfile(@Path("user")String user);

    @GET("my/topics")
    Call<String> getMyTopic();
}
