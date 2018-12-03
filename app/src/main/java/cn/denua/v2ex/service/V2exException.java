/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.service;

/*
 * v2ex Exception
 *
 * @author denua
 * @date 2018/12/03 11
 */
public class V2exException extends RuntimeException {

    private Exception e;
    private String msg;

    public V2exException(String msg){
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
