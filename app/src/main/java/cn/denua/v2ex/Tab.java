package cn.denua.v2ex;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Comparator;

/**
 * cn.denua.v2ex
 *
 * @author denua
 * @email denua@foxmail.com
 * @date 2019/2/16
 */
public class Tab implements Serializable, Comparable<Tab> {

    private String name;
    private TabEnum type;
    private String title;
    private int index;

    public Tab(TabEnum type, String name,  String title) {
        this.name = name;
        this.type = type;
        this.title = title;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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

    @Override
    public String toString() {
        return type.name().toLowerCase() + "_" + name + "_" + (title == null ? name : title);
    }

    @Override
    public int compareTo(@NonNull Tab o) {
        if (o.getIndex() == this.getIndex()) return 0;
        return o.getIndex() > getIndex() ?  -1 : 1;
    }
}
