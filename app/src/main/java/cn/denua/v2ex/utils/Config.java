package cn.denua.v2ex.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

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


    public static final String FILE_CONFIG_PREF = "pref_config";
    public static final String DIR_USER_PERSISTENT = "data";
    public static final String FILE_USER_PERSISTENT = "user_status";

    public static final HashMap<String, String> BASE_URL = new HashMap<>();
    public static final HashMap<String, String> CONFIG = new HashMap<>();

    public static Account account = new Account();
    public static boolean IsLogin = false;

    public static List<String> HOME_TAB_TITLES = new ArrayList<>();


    static {

        HOME_TAB_TITLES.add("热 门");
        HOME_TAB_TITLES.add("最 新");
        HOME_TAB_TITLES.add("热 门");

        CONFIG.put("username", null);
        CONFIG.put("last_touched", null);

        BASE_URL.put("www.sov2ex.com","https://www.sov2ex.com/");
    }

    public static void persistentAccount(){

        SharedPreferences.Editor editor= App.getApplication().getSharedPreferences(FILE_CONFIG_PREF, Context.MODE_PRIVATE).edit();
        String gsonAccount = new Gson().toJson(account);
        editor.putString("saved_status", gsonAccount);
        editor.apply();
    }

    public static boolean restoreAccount() {

        SharedPreferences editor = App.getApplication().getSharedPreferences(FILE_CONFIG_PREF, Context.MODE_PRIVATE);
        account = new Gson().fromJson(editor.getString("saved_status",null), Account.class);
        return account != null;
    }
}