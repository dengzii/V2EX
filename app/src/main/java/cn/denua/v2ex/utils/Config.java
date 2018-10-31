package cn.denua.v2ex.utils;

import android.content.Context;
import android.content.SharedPreferences;

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

    enum PrefKey{
        UserName, LatestLogin
    }

    public static final String CONFIG_PREF_FILE_NAME = "pref_config";

    public static final HashMap<String, String> BASE_URL = new HashMap<>();
    public static final HashMap<String, String> CONFIG = new HashMap<>();


    public static Account account = new Account();

    public static List<String> HOME_TAB_TITLES = new ArrayList<>();


    static {

        HOME_TAB_TITLES.add("热 门");
        HOME_TAB_TITLES.add("最 新");
        HOME_TAB_TITLES.add("热 门");
        CONFIG.put("username", null);
        CONFIG.put("last_touched", null);

        BASE_URL.put("www.sov2ex.com","https://www.sov2ex.com/");
    }

    public static void persetentUserName(String username){

        SharedPreferences sharedPreferences = App.getApplication().getSharedPreferences(CONFIG_PREF_FILE_NAME, Context.MODE_PRIVATE);
        sharedPreferences.getString(ConfigKey.UserName.name(),null);
    }
}