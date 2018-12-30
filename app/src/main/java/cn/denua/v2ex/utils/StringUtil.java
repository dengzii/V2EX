/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.utils;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.TimeUtils;

import java.text.DateFormat;
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

        String format = Config.getConfig(ConfigRefEnum.CONFIG_DATE_FORMAT);
        Locale locale = Locale.getDefault();//Config.getConfig(ConfigRefEnum.CONFIG_LOCAL);
        DateFormat dateFormat = new SimpleDateFormat(format, locale);

        return TimeUtils.getString(timestamp, dateFormat, timestamp, TimeConstants.SEC);
    }

    public static synchronized String getDateNow(String format){

        Locale locale = Locale.getDefault();
        DateFormat dateFormat = new SimpleDateFormat(format, locale);

        return TimeUtils.getString(System.currentTimeMillis(), dateFormat,
                System.currentTimeMillis(), TimeConstants.SEC);
    }
}
