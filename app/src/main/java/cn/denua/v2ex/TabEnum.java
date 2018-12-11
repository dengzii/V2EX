/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex;

import java.io.Serializable;

/*
 * topic tab
 *
 * @author denua
 * @date 2018/12/11 13
 */
public enum TabEnum implements Serializable {

    HOT("热 门"), NEWEST("最 新"), CHANGES("最近更新");
    String title;

    TabEnum(String title){
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
}
