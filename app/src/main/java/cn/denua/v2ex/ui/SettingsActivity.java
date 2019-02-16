/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.ui;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.denua.v2ex.Config;
import cn.denua.v2ex.R;
import cn.denua.v2ex.base.BaseActivity;
import cn.denua.v2ex.fragment.GeneralPreferenceFragment;

/*
 * @author denua
 * @date 2018/11/03 16
 */
public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_settings);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_content, new GeneralPreferenceFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Config.init(this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
