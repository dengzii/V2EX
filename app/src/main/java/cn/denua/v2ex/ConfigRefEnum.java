/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex;

import java.io.Serializable;
import java.util.Locale;

import cn.denua.v2ex.helper.EnumFindHelper;

/*
 * config reference
 *
 * @author denua
 * @date 2018/11/23 22
 */
public enum ConfigRefEnum implements Serializable {

    KEY_ACCOUNT                 ("kye_account",         null),
    KEY_FILE_CONFIG_PREF        ("user_status",         null),
    KEY_USER_NAME               ("key_username",        null),

    CONFIG_LAST_TOUCHED         ("last_touched",        0L),
    CONFIG_THEME                ("key_theme",           "MainTheme"),
    CONFIG_AUTO_NIGHT_THEME     ("auto_night_theme",    true),
    CONFIG_AUTO_NIGHT_TIME      ("auto_night_time",     18.30f),
    CONFIG_AUTO_CHECK           ("auto_check",          false),

    CONFIG_CHECK_MESSAGE_BACKGROUND ("check_message_background", false),

    CONFIG_HOME_TAB             ("home_tabs",           Config.HOME_TAB_TITLES),
    CONFIG_MEMBER_TAB           ("member_tab",          null),
    CONFIG_LOCAL                ("local",               Locale.CHINA),

    CONFIG_DETAILED_USER_INFO               ("detailed_user_info",            false),
    CONFIG_TOPIC_CREATE_INSTEAD_TOUCHED     ("topic_created_instead_touched",    true),
    CONFIG_NODE_NAME_INSTEAD_TITLE          ("node_name_instead_title",   true),
    CONFIG_DATE_FORMAT                      ("date_format", "yyyy/MM/dd HH:mm"),
    CONFIG_REPLY_LINE_HEIGHT                ("reply_line_height",   1.3f),
    CONFIG_REPLY_FROM_API                   ("get_reply_from_api",  false),

    DEFAULT("default", null);

    private String key;
    private Serializable defaultValue;

    static EnumFindHelper<ConfigRefEnum, String> sFindHelper =
            new EnumFindHelper<>(ConfigRefEnum.class, ConfigRefEnum::getKey);

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

    public static ConfigRefEnum getByDescriptor(String key){

        return sFindHelper.find(key, DEFAULT);
    }
}
