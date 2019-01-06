/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.service;

/*
 * 返回错误与错误说明枚举
 *
 * @author denua
 * @date 2018/11/26 14
 */
public enum ErrorEnum {

    ERR_PAGE_NEED_LOGIN0("<title>V2EX › 登录</title>", "需要先登录才能浏览"),
    ERR_PAGE_NEED_LOGIN("你要查看的页面需要先登录", "需要先登录才能浏览"),
    ERR_EMPTY_RESPONSE("空响应体", "服务器返回了一个空"),

    ERROR_CREATE_TOPIC_PROBLEM("创建主题时遇到一些问题", "创建主题时遇到一些问题"),

    ERROR_CREATE_TOPIC_TOO_OFTEN("你不能过于频繁地创建新主题", "过于频繁地创建新主题"),
    ERROR_CREATE_TOPIC_NEED_VERIFY_EMAIL("验证邮箱", "创建新主题过程中遇到一些问题"),


    ERROR_PARSE_HTML(" ", "解析 HTML 时发生错误"),
    ERR_EMPTY_REPLY("","");

    private String pattern;
    private String readable;

    ErrorEnum(String pattern, String readable) {
        this.pattern = pattern;
        this.readable = readable;
    }

    public String getPattern() {
        return pattern;
    }

    public String getReadable() {
        return readable;
    }

    public void check(String src) throws VException{
        if (src.contains(pattern)){
            throw new VException(readable);
        }
    }

    public void throwThis() throws VException{
        throw new VException(readable);
    }
}
