/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.View;

import cn.denua.v2ex.Config;
import cn.denua.v2ex.R;

/*
 * @author denua
 * @email denua@foxmail.com
 * @date 2018/12/22 16
 */
public class GeneralPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(Config.PREFERENCES_NAME);
        addPreferencesFromResource(R.xml.pref_general);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
