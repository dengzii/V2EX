package cn.denua.v2ex.base;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import cn.denua.v2ex.api.V2EX;
import cn.denua.v2ex.http.Client;

public class App extends Application {

    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        Logger.addLogAdapter(new AndroidLogAdapter());
        Client.init(this);
        V2EX.init();
    }

    public static Application getApplication(){
        return app;
    }
}
