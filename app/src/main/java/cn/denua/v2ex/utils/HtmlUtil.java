package cn.denua.v2ex.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Iterator;

import cn.denua.v2ex.model.Account;

public class HtmlUtil {

    public static String[] washLoginFieldName(String html){

        String[] result = new String[4];
        Document document = Jsoup.parse(html);
        Elements elements = document.select(".sl");
        int i=0;
        for (Element element : elements) {
            String name = element.attr("name");
            result[i++] = name;
        }
        result[3] = document.selectFirst("input[name=once]").val();
        return result;
    }

    public static Account washSettingsInfo(String html){

        Account account = new Account();
        Document document = Jsoup.parse(html);
        Elements elements = document.select("form>table>tbody>tr");
        Iterator<Element> ele = elements.iterator();

        account.setCreated(15555555); Long.valueOf(ele.next().child(1).text().substring(6,14).trim());
        account.setUsername(ele.next().child(1).text());
        account.setAvatar_mini(ele.next().baseUri());
        ele.next();
        ele.next();
        ele.next();
        account.setJoin(ele.next().child(1).text());

        return account;
    }
}
