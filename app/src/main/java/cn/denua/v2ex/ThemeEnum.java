/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex;

import cn.denua.v2ex.helper.EnumFindHelper;

/*
 * @author denua
 * @email denua@foxmail.com
 * @date 2018/12/23 16
 */
public enum ThemeEnum{

    MAIN_THEME  ("BlueGrey", R.style.MainTheme, 0),
    GREEN_THEME ("Green",   R.style.GreenTheme, 1),
    TEAL_THEME  ("Teal",    R.style.TealTheme, 2),
    ORANGE_THEME("Orange",  R.style.OrangeTheme, 3),
    INDIGO_THEME("Indigo",  R.style.IndigoTheme, 4);

    private String name;
    private int res;
    private int index;

    static EnumFindHelper<ThemeEnum, Integer> sFindHelperEnumOfIndex =
            new EnumFindHelper<>(ThemeEnum.class, ThemeEnum::getIndex);

    static EnumFindHelper<ThemeEnum, String> sFindHelperEnumOfDescriptor =
            new EnumFindHelper<>(ThemeEnum.class, ThemeEnum::getName);

    static EnumFindHelper<ThemeEnum, Integer> sFindHelperEnumOfThemeRes =
            new EnumFindHelper<>(ThemeEnum.class, ThemeEnum::getRes);

    ThemeEnum(String s, int i, int index) {
        this.name = s;
        this.res = i;
        this.index = index;
    }

    public String getName() {
        return name;
    }
    public int getRes() {
        return res;
    }

    public int getIndex() {
        return index;
    }

    public static ThemeEnum getByThemeRes(int res){
        return sFindHelperEnumOfThemeRes.find(res, MAIN_THEME);
    }

    public static ThemeEnum getByIndex(int index){
        return sFindHelperEnumOfIndex.find(index, MAIN_THEME);
    }
    public static ThemeEnum getByName(String name){
        return sFindHelperEnumOfDescriptor.find(name, MAIN_THEME);
    }
    public static int getThemeResByName(String name){
        return getByName(name).getRes();
    }
}