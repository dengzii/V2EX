/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
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
        loadConfig(context);
    }
    private static String resolveValue(XmlResourceParser xmlResourceParser){
        if (xmlResourceParser.getName().equals("string")){
            String name = xmlResourceParser.getAttributeValue(0);
            String text = xmlResourceParser.getText();
            System.out.print(name);
            return text;
        }
        return "-";
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

    public static void loadConfig(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_SETTINGS,
                Context.MODE_PRIVATE);
        Map<String, ?> pref = preferences.getAll();
        for (ConfigRefEnum refEnum:ConfigRefEnum.values()){
            String key = refEnum.getKey();
            if (pref.get(key)!=null){
                CONFIG.put(refEnum, (Serializable) pref.get(key));
            }
            CONFIG.put(refEnum,  refEnum.getDefaultValue());
            System.out.println(refEnum + "\tkey: " + refEnum.getKey() + "\tvalue: " + pref.get(key));
        }

        Set<String> homeTabs = preferences.getStringSet(ConfigRefEnum.CONFIG_HOME_TAB.getKey(),
                    new TreeSet<String>(){{add(TabEnum.ALL.getTitle());}});

    }
}

