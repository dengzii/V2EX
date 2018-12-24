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

import cn.denua.v2ex.Config;
import cn.denua.v2ex.ConfigRefEnum;
import cn.denua.v2ex.R;
import cn.denua.v2ex.utils.StringUtil;

/*
 * @author denua
 * @email denua@foxmail.com
 * @date 2018/12/22 16
 */
public class GeneralPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(Config.PREFERENCES_SETTINGS);
        addPreferencesFromResource(R.xml.pref_general);

        ListPreference listPreference = (ListPreference) findPreference(
                getString(R.string.key_theme));
        listPreference.setSummary(Config.getConfig(ConfigRefEnum.CONFIG_THEME));

        ListPreference dateFormatPreference = (ListPreference)findPreference(
                getString(R.string.key_date_format));
        String[] dateEntries = new String[dateFormatPreference.getEntryValues().length];
        int i = 0;
        for (CharSequence s:dateFormatPreference.getEntryValues()){
            String date = StringUtil.getDateNow(String.valueOf(s));
            dateEntries[i++] = date;
        }
        dateFormatPreference.setEntries(dateEntries);
        dateFormatPreference.setSummary(StringUtil.getDateNow(Config.getConfig(
                ConfigRefEnum.CONFIG_DATE_FORMAT)));
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
}
