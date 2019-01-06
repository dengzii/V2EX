package cn.denua.v2ex.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.regex.Pattern;

import cn.denua.v2ex.model.Member;
import cn.denua.v2ex.model.Node;
import cn.denua.v2ex.model.Reply;
import cn.denua.v2ex.model.Topic;

public class HtmlAmpUtil {

    public static Topic getTopicWithReply(String ampHtml){

        Topic topic = new Topic();
        Document document = Jsoup.parse(ampHtml);
        Element topicNode = document.selectFirst(".topic_node");

        Member member = new Member(document.selectFirst(".topic_author").text(), document.selectFirst(".topic_author_avatar").attr("src"));

        topic.setTitle(document.selectFirst(".topic_title").text());
        topic.setMember(member);
        topic.setContent(document.selectFirst(".topic_content").html());
        topic.setClicks(HtmlUtil.matcherGroup1Int(Pattern.compile("(\\d+)"), document.selectFirst(".topic_hits").text()));
        topic.setAgo(document.selectFirst(".topic_created").text());
        topic.setNode(new Node(HtmlUtil.matcherGroup1(Pattern.compile("/go/(\\w+)"), topicNode.html()),topicNode.selectFirst("a").text()));
        topic.setClicks(HtmlUtil.matcherGroup1Int(Pattern.compile("(\\d+)"), document.selectFirst(".topic_stats").text()));

        return topic;
    }

}
