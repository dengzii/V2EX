/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex;

import org.junit.Test;

/*
 * config test
 *
 * @author denua
 * @date 2018/11/23 20
 */
public class ConfigTest {

    @Test
    public void test(){

        System.out.println(TabEnum.findByDescriptor("DD").getTitle());

//        System.out.println(ConfigRefEnum.getByDescriptor("local"));

//        for (ConfigRefEnum configRefEnum:ConfigRefEnum.values()){
//            System.out.println(configRefEnum.getKey() + ": " + configRefEnum.getDefaultValue());
//        }

    }
}