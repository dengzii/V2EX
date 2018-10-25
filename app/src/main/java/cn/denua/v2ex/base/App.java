package cn.denua.v2ex.base;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import cn.denua.v2ex.http.RetrofitManager;


public class App extends Application {

    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        Logger.addLogAdapter(new AndroidLogAdapter());
        RetrofitManager.init(null);
    }

    public static Application getApplication(){
        return app;
    }
}
