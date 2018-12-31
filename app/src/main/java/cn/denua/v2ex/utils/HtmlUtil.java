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
import cn.denua.v2ex.service.V2exException;

public class HtmlUtil {

    private static final Pattern
            PATTERN_FAVOR_NODES         = Pattern.compile("<span class=\"bigger\">(\\d+)</span><div class=\"sep3\"></div><span class=\"fade\">节点收藏</span>"),
            PATTERN_FAVOR_TOPIC         = Pattern.compile("<span class=\"bigger\">(\\d+)</span><div class=\"sep3\"></div><span class=\"fade\">主题收藏</span>"),
            PATTERN_FOLLOWING           = Pattern.compile("<span class=\"bigger\">(\\d+)</span><div class=\"sep3\"></div><span class=\"fade\">特别关注</span>"),
            PATTERN_NOTIFICATIONS       = Pattern.compile("([\\d]+) 条未读提醒"),
            PATTERN_JOIN                = Pattern.compile("已在 ([\\d :+-]+) 完成验证"),
            PATTERN_NUMBER              = Pattern.compile("V2EX 第 (\\d+) 号会员"),
            PATTERN_GOLD                = Pattern.compile("(\\d+) <img src=\"/static/img/gold@2x.png\" height=\"16\""),
            PATTERN_SILVER              = Pattern.compile("(\\d+) <img src=\"/static/img/silver@2x.png\" height=\"16\""),
            PATTERN_BRONZE              = Pattern.compile("(\\d+) <img src=\"/static/img/bronze@2x.png\" height=\"16\""),

            PATTERN_TOPIC_NODE_NAME     = Pattern.compile("node\" href=\"/go/(\\w+)\">[^<]+</a>"),
            PATTERN_TOPIC_NODE_TITLE    = Pattern.compile("node\" href=\"/go/\\w+\">([^<]+)</a>"),
            PATTERN_TOPIC_FAVORS        = Pattern.compile("∙ {2}(\\d+) 人收藏 "),
            PATTERN_TOPIC_THANKS        = Pattern.compile("∙ {2}(\\d+) 人感谢"),
            PATTERN_TOPIC_CSRF          = Pattern.compile("var csrfToken = \"([^\"]+)"),
            PATTERN_TOPIC_REPLY_COUNT   = Pattern.compile("<span class=\"gray\">(\\d+) 回复"),
            PATTERN_TOPIC_REPLY_COUNT_  = Pattern.compile("/t/\\d+#reply(\\d+)"),
            PATTERN_TOPIC_ID            = Pattern.compile("href=\"/t/(\\d+)"),
            PATTERN_TOPIC_USER_AVATAR   = Pattern.compile("<a href=\"/member/\\w+\"><img src=\"([^\"]+)"),
            PATTERN_TOPIC_USERNAME      = Pattern.compile("href=\"/member/(\\w+)\"><img src="),
            PATTERN_TOPIC_AGO           = Pattern.compile("<span class=\"ago\">([^\"]+前)"),
            PATTERN_TOPIC_AGO_          = Pattern.compile("•?[^•]+?• {2}([^<•]+) {2}•"),
            PATTERN_TOPIC_CLICK         = Pattern.compile(" · (\\d+) 次点击"),
            PATTERN_TOPIC_UP_VOTE       = Pattern.compile("votes\"><li class=\"fa fa-chevron-up\"></li> (\\d+)"),

            PATTERN_REPLY_ID            = Pattern.compile("id=\"r_(\\d+)\""),
            PATTERN_REPLY_USERNAME      = Pattern.compile("href=\"/member/([^\"]+)\""),
            PATTERN_REPLY_AVATAR        = Pattern.compile("<img src=\"([^\"]+)\" class=\"avatar\""),
            PATTERN_REPLY_AGO           = Pattern.compile("<span class=\"ago\">([^\"]+前)"),
            PATTERN_REPLY_VIA           = Pattern.compile("(via [^<]+)"),
            PATTERN_REPLY_LIKE          = Pattern.compile("<span class=\"small fade\">♥ (\\d+)</span>"),
            PATTERN_REPLY_COUNT_MEMBER_DETAIL = Pattern.compile("href=\"/t/\\d+#reply(\\d+)");

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

    public static int getOnceFromPostTopicPage(String html){
        return matcherGroup1Int(Pattern.compile("value=\"(\\d+)\" name=\"once\""), html);
    }

    public static int getOnceFromSignInPage(String html){
        return matcherGroup1Int(Pattern.compile("/mission/daily/redeem\\?once=(\\d+)"), html);
    }
    /**
     * 从HTML中获取所有话题
     *
     * @return topics
     */
    public static List<Topic> getTopics(String html){

        Document document = Jsoup.parse(html);
        Elements elements;
        Node node = null;

        if (html.contains("TopicsNode")){
            elements = document.select("#TopicsNode >  .cell");
            node = new Node("","");
        }else {
            elements = document.select("#Main > .box > .cell");
        }

        List<Topic> topics = new ArrayList<>(101);
        Topic topic;
        Member member;
        for (Element element:elements){
            if (topics.size() >= 100){
                break;
            }
            try {
                topic = new Topic();
                String s = element.toString().replaceAll("&nbsp;", " ");
                topic.setId(matcherGroup1Int(PATTERN_TOPIC_ID, s));
                topic.setTitle(element.selectFirst(".item_title").text());//fixme null point exception
                topic.setReplies(matcherGroup1Int(PATTERN_TOPIC_REPLY_COUNT_, s));
                topic.setAgo(matcherGroup1(PATTERN_TOPIC_AGO_, s));

                member = new Member();
                member.setUsername(matcherGroup1(PATTERN_TOPIC_USERNAME, s));
                member.setAvatar_normal(matcherGroup1(PATTERN_TOPIC_USER_AVATAR, s));
                topic.setNode(new Node(matcherGroup1(PATTERN_TOPIC_NODE_NAME, s),
                        matcherGroup1(PATTERN_TOPIC_NODE_TITLE, s)));
                topic.setUpVote(matcherGroup1Int(PATTERN_TOPIC_UP_VOTE, s));
                topic.setMember(member);
                topics.add(topic);
            }catch (Exception e){
                System.out.println(element.html());
                e.printStackTrace();
            }
        }
        return topics;
    }

