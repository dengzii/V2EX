package cn.denua.v2ex.base;

import android.app.Application;

import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import cn.denua.v2ex.Config;
import cn.denua.v2ex.ConfigRefEnum;
import cn.denua.v2ex.http.RetrofitManager;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Account;
import cn.denua.v2ex.service.UserService;


public class App extends Application {

    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        Config.init(this);
        setTheme(getResources().getIdentifier(Config.getConfig(ConfigRefEnum.CONFIG_THEME),
                "style", getPackageName()));
        Logger.addLogAdapter(new AndroidLogAdapter());
        RetrofitManager.init(this);
        Utils.init(this);

        Logger.i("maxMemory" +( Runtime.getRuntime().maxMemory() / 1024 / 1024) + ", ");
    }
    public static Application getApplication(){
        return app;
    }
}
