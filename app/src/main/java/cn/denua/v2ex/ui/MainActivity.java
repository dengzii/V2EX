/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.denua.v2ex.ConfigRefEnum;
import cn.denua.v2ex.R;
import cn.denua.v2ex.TabEnum;
import cn.denua.v2ex.adapter.MainPagerAdapter;
import cn.denua.v2ex.base.BaseNetworkActivity;
import cn.denua.v2ex.fragment.TopicFragment;
import cn.denua.v2ex.http.RetrofitManager;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Account;
import cn.denua.v2ex.service.UserService;
import cn.denua.v2ex.Config;
import cn.denua.v2ex.utils.DialogUtil;

@SuppressWarnings("RedundantCast")
public class MainActivity extends BaseNetworkActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = "MainActivity";
    private final int LOGIN_REQUEST_CODE = 100;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.navigation)
    NavigationView navigationView;
    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.view_pager_tab)
    TabLayout tabLayout;

    private ImageView ivUserPic;
    private TextView tvUserName;
    private TextView tvBalance;

    private List<Fragment> topicFragments = new ArrayList<>();
    private MenuItem miLogin;
    private MenuItem miSignIn;
    private MenuItem miNotifications;

    private Account mAccount;
    /**
     * 负数表示签到天数, 今日已签到 <br>
     * 0 表示未连续签到, 今日未签到 <br>
     * 正数表示今日未签到 <br>
     */
    static int sSignIn = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNoToolbar();
        setContentView(R.layout.act_main);

        mAccount = Config.getAccount();
        ButterKnife.bind(this);
        initView();
        checkLoginAndSignStatus();
    }

    protected void initView(){
        super.initView();
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_toolbar_main);
        tabLayout.setupWithViewPager(viewPager);

        ArrayList<TabEnum> tabEnums = Config.getConfig(ConfigRefEnum.CONFIG_HOME_TAB);
        for (TabEnum s:tabEnums){
            tabLayout.addTab(tabLayout.newTab());
            topicFragments.add(TopicFragment.create(s));
        }

        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(Gravity.START));
        navigationView.setNavigationItemSelectedListener(this);

        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), topicFragments));

        ivUserPic = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.iv_user_pic);
        tvUserName = (TextView ) navigationView.getHeaderView(0).findViewById(R.id.tv_username);
        tvBalance = (TextView) navigationView.getHeaderView(0).findViewById(R.id.balance);

        miLogin = navigationView.getMenu().findItem(R.id.it_login_out);
        miSignIn = navigationView.getMenu().findItem(R.id.it_check);
        miNotifications = navigationView.getMenu().findItem(R.id.it_message);

        ivUserPic.setImageResource(R.drawable.ic_offline);
        tvUserName.setText(getResources().getText(R.string.click_to_login));
        ivUserPic.setOnClickListener(v -> {
            if (!mAccount.isLogin()){
                changeUserStatus();
            }
        });
        tvUserName.setOnClickListener(v -> {
            if (!mAccount.isLogin()){
                changeUserStatus();
            } else{
                UserDetailActivity.start(this, mAccount);
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Config.restoreState(savedInstanceState);
        Config.init(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Config.saveState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if (!PermissionUtils.isGranted("android.permission.WRITE_EXTERNAL_STORAGE")){
//            DialogUtil.showMessage(this,
//                    getString(R.string.alert),
//                    getString(R.string.need_storage_permission),
//                    value -> PermissionUtils.launchAppDetailsSettings());
//        }

        updateMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(Gravity.START)){
            drawerLayout.closeDrawer(Gravity.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                DialogUtil.showInputDialog(this, "查看话题", "输入话题ID", "", value ->
                        TopicActivity.start(MainActivity.this, Integer.valueOf(value)));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.it_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                finish();
                break;
            case R.id.it_check:
                signIn();
                break;
            case R.id.it_node:
                startActivity(new Intent(this, AllNodeActivity.class));
                break;
            case R.id.it_following:
                break;
            case R.id.it_post:
                if (mAccount.isLogin())
                    startActivity(new Intent(this, PostTopicActivity.class));
                break;
            case R.id.it_message:
                break;
            case R.id.it_login_out:
                changeUserStatus();
                break;
            default:break;
        }
        drawerLayout.closeDrawer(Gravity.START);
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case LOGIN_REQUEST_CODE:
                onActivityResultLogin(resultCode);
                break;
            default:
                break;
        }
    }

    private void onActivityResultLogin(int resultCode){

        if (resultCode == LoginActivity.RESULT_SUCCESS){
            mAccount = Config.getAccount();
            setUserStatus();
            checkDailySignIn();
        }
    }

    private void signIn(){

        if (!mAccount.isLogin() || sSignIn < 0){
            return;
        }
        UserService.signIn(false, new ResponseListener<Integer>() {
            @Override
            public void onComplete(Integer result) {
                ToastUtils.showShort("签到成功, 连续签到天数 " + result.toString());
                sSignIn = result;
                updateMenu();
            }
            @Override
            public void onFailed(String msg) {
                ToastUtils.showShort(msg);
            }
        });
    }

    private void changeUserStatus(){

        if (!mAccount.isLogin()){
            startActivityForResult(new Intent(this, LoginActivity.class),
                    LOGIN_REQUEST_CODE);
            return;
        }
        DialogUtil.showMessage(this,
                getString(R.string.alert), getString(R.string.are_you_sure_logout), value -> {
                    if (value){
                        logout();
                    }
                });
    }

    private void logout(){

        mAccount.logout();
        mAccount = new Account();
        sSignIn = 0;
        Config.persistentAccount(this);
        RetrofitManager.clearCookies();
        setUserStatus();
        updateMenu();
    }

    private void setUserStatus(){

        if (mAccount.isLogin()){
            miLogin.setIcon(R.drawable.ic_logout);
            miLogin.setEnabled(true);
            miLogin.setVisible(true);
            Glide.with(this).load(mAccount.getAvatar_large()).into(ivUserPic);
            tvUserName.setText(mAccount.getUsername());
            tvBalance.setText(String.valueOf(mAccount.getBalance()));
        }else{
            tvUserName.setText(R.string.click_to_login);
            miLogin.setVisible(false);
            miLogin.setEnabled(false);
            tvBalance.setText(R.string.zero);
            ivUserPic.setImageResource(R.drawable.ic_offline);
        }
        updateMenu();
    }

    private void updateMenu(){

        if (sSignIn == 0){
            miSignIn.setTitle(R.string.checked);
        }else {
            miSignIn.setTitle(sSignIn != 0 ? "已连续签到 " + Math.abs(sSignIn) + " 天" : getString(R.string.checked));
        }
        boolean enabled = sSignIn >= 0;
        TextView tvSignIn =  (TextView) miSignIn.getActionView().findViewById(R.id.tv_badge_msg);
        tvSignIn.setText(String.valueOf(Math.abs(mAccount.getSign())));
        miSignIn.setEnabled(enabled);
        miSignIn.setCheckable(enabled);
        TextView tvNotify = (TextView) miNotifications.getActionView().findViewById(R.id.tv_badge_msg);
        tvNotify.setText(String.valueOf(mAccount.getNotifications() == 0 ?"":mAccount.getNotifications()));
    }

    private void checkLoginAndSignStatus(){

        UserService.getInfo(new ResponseListener<Account>() {
            @Override
            public void onFailed(String msg) {
                mAccount.logout();
            }
            @Override
            public void onComplete(Account result) {
                Config.setAccount(result);
                checkDailySignIn();
                setUserStatus();
            }
        });
    }

    private void checkDailySignIn(){

        UserService.signIn(true, new ResponseListener<Integer>() {
            @Override
            public void onComplete(Integer result) {
                sSignIn = result;
                mAccount.setSign(result);
                if (sSignIn >= 0){
                    if (Config.getConfig(ConfigRefEnum.CONFIG_AUTO_CHECK)) signIn();
                }
                updateMenu();
                setUserStatus();
            }
            @Override
            public void onFailed(String msg) {
                ToastUtils.showShort(msg);
            }
        });
    }
}
