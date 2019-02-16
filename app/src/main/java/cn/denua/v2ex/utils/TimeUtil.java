/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.utils;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cn.denua.v2ex.Config;
import cn.denua.v2ex.ConfigRefEnum;

/*
 *  string util
 *
 * @author denua
 * @date 2018/11/26 16
 */
public class TimeUtil {

    public static boolean isNowBetweenTimeSpanOfDay(String startHourMin, String endHourMin){

        Calendar calendar = Calendar.getInstance();

        int now = calendar.get(Calendar.HOUR_OF_DAY) *  60 + calendar.get(Calendar.MINUTE);

        int start = Integer.valueOf(startHourMin.substring(0, 2)) * 60
                +  Integer.valueOf(startHourMin.substring(3, 5));
        int end = Integer.valueOf(endHourMin.substring(0, 2)) * 60
                + Integer.valueOf(endHourMin.substring(3, 5));

        return (now >= start && now <= end);
    }

    public static String timestampToStr(long secondTimestamp){

        if (secondTimestamp < 10000000000L){
            secondTimestamp = secondTimestamp * 1000L;
        }
        return timestampToStr(secondTimestamp, Config.getConfig(ConfigRefEnum.CONFIG_DATE_FORMAT));
    }

    /**
     * 毫秒转时间字符串
     *
     * @param millTimestamp 毫秒
     * @param format 格式
     * @return 格式化的时间
     */
    public static String timestampToStr(long millTimestamp, String format){
        return new SimpleDateFormat(format, Locale.getDefault()).format(millTimestamp);
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
