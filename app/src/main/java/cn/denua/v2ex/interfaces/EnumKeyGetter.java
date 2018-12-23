/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.interfaces;

/*
 * @author denua
 * @email denua@foxmail.com
 * @date 2018/12/23 13
 */
public interface EnumKeyGetter<V extends Enum<V>, K>{
    K getKey(V enumName);
}