/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;
import cn.denua.v2ex.R;
import cn.denua.v2ex.base.BaseActivity;

/*
 * settings
 *
 * @author denua
 * @date 2018/11/03 16
 */
public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_settings);
        ButterKnife.bind(this);
    }
}
