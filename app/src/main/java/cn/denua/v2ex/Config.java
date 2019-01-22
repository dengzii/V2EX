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
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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

    private static Account sAccount = new Account();

    /**
     * 负数表示今日已签到 <br>
     * 0 表示未连续签到 <br>
     * 正数表示今日未签到 <br>
     */
    public static int sSignIn = 0;

    private static ArrayList<TabEnum> HOME_TAB_TITLES = new ArrayList<>();

    static final ArrayList<TabEnum> HOME_TAB_DEFAULT = new ArrayList<TabEnum>(){{
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
     * @return 是否恢复账号信息成功
     */
    public static boolean restoreAccount() {
        try {
            SharedPreferences editor = App.getApplication().getSharedPreferences(
                    ConfigRefEnum.KEY_FILE_CONFIG_PREF.getKey(), Context.MODE_PRIVATE);
            sAccount = new Gson().fromJson(editor.getString(
                    ConfigRefEnum.KEY_ACCOUNT.getKey(),null), Account.class);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return sAccount != null;
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
        HOME_TAB_TITLES.clear();
        Set<String> homeTabs = preferences.getStringSet(ConfigRefEnum.CONFIG_HOME_TAB.getKey(),
                    new TreeSet<String>(){{add(TabEnum.ALL.getTitle());}});
        for (String tab:homeTabs){
            TabEnum tabEnum = TabEnum.contains(tab)
                    ? TabEnum.valueOf(tab)
                    : TabEnum.findByDescriptor(tab);
            HOME_TAB_TITLES.add(tabEnum);
        }
        CONFIG.put(ConfigRefEnum.CONFIG_HOME_TAB, HOME_TAB_TITLES);
    }
}

