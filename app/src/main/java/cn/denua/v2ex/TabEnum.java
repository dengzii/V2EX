/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex;

import java.io.Serializable;

import cn.denua.v2ex.helper.EnumFindHelper;

/*
 * topic tab
 *
 * @author denua
 * @date 2018/12/11 13
 */
public enum TabEnum implements Serializable {

    HOT     ("今日热议"),
    LATEST  ("最 新"),
    TAB     ("tab"),
    FOLLOWING("关 注"),
    MESSAGE ("消 息"),

    NODE    ("node"),
    CUSTOM  ("custom"),
    OTHER   ("other");

    private String title;

    static final EnumFindHelper<TabEnum, String> sFindHelper =
            new EnumFindHelper<>(TabEnum.class, TabEnum::getTitle);

    TabEnum(String title) {
        this.title = title;
    }

    public static TabEnum findByDescriptor(String descriptor){
        TabEnum node = NODE;
        node.setTitle(descriptor);
        return sFindHelper.find(descriptor, node);
    }

    public static boolean contains(String name){
        return sFindHelper.contains(name);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }
}
