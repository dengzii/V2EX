package cn.denua.v2ex.base;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import cn.denua.v2ex.Config;
import cn.denua.v2ex.http.RetrofitManager;


public class App extends Application {

    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        Config.init(this);
        Logger.addLogAdapter(new AndroidLogAdapter());
        RetrofitManager.init(this);
        Utils.init(this);
    }

    public static Application getApplication(){
        return app;
    }
}
