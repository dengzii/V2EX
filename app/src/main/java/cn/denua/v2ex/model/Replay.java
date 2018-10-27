package cn.denua.v2ex.model;

/*
 * Topic replay
 *
 * @author denua
 * @date 2018/10/21
 */
public class Replay {

    private String id;
    private Member member;
    private Member[] at;
    private String content;
    private String ago;
    private String via;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Member[] getAt() {
        return at;
    }

    public void setAt(Member[] at) {
        this.at = at;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAgo() {
        return ago;
    }

    public void setAgo(String ago) {
        this.ago = ago;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }
}
