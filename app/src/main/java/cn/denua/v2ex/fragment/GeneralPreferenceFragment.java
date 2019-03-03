/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.fragment;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.Locale;

import cn.denua.v2ex.Config;
import cn.denua.v2ex.ConfigRefEnum;
import cn.denua.v2ex.R;
import cn.denua.v2ex.ui.CustomTabActivity;
import cn.denua.v2ex.utils.TimeUtil;

/*
 * @author denua
 * @email denua@foxmail.com
 * @date 2018/12/22 16
 */
public class GeneralPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(
                Config.getConfig(ConfigRefEnum.CONFIG_PREFERENCE_SETTING_FILE));
        addPreferencesFromResource(R.xml.pref_general);

        ListPreference themePreference = (ListPreference) findPreference(
                getString(R.string.key_theme));
        themePreference.setSummary(Config.getConfig(ConfigRefEnum.CONFIG_THEME));

        ListPreference dateFormatPreference = (ListPreference)findPreference(
                getString(R.string.key_date_format));
        String[] dateEntries = new String[dateFormatPreference.getEntryValues().length];
        int i = 0;
        for (CharSequence s:dateFormatPreference.getEntryValues()){
            String date = TimeUtil.getDateNow(String.valueOf(s));
            dateEntries[i++] = date;
        }
        dateFormatPreference.setEntries(dateEntries);
        dateFormatPreference.setSummary(TimeUtil.getDateNow(Config.getConfig(
                ConfigRefEnum.CONFIG_DATE_FORMAT)));

        ListPreference localePreference = (ListPreference) findPreference(getString(R.string.key_local));
        String[] localesEntryValue = new String[Config.LOCAL_LIST.size()];
        String[] localesEntries= new String[Config.LOCAL_LIST.size()];
        int index = 0;
        for (Locale locale:Config.LOCAL_LIST){
            localesEntryValue[index] = locale.toString();
            localesEntries[index++] = locale.getDisplayCountry();
        }
        localePreference.setDefaultValue(localesEntryValue[0]);
        localePreference.setEntryValues(localesEntryValue);
        localePreference.setEntries(localesEntries);

        Preference clearCachePreference = findPreference(getString(R.string.clear_cache));
        clearCachePreference.setOnPreferenceClickListener(this::clearCache);
        clearCachePreference.setSummary("缓存大小 12 MB");
        findPreference(getString(R.string.key_home_tabs)).setOnPreferenceClickListener(preference -> {
            CustomTabActivity.start(getActivity());
            return false;
        });
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Preference preference = findPreference(getString(R.string.key_theme));
        preference.setOnPreferenceChangeListener((preference1, newValue) -> {
            Config.setConfig(ConfigRefEnum.CONFIG_THEME, String.valueOf(newValue));
            getActivity().recreate();
            return true;
        });
    }

    private boolean clearCache(Preference preference) {

        return false;
    }
}
