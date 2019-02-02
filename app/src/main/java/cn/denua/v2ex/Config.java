/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import cn.denua.v2ex.base.App;
import cn.denua.v2ex.model.Account;

/*
 * App 配置相关
 *
 * @user denua
 * @date 2018/10/18
 */
public class Config {

    private static HashMap<ConfigRefEnum, Serializable> CONFIG = new HashMap<>();

    private static Account sAccount;

    static final ArrayList<TabEnum> HOME_TAB_DEFAULT = new ArrayList<TabEnum>(){{
        add(TabEnum.LATEST);
        add(TabEnum.HOT);
        add(TabEnum.CHANGES);
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
    }
}

