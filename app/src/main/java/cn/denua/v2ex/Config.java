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
import java.util.List;

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

    public static Account account = new Account();
    public static boolean IsLogin = false;
    public static int sCurrentTheme = R.style.MainTheme;

    public static ArrayList<String> HOME_TAB_TITLES = new ArrayList<String>(3){{
        add("热 门");
        add("最 新");
        add("热 门");
    }};

    public static void init(Context context){

        CONFIG.put(ConfigRefEnum.KEY_USER_NAME, null);
        CONFIG.put(ConfigRefEnum.CONFIG_REPLY_FROM_API, false);
        CONFIG.put(ConfigRefEnum.CONFIG_REPLY_LINE_HEIGHT, 1.3f);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T getConfig(ConfigRefEnum key, @Nullable T defaultValue){
        T result = (T) CONFIG.get(key);
        return result==null?defaultValue:result;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T getConfig(ConfigRefEnum key){
        return (T) CONFIG.get(key);
    }

    public static <T extends Serializable> void setConfig(ConfigRefEnum key, @Nullable T value){

        CONFIG.put(key, value);
    }

    public static void persistentAccount(){

        SharedPreferences.Editor editor= App.getApplication().getSharedPreferences(
                ConfigRefEnum.KEY_FILE_CONFIG_PREF.getKey(), Context.MODE_PRIVATE).edit();
        String gsonAccount = new Gson().toJson(account);
        editor.putString(ConfigRefEnum.KEY_ACCOUNT.getKey(), gsonAccount);
        editor.apply();
    }

    public static boolean restoreAccount() {
        try {
            SharedPreferences editor = App.getApplication().getSharedPreferences(
                    ConfigRefEnum.KEY_FILE_CONFIG_PREF.getKey(), Context.MODE_PRIVATE);
            account = new Gson().fromJson(editor.getString(
                    ConfigRefEnum.KEY_ACCOUNT.getKey(),null), Account.class);
        }catch (Exception e){
            return false;
        }
        return account != null;
    }
}