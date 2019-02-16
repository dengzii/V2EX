package cn.denua.v2ex.base;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import cn.denua.v2ex.Config;
import cn.denua.v2ex.ConfigRefEnum;
import cn.denua.v2ex.http.RetrofitManager;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Account;
import cn.denua.v2ex.service.UserService;


public class App extends Application implements Application.ActivityLifecycleCallbacks {

    private static App app;
    private List<Activity> mActivities = new ArrayList<>();
    private Configuration mConfig;

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
        registerActivityLifecycleCallbacks(this);
        setFontScaleAndUiScale();
    }

    private void setFontScaleAndUiScale(){
        if (mConfig == null){
            mConfig = getResources().getConfiguration();
            System.out.println((String) Config.getConfig(ConfigRefEnum.CONFIG_FONT_SCALE));
            mConfig.fontScale
                    = mConfig.fontScale * Float.valueOf(Config.getConfig(ConfigRefEnum.CONFIG_FONT_SCALE));
            mConfig.densityDpi = (int) (mConfig.densityDpi
                    * Float.valueOf(Config.getConfig(ConfigRefEnum.CONFIG_UI_SCALE)));
        }
        onConfigurationChanged(mConfig);
        getResources().updateConfiguration(mConfig, getResources().getDisplayMetrics());
    }

    public static Application getApplication(){
        return app;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mActivities.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mActivities.remove(activity);
    }
}
