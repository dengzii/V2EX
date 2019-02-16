/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import cn.denua.v2ex.base.App;
import cn.denua.v2ex.model.Account;
import cn.denua.v2ex.utils.TimeUtil;

/*
 * App 配置相关
 *
 * @user denua
 * @date 2018/10/18
 */
public class Config {

    private static HashMap<ConfigRefEnum, Serializable> CONFIG = new HashMap<>();

    private static Account sAccount;

    /**
     * 用于配置字体与UI缩放
     */
    private static Configuration mConfig;

    static final ArrayList<TabEnum> HOME_TAB_DEFAULT = new ArrayList<TabEnum>(){{
        add(TabEnum.LATEST);
        add(TabEnum.HOT);
    }};

    public static final ArrayList<Locale> LOCAL_LIST = new ArrayList<Locale>(){{
        add(Locale.CHINA);
        add(Locale.US);
        add(Locale.JAPAN);
    }};

    public static Account getAccount() {
        return sAccount;
    }

    public static void setAccount(Account account){
        sAccount = account;
    }

    /**
     * 初始化配置, 从 SharedPreferences 文件中读取配置
     * @param context the context
     */
    public static void init(Context context){

        loadConfig(context);
        restoreAccount();

        mConfig = context.getResources().getConfiguration();
        if (getConfig(ConfigRefEnum.CONFIG_AUTO_NIGHT_THEME)){
            String[] autoNightThemeTime =
                    ((String)getConfig(ConfigRefEnum.CONFIG_AUTO_NIGHT_TIME)).split("_");
            if (autoNightThemeTime.length == 2 &&
                    TimeUtil.isNowBetweenTimeSpanOfDay(autoNightThemeTime[0], autoNightThemeTime[1])){
                setConfig(ConfigRefEnum.CONFIG_THEME, "DarkTheme");
            }
        }

    }

    public static void saveState(Bundle bundle){

    }

    public static void restoreState(Bundle bundle){

    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T getConfig(ConfigRefEnum key, @Nullable T defaultValue){
        T result = (T) CONFIG.get(key);
        return result == null ? defaultValue : result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getConfig(ConfigRefEnum key){
        T result = (T) CONFIG.get(key);
        if (result == null){
            result = (T) key.getDefaultValue();
        }
        return result;
    }

    public static <T extends Serializable> void setConfig(ConfigRefEnum key, @Nullable T value){

        CONFIG.put(key, value);
    }

    public static Configuration getConfiguretion(){
        return mConfig;
    }
    /**
     * 将用户信息 (Account) 以 json 的形式持久化
     *
     * @param context the context
     */
    public static void persistentAccount(Context context){

        SharedPreferences.Editor editor= context.getSharedPreferences(
                ConfigRefEnum.KEY_FILE_CONFIG_PREF.getKey(), Context.MODE_PRIVATE).edit();
        String gsonAccount = new Gson().toJson(sAccount);
        editor.putString(ConfigRefEnum.KEY_ACCOUNT.getKey(), gsonAccount);
        editor.apply();
    }

    /**
     * 从 SharedPreferences 文件中读取以 json 形式保存的用户信息
     */
    private static void restoreAccount() {
        try {
            SharedPreferences editor = App.getApplication().getSharedPreferences(
                    ConfigRefEnum.KEY_FILE_CONFIG_PREF.getKey(), Context.MODE_PRIVATE);
            sAccount = new Gson().fromJson(editor.getString(
                    ConfigRefEnum.KEY_ACCOUNT.getKey(),null), Account.class);
            if (sAccount == null){
                sAccount = new Account();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private static void loadConfig(Context context){

        SharedPreferences preferences = context.getSharedPreferences(
                Config.getConfig(ConfigRefEnum.CONFIG_PREFERENCE_SETTING_FILE),
                Context.MODE_PRIVATE);
        Map<String, ?> pref = preferences.getAll();
        for (ConfigRefEnum refEnum:ConfigRefEnum.values()){
            String key = refEnum.getKey();
            CONFIG.put(refEnum, pref.get(key) != null
                    ? (Serializable) pref.get(key)
                    : refEnum.getDefaultValue());
        }
        Set<String> homeTabs = preferences.getStringSet(
                ConfigRefEnum.CONFIG_HOME_TAB.getKey(), null);
        if (homeTabs == null){
            CONFIG.put(ConfigRefEnum.CONFIG_HOME_TAB, HOME_TAB_DEFAULT);
            return;
        }
        ArrayList<TabEnum> tabEnums = new ArrayList<>();
        for (String tab:homeTabs){
            TabEnum tabEnum = TabEnum.contains(tab)
                    ? TabEnum.valueOf(tab)
                    : TabEnum.findByDescriptor(tab);
            tabEnums.add(tabEnum);
        }
        CONFIG.put(ConfigRefEnum.CONFIG_HOME_TAB, tabEnums);
        mConfig.fontScale =
                mConfig.fontScale * Float.valueOf(Config.getConfig(ConfigRefEnum.CONFIG_FONT_SCALE));
        mConfig.densityDpi = (int) (mConfig.densityDpi
                * Float.valueOf(Config.getConfig(ConfigRefEnum.CONFIG_UI_SCALE)));
    }
}

