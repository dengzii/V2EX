package cn.denua.v2ex.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/*
 *
 *
 * @author denua
 * @date 2018/10/21
 */
public class Node implements Parcelable, Cloneable {

    private static Node node = new Node();

    protected int id;
    protected String name;
    protected String title;
    protected int topics;
    protected String footer;
    protected String header;

    protected String title_alternative;

    protected String parent_node_name;

    protected int stars;
    protected boolean root;

    protected String avatar_normal;
    protected String avatar_mini;
    protected String avatar_large;

    public static Node getNode(){
        return (Node) node.clone();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTopics() {
        return topics;
    }

    public void setTopics(int topics) {
        this.topics = topics;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getTitle_alternative() {
        return title_alternative;
    }

    public void setTitle_alternative(String title_alternative) {
        this.title_alternative = title_alternative;
    }

    public String getParent_node_name() {
        return parent_node_name;
    }

    public void setParent_node_name(String parent_node_name) {
        this.parent_node_name = parent_node_name;
    }

    public int getStarts() {
        return stars;
    }

    public void setStarts(int stars) {
        this.stars = stars;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getAvatar_normal() {
        return "https:" + avatar_normal;
    }

    public void setAvatar_normal(String avatar_normal) {
        this.avatar_normal = avatar_normal;
    }

    public String getAvatar_mini() {
        return "https:" + avatar_mini;
    }

    public void setAvatar_mini(String avatar_mini) {
        this.avatar_mini = avatar_mini;
    }

    public String getAvatar_large() {
        return "https:" + avatar_large;
    }

    public void setAvatar_large(String avatar_large) {
        this.avatar_large = avatar_large;
    }

    public Object clone(){

        Node copy = null;

        try {
            copy = (Node) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return copy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.title);
        dest.writeInt(this.topics);
        dest.writeString(this.footer);
        dest.writeString(this.header);
        dest.writeString(this.title_alternative);
        dest.writeString(this.parent_node_name);
        dest.writeInt(this.stars);
        dest.writeByte(this.root ? (byte) 1 : (byte) 0);
        dest.writeString(this.avatar_mini);
        dest.writeString(this.avatar_normal);
        dest.writeString(this.avatar_large);
    }

    public Node() {
    }

    public Node(String name, String title){
        this.name = name;
        this.title = title;
    }

    protected Node(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.title = in.readString();
        this.topics = in.readInt();
        this.footer = in.readString();
        this.header = in.readString();
        this.title_alternative = in.readString();
        this.parent_node_name = in.readString();
        this.stars = in.readInt();
        this.root = in.readByte() != 0;
        this.avatar_mini = in.readString();
        this.avatar_normal = in.readString();
        this.avatar_large = in.readString();
    }

    public static final Creator<Node> CREATOR = new Creator<Node>() {
        @Override
        public Node createFromParcel(Parcel source) {
            return new Node(source);
        }

        @Override
        public Node[] newArray(int size) {
            return new Node[size];
        }
    };

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", topics=" + topics +
                ", footer='" + footer + '\'' +
                ", header='" + header + '\'' +
                ", title_alternative='" + title_alternative + '\'' +
                ", parent_node_name='" + parent_node_name + '\'' +
                ", stars=" + stars +
                ", root=" + root +
                ", avatar_normal='" + avatar_normal + '\'' +
                ", avatar_mini='" + avatar_mini + '\'' +
                ", avatar_large='" + avatar_large + '\'' +
                '}';
    }
}
