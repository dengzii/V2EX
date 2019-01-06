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

        if (timestamp < 10000000000L){
            timestamp = timestamp * 1000L;
        }
        return new SimpleDateFormat(Config.getConfig(
                ConfigRefEnum.CONFIG_DATE_FORMAT), Locale.getDefault()).format(timestamp);
    }

    /**
     * 日期转时间戳
     *
     * @param date 日期
     * @param format 日期的格式 默认：yyyy-MM-dd HH:mm:ss
     * @return 时间戳
     */
    public static long strToTimestamp(String date, String format){

        try {
            if (format == null) format = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
            return simpleDateFormat.parse(date).getTime() / 1000;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public static synchronized String getDateNow(String format){

        return new SimpleDateFormat(format, Locale.getDefault()).format(System.currentTimeMillis());
    }
}