    public static void attachRepliesAndDetail(Topic topic, String html){

        Document document = Jsoup.parse(html);
        Iterator<Element> it = document.select("#Main > .box > .cell > .fr > .tag").iterator();
        List<Tag> tags = new ArrayList<>();

        for ( ; it.hasNext(); ) {
            tags.add(new Tag(it.next().text()));
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
        topic.setClicks(matcherGroup1Int(PATTERN_TOPIC_CLICK, html));
        topic.setTags(tags);

        if (topic.getNode() == null){
            String nodeName = matcherGroup1(PATTERN_TOPIC_NODE_NAME, html);
            String nodeTitle = matcherGroup1(PATTERN_TOPIC_NODE_TITLE, html);
            topic.setNode(new Node(nodeName, nodeTitle));
        }
        topic.setAgo(matcherGroup1(PATTERN_TOPIC_AGO, html));
        topic.setFavors(matcherGroup1Int(PATTERN_TOPIC_FAVORS, html));
        topic.setThanks(matcherGroup1Int(PATTERN_TOPIC_THANKS, html));
        topic.setCsrfToken(matcherGroup1(PATTERN_TOPIC_CSRF, html));
        if (topic.getReplies() == 0){
            topic.setReplies(matcherGroup1Int(PATTERN_TOPIC_REPLY_COUNT, html));
        }
        attachReplies(topic, html);
    }

    public static void attachReplies(Topic topic, String html){

        if (topic.getReplies() == 0){
            topic.setReplyList(new ArrayList<>());
            return;
        }

        Document document = Jsoup.parse(html);
        Elements elements = document.select("#Main > .box > .cell[id]");
        Iterator<Element> elementIterator = elements.iterator();

        List<Reply> replies = new ArrayList<>(elements.size());
        String poster = topic.getMember().getUsername();

        for (int f=0; elementIterator.hasNext(); f++) {
            Element e = elementIterator.next();
            Reply reply = new Reply();

            Element element = e.selectFirst(".reply_content");
            if (element != null){
                for (Element img:element.select("img")){
                    img.attr("width","100%");
                    img.attr("height","auto");
                }
                reply.setContent(element.html());
            }else{
                throw new V2exException("This post seems to have been blocked\nEmpty reply content");
            }

            String cell = e.toString();
            int id = matcherGroup1Int(PATTERN_REPLY_ID, cell);
            String username = matcherGroup1(PATTERN_REPLY_USERNAME, cell);
            String avatarNormal = matcherGroup1(PATTERN_REPLY_AVATAR, cell);

            reply.setId(id);
            reply.setMember(new Member(username, avatarNormal));
            reply.setPoster(username.equals(poster));
            reply.setAgo(matcherGroup1(PATTERN_REPLY_AGO, cell));
            reply.setVia(matcherGroup1(PATTERN_REPLY_VIA, cell));
            reply.setLike(matcherGroup1Int(PATTERN_REPLY_LIKE, cell));
            reply.setFloor(f);

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
            int replies = matcherGroup1Int(PATTERN_REPLY_COUNT_MEMBER_DETAIL,element.html());
            topic.setId(matcherGroup1Int(PATTERN_TOPIC_ID, element.html()));
            topic.setTitle(element.selectFirst(".item_title").text());
            topic.setReplies(replies);
            topic.setNode(new Node(matcherGroup1(PATTERN_TOPIC_NODE_NAME, element.html()),
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

        account.setFavorNodes(matcherGroup1Int(PATTERN_FAVOR_NODES, html));
        account.setFavorTopics(matcherGroup1Int(PATTERN_FAVOR_TOPIC, html));
        account.setFollowing(matcherGroup1Int(PATTERN_FOLLOWING, html));

        account.setNotifications(matcherGroup1Int(PATTERN_NOTIFICATIONS, html));
        account.setJoin(matcherGroup1(PATTERN_JOIN, html));
        account.setNumber(matcherGroup1(PATTERN_NUMBER, html));

        account.setGold(matcherGroup1Int(PATTERN_GOLD, html));
        account.setSilver(matcherGroup1Int(PATTERN_SILVER ,  html));
        account.setBronze(matcherGroup1Int(PATTERN_BRONZE, html));

        account.setBalance(account.getBronze()+account.getSilver()*100+account.getGold()*10000);
    }

    public static String applyHtmlStyle(String html){

        if (html == null || html.equals("")){
            return "";
        }
        Document document = Jsoup.parse(html);
        for (Element img:document.select("img")){
            img.attr("width","100%");
            img.attr("height","auto");
        }

        return document.toString();
    }

    public static String matcherGroup1(Pattern pattern, String str){

        Matcher matcher = pattern.matcher(str);
        if (matcher.find()){
            return matcher.group(1).trim();
        }
        return "";
    }
    public static int matcherGroup1Int(Pattern pattern, String str){
        String res = matcherGroup1(pattern, str);
        return res.equals("") ? 0 : Integer.valueOf(res);
    }
}
