package cn.denua.v2ex.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Iterator;

import cn.denua.v2ex.model.AccountModel;

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

    public static AccountModel washSettingsInfo(String html){

        Document document = Jsoup.parse(html);

        Elements elements = document.select("form>table>tbody>tr");
        Iterator<Element> ele = elements.iterator();

        int member = Integer.valueOf(ele.next().child(1).text().substring(6,15).trim());
        String userName = ele.next().child(1).text();
        String pic = ele.next().baseUri();

        AccountModel accountModel = new AccountModel(userName, pic, member);

        ele.next();
        ele.next();
        ele.next();
        accountModel.setJoin(ele.next().child(1).text());

        return accountModel;
    }
}
