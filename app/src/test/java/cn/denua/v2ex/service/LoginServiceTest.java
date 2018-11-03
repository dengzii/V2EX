/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.service;

import com.blankj.utilcode.util.RegexUtils;

import org.junit.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.denua.v2ex.model.Account;

import static org.junit.Assert.*;

/*
 *
 *
 * @author denua
 * @date 2018/11/03 13
 */
public class LoginServiceTest {


    @Test
    public void getInfo(){

        String input = "<tr>\n" +
                "                                    <td width=\"48\" valign=\"top\"><a href=\"/member/abcd\"><img src=\"//\" class=\"avatar\" style=\"max-width: 48px; max-height: 48px;\" border=\"0\" align=\"default\"></a></td>\n" +
                "                                    <td width=\"10\" valign=\"top\"></td>\n" +
                "                                    <td width=\"auto\" align=\"left\"><span class=\"bigger\"><a href=\"/member/abcd\">denua1</a></span>\n" +
                "                                        \n" +
                "                                    </td>\n" +
                "                                </tr>";


        Matcher matcher = Pattern.compile("href=\"/member/([^\"]+)\"").matcher(input);
        System.out.println(matcher.find());
        System.out.println(matcher.group(1));

    }
}