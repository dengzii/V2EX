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

    private String id;

    private String title;
    private String time_pass;

    private int clicks;
    private int favors;
    private int thanks;

    private String content;

    private Member author;
    private Node node;

    private Replay[] replays;

}
