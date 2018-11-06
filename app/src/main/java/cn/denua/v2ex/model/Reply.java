package cn.denua.v2ex.model;

import java.util.List;

/*
 * Topic replay
 *
 * @author denua
 * @date 2018/10/21
 */
public class Reply {

    private int id;
    private Member member;
    private String content;
    private String ago;
    private String via;

    private List<Reply> at;

    public Member getMember() {
        return member;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMember(Member member) {
        this.member = member;
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

    public List<Reply> getAt() {
        return at;
    }

    public void setAt(List<Reply> at) {
        this.at = at;
    }
}
