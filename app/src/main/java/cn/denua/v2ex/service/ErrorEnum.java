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

    ERR_PAGE_NEED_LOGIN0("placeholder=\"请输入上图中的验证码\"", "需要先登录才能浏览"),
    ERR_PAGE_NEED_LOGIN("你要查看的页面需要先登录", "需要先登录才能浏览"),
    ERR_EMPTY_RESPONSE("空响应体", "服务器返回了一个空"),

    ERROR_PARSE_HTML("", "解析 HTML 时发生错误"),
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
}
