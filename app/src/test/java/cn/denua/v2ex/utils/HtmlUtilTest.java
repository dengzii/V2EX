/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.utils;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

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
    public void reply() {

        String input = "" +
                "@<a href=\"/member/helloWorld\">helloWorld</a> #2 how are you?" +
                "what say.\"<a href=\"/member/username\">username</a>";

        String regex = "<a href=\"/member/\\w+\">(\\w+)</a>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        List<int[]> group = new ArrayList<>();

        while (matcher.find()){
            String result = matcher.group(1);
            int start = input.indexOf("<");
            input = input.replaceFirst(regex, result);
            int endFix = start + result.length();
            group.add(new int[]{start, endFix});
        }
        System.out.println(input);
        for (int[] ints:group){
            System.out.println(ints[0]+"-"+ints[1]);
        }
    }

    @Test
    public void attachAccountInfo() {

    }

    @Test
    public void applyHtmlStyle() {

    }
}





