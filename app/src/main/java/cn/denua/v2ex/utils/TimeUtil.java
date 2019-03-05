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

    /**
     * 判断现在是否在一个时间段内, 这个时间段跨越两天, 时间段的格式为 HH:mm
     *
     * @param startHourMin 开始时间 格式 HH:mm
     * @param endHourMin 结束时间
     * @return 是否在这个时间段
     */
    public static boolean isNowBetweenTimeSpanOfDay(String startHourMin, String endHourMin){

        Calendar now = Calendar.getInstance();

        Calendar start = (Calendar) now.clone();
        start.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startHourMin.split(":")[0]));
        start.set(Calendar.MINUTE, Integer.parseInt(startHourMin.split(":")[1]));

        Calendar end = (Calendar) now.clone();
        end.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) + 1);
        end.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endHourMin.split(":")[0]));
        end.set(Calendar.MINUTE, Integer.parseInt(endHourMin.split(":")[1]));

        return now.after(start) && now.before(end);
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
