/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.utils;
import java.text.SimpleDateFormat;
import java.util.Locale;

import cn.denua.v2ex.Config;
import cn.denua.v2ex.ConfigRefEnum;

/*
 *  string util
 *
 * @author denua
 * @date 2018/11/26 16
 */
public class StringUtil {


    public static String timestampToStr(long timestamp){

        if (timestamp < 9999999999L){
            timestamp = timestamp * 1000L;
        }
        return new SimpleDateFormat(Config.getConfig(
                ConfigRefEnum.CONFIG_DATE_FORMAT), Locale.getDefault()).format(timestamp);
    }

    public static synchronized String getDateNow(String format){

        return new SimpleDateFormat(format, Locale.getDefault()).format(System.currentTimeMillis());
    }
}
