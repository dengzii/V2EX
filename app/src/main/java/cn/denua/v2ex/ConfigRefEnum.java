/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

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
    CONFIG_THEME                ("theme",           R.style.MainTheme),
    CONFIG_AUTO_NIGHT_THEME     ("auto_night",      false),
    CONFIG_AUTO_NIGHT_DARK      ("auto_night_dark", false),
    CONFIG_AUTO_NIGHT           ("night_time",      18.30f),
    CONFIG_AUTO_CHECK           ("auto_check",      false),
    CONFIG_HOME_TAB             ("home_tab",        Config.HOME_TAB_TITLES),
    CONFIG_DETAILED_INFO        ("detailed",        false),
    CONFIG_MEMBER_TAB           ("member_tab",      null),
    CONFIG_LOCAL                ("local",           Locale.CHINA),

    CONFIG_PROFILE_DATE_FORMAT  ("profile_date_format", "yyyy/MM/dd HH:mm"),
    CONFIG_REPLY_LINE_HEIGHT    ("reply_line_height",   1.3f),

    CONFIG_REPLY_FROM_API       ("get_reply_from_api",  false),
    ;

    private String key;
    private Serializable defaultValue;

    ConfigRefEnum(String k, Serializable v) {
        this.key = k;
        this.defaultValue = v;
    }

    public String getKey(){
        return this.key;
    }
    public Serializable getDefaultValue(){
        return this.defaultValue;
    }
}
