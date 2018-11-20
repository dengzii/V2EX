/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.utils;


import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.denua.v2ex.model.Topic;

/*
 * HtmlUtil test
 *
 * @author denua
 * @date 2018/11/09 21
 */
public class HtmlUtilTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void getLoginFieldName() {

    }

    @Test
    public void attachRepliesAndDetail() {

    }

    @Test
    public void getMemberTopics(){

    }

    @Test
    public void reply() {

        String input = "@<a href=\"/member/newmind\">newmind</a> #13 <br><img src=\"https://i.imgur.com/pVOVOsz.png\" class=\"embedded_image\">" +
                "@<a href=\"/member/redapple02041\"" +
                ">redapple02041</a> #18 <br>aaaaaaaaaaaaaa<a target=\"_blank\" href=\"https://chrome.google.com/webstore/detai" +
                "l/acfun-html5-player/onjihgccojkcmlmlbjmgkcnehdbpbhcf\" rel=\"nofollow\">https://chrome.google.co" +
                "m/webstore/detail/acfun-html5-player/onjihgccojkcmlmlbjmgkcnehdbpbhcf</a>";

        input = input.replaceAll("<br>","");

        String regex = "<a href=\"/member/\\w+\">(\\w+)</a>|<a target=\"_blank\" href=\"(http[^\"]+)\" [^>]+>[^>]+>|<img src=\"(http[^\"]+)\" [^>]+>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        String[] split = input.split(regex);

        int index=0;
        StringBuilder re= new StringBuilder();
        int count = 0;
        while (matcher.find()){

            String group1 = matcher.group(1);
            String group2 = matcher.group(2);
            String group3 = matcher.group(3);

            re.append(split[index++]);
            if (group1 != null){
                re.append(group1);
                System.out.println(group1);
            }else if (group2 !=  null){
                re.append(group2);
                System.out.println(group2);
            }else{
                re.append(group3);
                System.out.println(group3);
            }
        }
        if (split.length != index)
            re.append(split[index]);

        System.out.println(re);
    }

    @Test
    public void attachAccountInfo() {

    }

    @Test
    public void applyHtmlStyle() {

    }
}





