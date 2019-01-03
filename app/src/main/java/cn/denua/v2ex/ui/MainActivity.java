/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.ui;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;

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
import cn.denua.v2ex.widget.MessageDialog;

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
    private MenuItem miSignIn;

    private UserService mUserService = new UserService(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setThemeNoActionBar();
        setContentView(R.layout.act_main);

        ButterKnife.bind(this);
        initView();
    }

    protected void initView(){

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

        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), topicFragments));

        ivUserPic = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.iv_user_pic);
        tvUserName = (TextView ) navigationView.getHeaderView(0).findViewById(R.id.tv_username);
        tvBalance = (TextView) navigationView.getHeaderView(0).findViewById(R.id.balance);

        miLogin = navigationView.getMenu().findItem(R.id.it_login_out);
        miSignIn = navigationView.getMenu().findItem(R.id.it_check);

        ivUserPic.setImageResource(R.drawable.ic_offline);
        tvUserName.setText(getResources().getText(R.string.click_to_login));
        ivUserPic.setOnClickListener(v -> {
            if (!Config.IsLogin){
                changeUserStatus();
            }
        });
        tvUserName.setOnClickListener(v -> {
            if (!Config.IsLogin){
                changeUserStatus();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (Config.restoreAccount(this)){
            mUserService.getInfo(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!PermissionUtils.isGranted("android.permission.WRITE_EXTERNAL_STORAGE")){
            MessageDialog messageDialog = new MessageDialog(this);
            messageDialog.init(
                    R.string.alert,
                    R.string.need_storage_permission,
                    (dialog, which) -> PermissionUtils.launchAppDetailsSettings());
            messageDialog.showDialog();
        }
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
                if (Config.IsLogin)
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
                if (resultCode == LoginActivity.RESULT_SUCCESS)
                    setUserStatus();
                break;
            default:
                break;
        }
    }

    private void signIn(){

        if (Config.IsLogin){
            UserService.signIn(false, new ResponseListener<Integer>() {
                @Override
                public void onComplete(Integer result) {
                    ToastUtils.showShort("签到成功, 连续签到天数 " + result.toString());
                    updateSignInMenu(result, false);
                }
                @Override
                public void onFailed(String msg) {
                    ToastUtils.showShort(msg);
                }
            });
        }else{
            ToastUtils.showShort("需要登录");
        }
    }

    private void changeUserStatus(){

        if (Config.IsLogin){
            MessageDialog dialog = new MessageDialog(this);
            dialog.init(getResources().getString(R.string.alert),
                        getResources().getString(R.string.are_you_sure_logout),
                        (dialog1, which) -> logout());
            dialog.setNegativeCallBack((dialog12, which) -> dialog12.dismiss());
            dialog.showDialog();
        } else{
            startActivityForResult(new Intent(this, LoginActivity.class),
                    LOGIN_REQUEST_CODE);
        }
    }

    private void logout(){

        Config.IsLogin = false;
        Config.sAccount = new Account();
        Config.persistentAccount(this);
        RetrofitManager.clearCookies();
        setUserStatus();
    }

    private void setUserStatus(){

        if (Config.IsLogin){
            miLogin.setIcon(R.drawable.ic_logout);
            miLogin.setEnabled(true);
            miLogin.setVisible(true);
            Glide.with(this).load(Config.sAccount.getAvatar_large()).into(ivUserPic);
            tvUserName.setText(Config.sAccount.getUsername());
            tvBalance.setText(String.valueOf(Config.sAccount.getBalance()));
        }else{
            tvUserName.setText(R.string.click_to_login);
            miLogin.setVisible(false);
            miLogin.setEnabled(false);
            tvBalance.setText(R.string.zero);
            ivUserPic.setImageResource(R.drawable.ic_launcher_foreground);
        }
        invalidateOptionsMenu();
    }

    @Override
    public void onFailed(String msg) {

        Config.IsLogin = false;
        ToastUtils.showShort(msg);
    }

    @Override
    public void onComplete(Account result) {
        Config.sAccount = result;
        Config.IsLogin = true;
        setUserStatus();
        UserService.signIn(true, new ResponseListener<Integer>() {
            @Override
            public void onComplete(Integer result) {
                ToastUtils.showShort(result);
                if (result <= -1){
                    updateSignInMenu(result * -1, false);
                }else if (result > 0){
                    updateSignInMenu(result, true);
                }
            }
            @Override
            public void onFailed(String msg) {
                ToastUtils.showShort(msg);
            }
        });
    }

    private void updateSignInMenu(int days, boolean enabled){

        miSignIn.setTitle("已连续签到 " + days + " 天");
        miSignIn.setEnabled(enabled);
    }

}
