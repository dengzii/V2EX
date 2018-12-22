/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.List;

import cn.denua.v2ex.R;

/*
 * @author denua
 * @date 2018/11/03 16
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        super.onBuildHeaders(target);
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return true;
    }
}
