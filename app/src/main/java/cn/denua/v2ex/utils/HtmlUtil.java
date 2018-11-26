package cn.denua.v2ex.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.denua.v2ex.model.Account;
import cn.denua.v2ex.model.Member;
import cn.denua.v2ex.model.Node;
import cn.denua.v2ex.model.Reply;
import cn.denua.v2ex.model.Tag;
import cn.denua.v2ex.model.Topic;

public class HtmlUtil {

    public static String[] getLoginFieldName(String html){

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

    public static void attachRepliesAndDetail(Topic topic, String html){

        Document document = Jsoup.parse(html);
        Iterator<Element> it = document.select("#Main > .box > .cell > .fr > .tag").iterator();
        List<Tag> tags = new ArrayList<>();

        for ( ; it.hasNext(); ) {
            Element element = it.next();
            tags.add(new Tag(element.text()));
        }
        // topic not from home page
        if (topic.getContent_rendered()==null){
            Element contentBox = document.selectFirst("#Main > .box");
            Element topicContent = contentBox.selectFirst(".cell");

            topic.setContent_rendered("<br><br>");
            if (topicContent != null){
                Elements subtle = contentBox.select(".subtle");
                topic.setContent_rendered(topicContent.html() +
                        (subtle == null ? "" :subtle.html()));
            }
        }
        topic.setClicks(matcherGroup1Int(" · (\\d+) 次点击", html));
        topic.setTags(tags);

        String nodeName = matcherGroup1("<a href=\"/go/([^\"])+\">[^<]+</a>", html);
        String nodeTitle = matcherGroup1("<a href=\"/go/[^\"]+\">([^<]+)</a>", html);
        topic.setNode(new Node(nodeName, nodeTitle));
        topic.setFavors(matcherGroup1Int("∙  (\\d+) 人收藏 ", html));
        topic.setThanks(matcherGroup1Int("∙  (\\d+) 人感谢", html));
        topic.setCsrfToken(matcherGroup1("var csrfToken = \"([^\"]+)", html));
        if (topic.getReplies() == 0){
            topic.setReplies(matcherGroup1Int("<span class=\"gray\">(\\d+) 回复", html));
        }
        attachReplies(topic, html);
    }

    public static void attachReplies(Topic topic, String html){

        if (topic.getReplies() == 0){
            topic.setReplyList(new ArrayList<>());
            return;
        }

        Document document = Jsoup.parse(html);
        Iterator<Element> elementIterator = document.select("#Main > .box > .cell[id]").iterator();

        List<Reply> replies = new ArrayList<>();
        String poster = topic.getMember().getUsername();

        for (int f=0; elementIterator.hasNext(); f++) {
            Element e = elementIterator.next();

            Reply reply = new Reply();
            String cell = e.toString();
            int id = matcherGroup1Int("id=\"r_(\\d+)\"", cell);
            String username = matcherGroup1("href=\"/member/([^\"]+)\"", cell);
            String avatarNormal = matcherGroup1("<img src=\"([^\"]+)\" class=\"avatar\"", cell);

            reply.setId(id);
            reply.setMember(new Member(username, avatarNormal));
            reply.setPoster(username.equals(poster));
            reply.setAgo(matcherGroup1("<span class=\"ago\">([^\"]+前)", cell));
            reply.setVia(matcherGroup1("(via [^<]+)", cell));
            reply.setLike(matcherGroup1Int("<span class=\"small fade\">♥ (\\d+)</span>", cell));
            reply.setFloor(f);

            Element element = e.selectFirst(".reply_content");
            for (Element img:element.select("img")){
                img.attr("width","100%");
                img.attr("height","auto");
            }

            reply.setContent(element.html());
            replies.add(reply);
        }
        if (topic.getReplyList()!= null) {
            topic.getReplyList().addAll(replies);
        } else{
            topic.setReplyList(replies);
        }
    }

    public static void attachCreatedTopics(Member member, String html){

        Document document = Jsoup.parse(html);
        Elements elements = document.select("#Main > .box > .item");

        member.setTopicsCount(Integer.parseInt(
                document.selectFirst("#Main > .box > .header > .fr > strong").text()
        ));
        List<Topic> topics = new ArrayList<>();
        for (Element element:elements){
            Topic topic = new Topic();
            int replies = matcherGroup1Int("href=\"/t/\\d+#reply(\\d+)",element.html());
            topic.setId(matcherGroup1Int("href=\"/t/(\\d+)", element.html()));
            topic.setTitle(element.selectFirst(".item_title").text());
            topic.setReplies(replies);
            topic.setNode(new Node(matcherGroup1("href=\"/go/(\\w+)\"", element.html()),
                    element.selectFirst(".node").text()));
            topic.setMember(member);
            if (replies>0){
                String ago = element.selectFirst(".topic_info").text().split(" • ")[2];
                topic.setAgo(ago);
                topic.setLast_reply_by(element.selectFirst("strong").lastElementSibling().text());
            }
            topics.add(topic);
        }
        member.setCreatedTopics(topics);
    }

    public static void attachAccountInfo(Account account, String html){

        account.setFavorNodes(matcherGroup1Int("<span class=\"bigger\">(\\d+)</span>" +
                "<div class=\"sep3\"></div><span class=\"fade\">节点收藏</span>", html));
        account.setFavorTopics(matcherGroup1Int("<span class=\"bigger\">(\\d+)</span>" +
                "<div class=\"sep3\"></div><span class=\"fade\">主题收藏</span>", html));
        account.setFollowing(matcherGroup1Int("<span class=\"bigger\">(\\d+)</span>" +
                "<div class=\"sep3\"></div><span class=\"fade\">特别关注</span>", html));

        account.setNotifications(matcherGroup1Int("([\\d]+) 条未读提醒", html));
        account.setJoin(matcherGroup1("已在 ([\\d :+-]+) 完成验证", html));
        account.setNumber(matcherGroup1("V2EX 第 (\\d+) 号会员", html));

        account.setGold(matcherGroup1Int("(\\d+) <img src=\"/static/img/gold@2x.png\"" +
                " height=\"16\" ", html));
        account.setSilver(matcherGroup1Int("(\\d+) <img src=\"/static/img/silver@2x." +
                "png\" height=\"16\"" ,  html));
        account.setBronze(matcherGroup1Int("(\\d+) <img src=\"/static/img/bronze@2x" +
                ".png\" height=\"16\"", html));

        account.setBalance(account.getBronze()+account.getSilver()*100+account.getGold()*10000);
    }

    public static String applyHtmlStyle(String html){

        Document document = Jsoup.parse(html);
        for (Element img:document.select("img")){
            img.attr("width","100%");
            img.attr("height","auto");
        }

        return document.toString();
    }

    private static String matcherGroup1(String regex, String html){

        Matcher matcher = Pattern.compile(regex).matcher(html);
        if (matcher.find()){
            return matcher.group(1);
        }
        return "";
    }

    private static int matcherGroup1Int(String regex, String html){
        String res = matcherGroup1(regex, html);
        return res.equals("") ? 0: Integer.valueOf(res);
    }
}
