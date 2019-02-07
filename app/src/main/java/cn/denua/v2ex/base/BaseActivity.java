package cn.denua.v2ex.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.BarUtils;

import cn.denua.v2ex.Config;
import cn.denua.v2ex.ConfigRefEnum;
import cn.denua.v2ex.R;

import static cn.denua.v2ex.utils.StatusBarUtil.getStatusBarHeight;

/*
 * base activity
 *
 * @user denua
 * @date 2018/10/20
 */
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    private ViewGroup mViewRoot;
    private FrameLayout mFlContainer;
    private Toolbar mToolBar;

    private boolean mInForeground = false;
    private boolean mIsToolBar = true;
    protected int mStatusBarHeight;
    protected int mNavBarHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        UnexpectedExceptionHandler.getInstance().init();

        mStatusBarHeight = BarUtils.getStatusBarHeight();
        mNavBarHeight = BarUtils.getNavBarHeight();
        setTheme();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        super.setContentView(R.layout.w_layout_act_root);

        mViewRoot = find(R.id.v_root);
        if (!mIsToolBar){
            mViewRoot.removeView(find(R.id.ll_toolbar));
        }else{
            initToolBar();
        }
        mFlContainer = find(R.id.fl_root_container);
        View bar = find(R.id.v_root_top);
        bar.getLayoutParams().height = mStatusBarHeight;
        bar.setBackgroundColor(getResolveAttr(R.attr.attr_color_primary_dark));
        LayoutInflater.from(this).inflate(layoutResID, mFlContainer,true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mInForeground = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mInForeground = false;
    }

    @CallSuper
    protected void initView(){
        
    }

    protected void setNoToolbar(){
        this.mIsToolBar = false;
    }

    protected void setSwipeRefreshTheme(SwipeRefreshLayout swipeRefreshLayout){

        TypedValue colorAccent = new TypedValue();
        TypedValue colorPrimaryDark = new TypedValue();
        getTheme().resolveAttribute(R.attr.attr_color_accent,colorAccent,
                true);
        getTheme().resolveAttribute(R.attr.attr_color_primary_dark,
                colorPrimaryDark, true);
        swipeRefreshLayout.setColorSchemeResources(colorAccent.resourceId,
                colorPrimaryDark.resourceId);
    }

    protected void setTheme(){
        setTheme(getCurrentThemeId());
    }

    protected void initToolBar(){

        mToolBar = find(R.id.ll_toolbar).findViewById(R.id.toolbar);
        mToolBar.setElevation(5);
        mToolBar.setNavigationOnClickListener(v -> finish());
    }

    protected int getResolveAttr(@AttrRes int attr){
        TypedValue typedColor = new TypedValue();
        getTheme().resolveAttribute(attr, typedColor, true);
        return typedColor.data;
    }

    protected int getCurrentThemeId(){
        return getResources().getIdentifier(Config.getConfig(ConfigRefEnum.CONFIG_THEME),
                "style", getPackageName());
    }

    protected int getColorAttr(@AttrRes int attr){

        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }

    public int getColorAccent(){

        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.attr_color_accent, typedValue, true);
        return typedValue.data;
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T find(@IdRes int id){
        return (T) findViewById(id);
    }

    protected boolean  isInForeground(){
        return this.mInForeground;
    }

    protected void addStatusBarPlaceHolder(){

        ViewGroup rootView = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.setPadding(0, getStatusBarHeight(this), 0, 0);
    }
}
