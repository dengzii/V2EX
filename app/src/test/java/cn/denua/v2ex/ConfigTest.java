/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.TimeUtils;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.denua.v2ex.utils.StringUtil;

/*
 * config test
 *
 * @author denua
 * @date 2018/11/23 20
 */
public class ConfigTest {

    @Test
    public void dataFormatTest(){
        System.out.println(StringUtil.timestampToStr(1272203146L));
        System.out.println(StringUtil.timestampToStr(System.currentTimeMillis()));
    }

    @Test
    public void test(){

        System.out.println(TabEnum.findByDescriptor("DD").getTitle());

        Locale local = Config.getConfig(ConfigRefEnum.CONFIG_LOCAL);

        System.out.println(local);
//        System.out.println(ConfigRefEnum.getByDescriptor("local"));

//        for (ConfigRefEnum configRefEnum:ConfigRefEnum.values()){
//            System.out.println(configRefEnum.getKey() + ": " + configRefEnum.getDefaultValue());
//        }

    }
}