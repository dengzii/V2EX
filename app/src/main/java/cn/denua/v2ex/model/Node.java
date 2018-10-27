package cn.denua.v2ex.model;

import com.google.gson.annotations.SerializedName;

/*
 *
 *
 * @author denua
 * @date 2018/10/21
 */
public class Node {

    private int id;
    private String name;
    private String title;
    private int topics;
    private String footer;
    private String header;

    private String title_alternative;

    private String parent_node_name;

    private int starts;
    private boolean root;

    private String avart_normal;
    private String avart_mini;
    private String avart_large;

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
}
