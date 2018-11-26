/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex;

import java.io.Serializable;

/*
 * config reference
 *
 * @author denua
 * @date 2018/11/23 22
 */
public enum ConfigRefEnum implements Serializable {

    KEY_ACCOUNT             ("kye_account",             null),
    KEY_FILE_CONFIG_PREF    ("key_shared_pref_name",    null),
    KEY_USER_NAME           ("key_username",            null),

    CONFIG_LAST_TOUCHED         ("last_touched",    0L),
    CONFIG_THEME_INT            ("theme",           R.style.MainTheme),
    CONFIG_AUTO_NIGHT_THEME     ("auto_night",      false),
    CONFIG_AUTO_NIGHT_DARK      ("auto_night_dark", false),
    CONFIG_AUTO_NIGHT_TIME      ("night_time",      18.30f),
    CONFIG_AUTO_CHECK_BOOL      ("auto_check",      false),
    CONFIG_HOME_TAB_LIST        ("home_tab",        Config.HOME_TAB_TITLES),
    CONFIG_DETAILED_INFO        ("detailed",        false),
    CONFIG_MEMBER_TAB           ("member_tab",      null),

    CONFIG_REPLY_LINE_HEIGHT    ("reply_line_height",   1.3f),

    CONFIG_REPLY_FROM_API       ("get_reply_from_api",  false),
    ;

    private String key;
    private Object defaultValue;

    ConfigRefEnum(String account, Serializable o) { }

    public String getKey(){
        return this.key;
    }
    public Object getDefaultValue(){
        return this.defaultValue;
    }
}
