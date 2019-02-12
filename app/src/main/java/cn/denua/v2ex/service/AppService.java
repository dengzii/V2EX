package cn.denua.v2ex.service;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import cn.denua.v2ex.api.AppApi;
import cn.denua.v2ex.http.RetrofitManager;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.utils.RxUtil;

/**
 * cn.denua.v2ex.service
 *
 * @author denua
 * @email denua@foxmail.com
 * @date 2019/2/10
 */
public class AppService {

    public static void checkNotifycation(String errorLog,ResponseListener<String> responseListener){

        Map<String, String> form = new HashMap<String,String>(){{
            put("version", AppUtils.getAppVersionName() + AppUtils.getAppVersionCode());
            put("deviceModel", DeviceUtils.getModel());
            put("sdkVersion", String.valueOf(DeviceUtils.getSDKVersionCode()));
            put("screen",  ScreenUtils.getScreenDensity() +
                    " " + ScreenUtils.getScreenHeight() +
                    "x" + ScreenUtils.getScreenWidth());
            put("errorLog", errorLog);
        }};
        RetrofitManager.create(AppApi.class)
                .checkNotifycation(form)
                .compose(RxUtil.io2main())
                .subscribe(new RxObserver<JsonObject>() {
                    @Override
                    public void _onNext(JsonObject jsonObject) {

                    }
                });
    }
}
