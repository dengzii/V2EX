package cn.denua.v2ex.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gyf.barlibrary.ImmersionBar;

import cn.denua.v2ex.R;
import cn.denua.v2ex.utils.StatusBarUtil;
import cn.denua.v2ex.utils.UnexpectedExceptionHandler;

/*
 * base activity
 *
 * @user denua
 * @date 2018/10/20
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ImmersionBar.with(this)
//                .barColor(R.color.primary)
//                .navigationBarAlpha(0.01f)
//                .init();

        UnexpectedExceptionHandler.getInstance().init();
        StatusBarUtil.setTranslucentStatusBar(this);
        StatusBarUtil.fitSystemWindow(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }
}
