package cn.denua.v2ex.model;

/*
 * v2ex topic
 * 包含一个话题的所有回复
 * 回复的用户，回复的内容，回复的详情
 *
 * @author denua
 * @date 2018/10/21
 */
public class Topic {

    public String id;

    public String title;
    public String refreshTime;

    public int clicks = 0;
    public int favors = 0;
    public int thanks = 0;
    public int replay = 0;

    public String content;

    public Member author;
    public Node node;

    public Replay[] replays;
}
