package cn.denua.v2ex.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class V2exUtil {

    public static String[] washLoginFieldName(String html){

        System.out.println(html);
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

    public static String[] washProfileInfo(String html){

        Document document = Jsoup.parse(html);

        Element element = document.selectFirst("tbody > tr > td");
        return null;
    }
}
