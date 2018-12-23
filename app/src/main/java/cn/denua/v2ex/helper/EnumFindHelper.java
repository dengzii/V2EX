/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.helper;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import cn.denua.v2ex.interfaces.EnumKeyGetter;

/*
 * @author denua
 * @email denua@foxmail.com
 * @date 2018/12/23 12
 */
public class EnumFindHelper<V extends Enum<V>, K> {

    private Map<K, V> lookUp = new HashMap<>();

    public EnumFindHelper(Class<V> clazz, EnumKeyGetter<V, K> keyGetter){
        for (V enumValue:EnumSet.allOf(clazz)){
            lookUp.put(keyGetter.getKey(enumValue), enumValue);
        }
    }

    public V find(K key, V defaultValue){
        V enumName = lookUp.get(key);
        return enumName == null ? defaultValue : enumName;
    }
}

