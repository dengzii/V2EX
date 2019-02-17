package cn.denua.v2ex;

import java.io.Serializable;

/**
 * cn.denua.v2ex
 *
 * @author denua
 * @email denua@foxmail.com
 * @date 2019/2/16
 */
public class Tab implements Serializable {

    private String name;
    private TabEnum type;
    private String title;

    public Tab(TabEnum type, String name,  String title) {
        this.name = name;
        this.type = type;
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TabEnum getType() {
        return type;
    }

    public void setType(TabEnum type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
