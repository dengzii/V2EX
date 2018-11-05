package cn.denua.v2ex.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import cn.denua.v2ex.utils.UnexpectedExceptionHandler;

import static cn.denua.v2ex.utils.StatusBarUtil.getStatusBarHeight;

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

        UnexpectedExceptionHandler.getInstance().init();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        ImmersionBar.with(this)
//                .navigationBarAlpha(0.2f)
//                .init();

//        ViewGroup viewGroup = (ViewGroup ) getWindow().getDecorView().findViewById(android.R.id.content);
//        BarUtils.addMarginTopEqualStatusBarHeight(viewGroup);

//        addStatusBarPlaceHolder();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void addStatusBarPlaceHolder(){

        ViewGroup rootView = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.setPadding(0, getStatusBarHeight(this), 0, 0);
    }
}
