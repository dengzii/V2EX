package cn.denua.v2ex.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.denua.v2ex.model.Account;

/*
 * App 配置相关
 *
 * @user denua
 * @date 2018/10/18
 */
public class Config {

    public static final HashMap<String, String> BASE_URL = new HashMap<>();

    public static Account account = new Account();

    public static List<String> HOME_TAB_TITLES = new ArrayList<String>(){{
        add("热 门");
        add("最 新");
        add("热 门");
    }};

    static {

        BASE_URL.put("www.sov2ex.com","https://www.sov2ex.com/");
    }
}
