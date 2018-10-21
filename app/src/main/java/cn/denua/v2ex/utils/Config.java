package cn.denua.v2ex.utils;

import java.util.HashMap;

/*
 * App 配置相关
 *
 * @user denua
 * @date 2018/10/18
 */
public class Config {

    public static final HashMap<String, String> BASE_URL = new HashMap<>();

    static {

        BASE_URL.put("www.sov2ex.com","https://www.sov2ex.com/");
    }
}
