package cn.denua.v2ex.utils;

import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.*;

/**
 * cn.denua.v2ex.utils
 *
 * @author denua
 * @email denua@foxmail.com
 * @date 2019/3/5
 */
public class TimeUtilTest {

    @Test
    public void isNowBetweenTimeSpanOfDay() {
        try {
            TimeUtil.isNowBetweenTimeSpanOfDay("21:31","06:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}