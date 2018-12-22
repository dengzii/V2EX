/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import cn.denua.v2ex.R;

/*
 * @author denua
 * @email denua@foxmail.com
 * @date 2018/12/22 16
 */
public class AboutPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_about);
    }

}
