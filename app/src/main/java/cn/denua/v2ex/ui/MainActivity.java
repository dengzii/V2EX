/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.ui;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.RequestFuture;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.denua.v2ex.R;
import cn.denua.v2ex.adapter.MainPagerAdapter;
import cn.denua.v2ex.base.BaseNetworkActivity;
import cn.denua.v2ex.fragment.TopicFragment;
import cn.denua.v2ex.http.RetrofitManager;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Account;
import cn.denua.v2ex.service.LoginService;
import cn.denua.v2ex.utils.Config;
import cn.denua.v2ex.utils.StatusBarUtil;
import cn.denua.v2ex.wiget.MessageDialog;

@SuppressWarnings("RedundantCast")
public class MainActivity extends BaseNetworkActivity implements NavigationView.OnNavigationItemSelectedListener, ResponseListener<Account> {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        StatusBarUtil.hideActionBar(this);
        ButterKnife.bind(this);
        initView();

        if (Config.restoreAccount()){
            new LoginService<>(this).getInfo(this);
        }
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView(){

        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.toolbar_main);

        tabLayout.setupWithViewPager(viewPager);

        for (String s:Config.HOME_TAB_TITLES){
            tabLayout.addTab(tabLayout.newTab());
            topicFragments.add(TopicFragment.newInstance(s));
        }

        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(Gravity.START));
        navigationView.setNavigationItemSelectedListener(this);

        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), topicFragments));

        ivUserPic = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.iv_user_pic);
        tvUserName = (TextView ) navigationView.getHeaderView(0).findViewById(R.id.tv_username);
        tvBalance = (TextView) navigationView.getHeaderView(0).findViewById(R.id.balance);

        miLogin = navigationView.getMenu().findItem(R.id.it_login_out);

        ivUserPic.setImageResource(R.drawable.ic_launcher_foreground);
        tvUserName.setText(getResources().getText(R.string.not_login));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main, menu);
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
                Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();
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
                break;
            case R.id.it_check:
                break;
            case R.id.it_node:
                break;
            case R.id.it_following:
                break;
            case R.id.it_post:
                break;
            case R.id.it_message:
                break;
            case R.id.it_login_out:
                changeUserStatus();
                break;
            default:break;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case LOGIN_REQUEST_CODE:
                if (resultCode == LoginActivity.RESULT_SUCCESS)
                    setUserStatus();
                break;
            default:
                break;
        }
    }

    private void changeUserStatus(){

        drawerLayout.closeDrawer(Gravity.START);
        if (Config.IsLogin){
            MessageDialog dialog = new MessageDialog();
            dialog.setTitle("提示");
            dialog.setMessage("登出?");
            dialog.setConfirmListener(this::logout);
            dialog.show(getSupportFragmentManager(), "logout_confirm");
        }else{
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void logout(){

        Config.IsLogin = false;
        Config.account = new Account();
        Config.persistentAccount();
        RetrofitManager.clearCookies();
        setUserStatus();
    }

    private void setUserStatus(){

        if (Config.IsLogin){
            miLogin.setIcon(R.drawable.ic_logout);
            Glide.with(this).load(Config.account.getAvatar_large()).into(ivUserPic);
            tvUserName.setText(Config.account.getUsername());
            miLogin.setTitle(R.string.logout);
            tvBalance.setText(String.valueOf(Config.account.getBalance()));
        }else{
            tvUserName.setText(R.string.not_login);
            miLogin.setTitle(R.string.login);
            tvBalance.setText(R.string.zero);
            miLogin.setIcon(R.drawable.ic_login);
            ivUserPic.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }
    @Override
    public void onFailed(String msg) {
        Config.IsLogin = false;
        ToastUtils.showShort(msg);
    }
    @Override
    public void onComplete(Account result) {
        Config.account = result;
        Config.IsLogin = true;
        setUserStatus();
    }

}
