/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import cn.denua.v2ex.model.Account;

/*
 * App 配置相关
 *
 * @user denua
 * @date 2018/10/18
 */
public class Config {

    private static HashMap<ConfigRefEnum, Serializable> CONFIG = new HashMap<>();

    public static final String PREFERENCES_SETTINGS = "preferences_settings";
    public static final String PREFERENCEAS_USET_STATUS = "pref_status";

    public static Account account = new Account();
    public static boolean IsLogin = false;

    public static ArrayList<TabEnum> HOME_TAB_TITLES = new ArrayList<>();

    public static void init(Context context){
        HOME_TAB_TITLES.clear();
        loadConfig(context);
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

    public static void persistentAccount(Context context){

        SharedPreferences.Editor editor= context.getSharedPreferences(
                ConfigRefEnum.KEY_FILE_CONFIG_PREF.getKey(), Context.MODE_PRIVATE).edit();
        String gsonAccount = new Gson().toJson(account);
        editor.putString(ConfigRefEnum.KEY_ACCOUNT.getKey(), gsonAccount);
        editor.apply();
    }

    public static boolean restoreAccount(Context context) {
        try {
            SharedPreferences editor = context.getSharedPreferences(
                    ConfigRefEnum.KEY_FILE_CONFIG_PREF.getKey(), Context.MODE_PRIVATE);
            account = new Gson().fromJson(editor.getString(
                    ConfigRefEnum.KEY_ACCOUNT.getKey(),null), Account.class);
        }catch (Exception e){
            return false;
        }
        return account != null;
    }

    private static void loadConfig(Context context){

        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_SETTINGS,
                Context.MODE_PRIVATE);
        Map<String, ?> pref = preferences.getAll();
        for (ConfigRefEnum refEnum:ConfigRefEnum.values()){
            String key = refEnum.getKey();
            CONFIG.put(refEnum, pref.get(key) != null
                    ? (Serializable) pref.get(key)
                    : refEnum.getDefaultValue());
        }

        Set<String> homeTabs = preferences.getStringSet(ConfigRefEnum.CONFIG_HOME_TAB.getKey(),
                    new TreeSet<String>(){{add(TabEnum.ALL.getTitle());}});
        for (String tab:homeTabs){
            TabEnum tabEnum = TabEnum.contains(tab)
                    ? TabEnum.valueOf(tab)
                    : TabEnum.findByDescriptor(tab);
            HOME_TAB_TITLES.add(tabEnum);
        }
    }
}

