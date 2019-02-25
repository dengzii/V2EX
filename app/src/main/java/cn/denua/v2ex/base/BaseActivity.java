package cn.denua.v2ex.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.BarUtils;

import cn.denua.v2ex.App;
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

    /**
     * 所有活动的布局都包含这个根布局, 这个布局包括  <br>
     * ToolBar <br>
     * 顶部填充的 statusBar 的高度  <br>
     * 全屏返回手势检测  <br>
     * 每个 Activity 的布局  <br>
     * 底部 NavBar 的填充高度  <br>
     */
    private ViewGroup mViewRoot;
    /**
     * 每个 Activity 的布局容器
     */
    private FrameLayout mFlContainer;
    private ViewGroup mToolBarContainer;
    private Toolbar mToolBar;

    /**
     * 当前 Activity 是否在前台, 用于后台耗时请求是否返回数据
     */
    private boolean mInForeground = false;
    private boolean mIsShowToolBar = true;
    protected int mStatusBarHeight;
    protected int mNavBarHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 未捕获的异常, 记录错误上传
//        UnexpectedExceptionHandler.getInstance().init();

        mStatusBarHeight = BarUtils.getStatusBarHeight();
        mNavBarHeight = BarUtils.getNavBarHeight();
        setTheme();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        super.setContentView(R.layout.w_layout_act_root);
        setRootView();
        LayoutInflater.from(this).inflate(layoutResID, mFlContainer,true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mInForeground = true;
        setFontScaleAndUiScale();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mInForeground = false;
    }

    @CallSuper
    protected void initView(){
        
    }

    /**
     * 隐藏 ToolBar, 需要在 setContentView 前调用
     */
    protected void setNoToolbar(){
        this.mIsShowToolBar = false;
    }

    /**
     * 许多 Activity 中都有用到 SwipeRefreshLayout, 在这统一添加这个方法设置颜色样式
     *
     * @param swipeRefreshLayout the swipeRefreshLayout
     */
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

    /**
     * 通过当前主题解析 attr 的真实值
     *
     * @param attr The attr id
     * @return The data of attr
     */
    protected int getResolveAttr(@AttrRes int attr){
        TypedValue typedColor = new TypedValue();
        getTheme().resolveAttribute(attr, typedColor, true);
        return typedColor.data;
    }

    /**
     * 获取当前主题的 Id
     * 主题保存在 Config 配置中
     *
     * @return The id of current theme
     */
    protected int getCurrentThemeId(){
        return getResources().getIdentifier(Config.getConfig(ConfigRefEnum.CONFIG_THEME),
                "style", getPackageName());
    }

    protected int getColorAttr(@AttrRes int attr){

        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }

    protected int getColorAccent(){

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

    /**
     * 配置字体和ui的缩放
     */
    protected void setFontScaleAndUiScale(){
        onConfigurationChanged(Config.getConfiguration());
        getResources().updateConfiguration(Config.getConfiguration(), getResources().getDisplayMetrics());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 初始化根布局
     */
    private void setRootView(){
        mViewRoot = find(R.id.v_root);
        mToolBarContainer = find(R.id.ll_toolbar);
        if (!mIsShowToolBar){
            mViewRoot.removeView(mToolBarContainer);
        }else{
            initToolBar();
        }
        mFlContainer = find(R.id.fl_root_container);
        View bar = find(R.id.v_root_top);
        bar.getLayoutParams().height = mStatusBarHeight;
        bar.setBackgroundColor(getResolveAttr(R.attr.attr_color_primary_dark));
    }

    private void initToolBar(){

        mToolBar = find(R.id.ll_toolbar).findViewById(R.id.toolbar);
        mToolBar.setElevation(5);
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationOnClickListener(v -> finish());
    }
}
