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
public class Node implements Parcelable {

    protected int id;
    protected String name;
    protected String title;
    protected int topics;
    protected String footer;
    protected String header;

    protected String title_alternative;

    protected String parent_node_name;

    protected int starts;
    protected boolean root;

    protected String avart_normal;
    protected String avart_mini;
    protected String avart_large;

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
        return starts;
    }

    public void setStarts(int starts) {
        this.starts = starts;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public String getAvart_normal() {
        return avart_normal;
    }

    public void setAvart_normal(String avart_normal) {
        this.avart_normal = avart_normal;
    }

    public String getAvart_mini() {
        return avart_mini;
    }

    public void setAvart_mini(String avart_mini) {
        this.avart_mini = avart_mini;
    }

    public String getAvart_large() {
        return avart_large;
    }

    public void setAvart_large(String avart_large) {
        this.avart_large = avart_large;
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
        dest.writeInt(this.starts);
        dest.writeByte(this.root ? (byte) 1 : (byte) 0);
        dest.writeString(this.avart_normal);
        dest.writeString(this.avart_mini);
        dest.writeString(this.avart_large);
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
        this.starts = in.readInt();
        this.root = in.readByte() != 0;
        this.avart_normal = in.readString();
        this.avart_mini = in.readString();
        this.avart_large = in.readString();
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
}
