package cn.denua.v2ex.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import cn.denua.v2ex.R;
import cn.denua.v2ex.base.BaseActivity;

public class WelcomeActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThemeNoActionBar();
        setContentView(R.layout.act_welcome);

    }
}

