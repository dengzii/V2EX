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

    public static ArrayList<TabEnum> HOME_TAB_TITLES = new ArrayList<TabEnum>(3){{
        add(TabEnum.HOT);
        add(TabEnum.CHANGES);
        add(TabEnum.LATEST);
        add(TabEnum.ALL);
        add(TabEnum.FOLLOWING);
        TabEnum tabEnum = TabEnum.NODE;
        tabEnum.setTitle("sandbox");
        add(tabEnum);
    }};

    public static void init(Context context){

        for (ConfigRefEnum configRefEnum: ConfigRefEnum.values()){
            CONFIG.put(configRefEnum, configRefEnum.getDefaultValue());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T getConfig(ConfigRefEnum key, @Nullable T defaultValue){
        T result = (T) CONFIG.get(key);
        return result==null?defaultValue:result;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T getConfig(ConfigRefEnum key){
        T result = (T) CONFIG.get(key);
        if (result == null){
            result = (T) key.getDefaultValue();
        }
        return result;
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

    public  enum ThemeEnum{

        MAIN_THEME  ("BlueGrey", R.style.MainTheme),
        GREEN_THEME ("Green",   R.style.GreenTheme),
        TEAL_THEME  ("Teal",    R.style.TealTheme),
        ORANGE_THEME("Orange",  R.style.OrangeTheme),
        INDIGO_THEME("Indigo",  R.style.IndigoTheme);

        private String name;
        private int res;
        ThemeEnum(String s, int i) {
            this.name = s;
            this.res = i;
        }
        public static int indexOf(int res){
            switch (res){
                case R.style.MainTheme:
                    return 0;
                case R.style.GreenTheme:
                    return 1;
                case R.style.TealTheme:
                    return 2;
                case R.style.OrangeTheme:
                    return 3;
                case R.style.IndigoTheme:
                    return 4;
                    default:
                        return -1;
            }
        }
        public String getName() {
            return name;
        }
        public int getRes() {
            return res;
        }
    }
}

