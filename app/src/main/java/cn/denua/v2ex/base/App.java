package cn.denua.v2ex.base;

import android.app.Application;

import cn.denua.v2ex.api.V2EX;

public class App extends Application {

    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        V2EX.init();
    }

    public static Application getApplication(){
        return app;
    }
}
