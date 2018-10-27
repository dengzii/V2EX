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

    private int id;

    private String title;

    private long last_touched;
    private long last_modified;

    private String url;
    private long created;

    private int clicks = 0;
    private int favors = 0;
    private int thanks = 0;
    private int replies = 0;

    private String content;

    private String content_rendered;

    private String last_reply_by = "";

    private Member member;

    private Node node;

    private Replay[] replay;




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getLast_touched() {
        return last_touched;
    }

    public void setLast_touched(long last_touched) {
        this.last_touched = last_touched;
    }

    public long getLast_modified() {
        return last_modified;
    }

    public void setLast_modified(long last_modified) {
        this.last_modified = last_modified;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public int getFavors() {
        return favors;
    }

    public void setFavors(int favors) {
        this.favors = favors;
    }

    public int getThanks() {
        return thanks;
    }

    public void setThanks(int thanks) {
        this.thanks = thanks;
    }

    public int getReplies() {
        return replies;
    }

    public void setReplies(int replies) {
        this.replies = replies;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent_rendered() {
        return content_rendered;
    }

    public void setContent_rendered(String content_rendered) {
        this.content_rendered = content_rendered;
    }

    public String getLast_reply_by() {
        return last_reply_by;
    }

    public void setLast_reply_by(String last_reply_by) {
        this.last_reply_by = last_reply_by;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Replay[] getReplay() {
        return replay;
    }

    public void setReplay(Replay[] replay) {
        this.replay = replay;
    }
}
