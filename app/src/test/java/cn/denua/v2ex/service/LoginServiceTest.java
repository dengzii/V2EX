/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.service;

import com.blankj.utilcode.util.RegexUtils;

import org.junit.Test;

import java.util.List;
import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.denua.v2ex.model.Account;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static org.junit.Assert.*;

/*
 *
 *
 * @author denua
 * @date 2018/11/03 13
 */
public class LoginServiceTest {

    @Test
    public void test1(){

        io.reactivex.Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        emitter.onNext("a");
                        emitter.onComplete();
                    }
                })
                .flatMap(new Function<String, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(String s) throws Exception {
                        System.out.println(s);
                        return io.reactivex.Observable.just(1,2,3);
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println(integer);
                    }
                });

    }

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