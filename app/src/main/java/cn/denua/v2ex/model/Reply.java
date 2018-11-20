package cn.denua.v2ex.model;

import java.util.ArrayList;
import java.util.List;

/*
 * Topic replay
 *
 * @author denua
 * @date 2018/10/21
 */
public class Reply implements Cloneable {

    private int id;
    private Member member;
    private String content;
    private String ago;
    private String via;
    private boolean isPoster;
    private int like;
    private int floor;

    private List<Reply> at;

    public boolean isPoster() {
        return isPoster;
    }

    public void setPoster(boolean poster) {
        isPoster = poster;
    }

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

    public int getLike() {
        return like;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void setLike(int like) {
        this.like = like;
    }

    @Override
    protected Object clone(){

        Reply copy = null;
        try {
            copy = (Reply) super.clone();
            if ( at != null) {
                copy.setAt(new ArrayList<>());
                at.addAll(copy.getAt());
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return copy;
    }
}
