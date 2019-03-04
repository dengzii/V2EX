package cn.denua.v2ex;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import com.blankj.utilcode.util.Utils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

import cn.denua.v2ex.base.BaseActivity;
import cn.denua.v2ex.http.RetrofitManager;
import cn.denua.v2ex.interfaces.Secret;


public class App extends Application implements Application.ActivityLifecycleCallbacks {

    private static App app;
    private static List<Activity> mActivities = new ArrayList<>();
    private Configuration mConfig;
    private Secret mSecretConfig;


    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        mSecretConfig = new SecretImpl();
        mSecretConfig.init(this);

        Config.init(this);
        setTheme(getResources().getIdentifier(Config.getConfig(ConfigRefEnum.CONFIG_THEME),
                "style", getPackageName()));
        Logger.addLogAdapter(new AndroidLogAdapter());
        RetrofitManager.init(this);
        Utils.init(this);

        registerActivityLifecycleCallbacks(this);
        setFontScaleAndUiScale();
    }

    public static Activity getPreActivity(Activity activity){

        return  (mActivities.size() <= 1)
                ? null
                : mActivities.get(mActivities.indexOf(activity) - 1);
    }

    public static Activity getLatestActivity(){
        return (mActivities.size() == 0)
                ? null
                : mActivities.get(mActivities.size() - 1);
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
