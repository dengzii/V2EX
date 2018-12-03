package cn.denua.v2ex.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/*
 * v2ex topic
 * 包含一个话题的所有回复
 *
 * @author denua
 * @date 2018/10/21
 */
public class Topic implements Parcelable, Cloneable {

    private int id;

    private long last_touched;
    private long last_modified;
    private long created;

    private int clicks = 0;
    private int favors = 0;
    private int thanks = 0;
    private int replies = 0;
    private int upVote = 0;
    private int downVote = 0;

    private String title;
    private String content;
    private String content_rendered;
    private String last_reply_by;
    private String url;
    private String ago;

    private Member member;
    private Node node;
    private List<Reply> replyList;
    private List<Tag> tags = new ArrayList<>();

    private String csrfToken;

    public Topic(){}

    public Topic(int id){
        this.id = id;
    }

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

    public int getUpVote() {
        return upVote;
    }

    public void setUpVote(int upVote) {
        this.upVote = upVote;
    }

    public int getDownVote() {
        return downVote;
    }

    public void setDownVote(int downVote) {
        this.downVote = downVote;
    }

    public String getAgo() {
        return ago;
    }

    public void setAgo(String ago) {
        this.ago = ago;
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

    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public List<Reply> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<Reply> replyList) {
        this.replyList = replyList;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Object clone(){

        Topic copy = null;
        try {
            copy = (Topic) super.clone();
            copy.setMember((Member) member.clone());
            copy.setNode((Node) node.clone());
            if (replyList != null){
                copy.setReplyList(new ArrayList<>());
                replyList.addAll(copy.getReplyList());
            }
            if (tags != null) {
                copy.setTags(new ArrayList<>());
                tags.addAll(copy.getTags());
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return copy;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "id=" + id +
                ", replies=" + replies +
                ", title='" + title + '\'' +
                ", last_reply_by='" + last_reply_by + '\'' +
                ", ago='" + ago + '\'' +
                ", last_reply_by=" + last_reply_by +
                ", node=" + node.getTitle() +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeLong(this.last_touched);
        dest.writeLong(this.last_modified);
        dest.writeString(this.url);
        dest.writeLong(this.created);
        dest.writeInt(this.clicks);
        dest.writeInt(this.favors);
        dest.writeInt(this.thanks);
        dest.writeInt(this.replies);
        dest.writeString(this.content);
        dest.writeString(this.content_rendered);
        dest.writeString(this.last_reply_by);
        dest.writeParcelable(this.member, flags);
        dest.writeParcelable(this.node, flags);
        dest.writeList(this.replyList);
        dest.writeList(this.tags);
    }

    protected Topic(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.last_touched = in.readLong();
        this.last_modified = in.readLong();
        this.url = in.readString();
        this.created = in.readLong();
        this.clicks = in.readInt();
        this.favors = in.readInt();
        this.thanks = in.readInt();
        this.replies = in.readInt();
        this.content = in.readString();
        this.content_rendered = in.readString();
        this.last_reply_by = in.readString();
        this.member = in.readParcelable(Member.class.getClassLoader());
        this.node = in.readParcelable(Node.class.getClassLoader());
        this.replyList = new ArrayList<Reply>();
        in.readList(this.replyList, Reply.class.getClassLoader());
        this.tags = new ArrayList<Tag>();
        in.readList(this.tags, Tag.class.getClassLoader());
    }

    public static final Creator<Topic> CREATOR = new Creator<Topic>() {
        @Override
        public Topic createFromParcel(Parcel source) {
            return new Topic(source);
        }

        @Override
        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };
}
