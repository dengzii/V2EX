/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.ui;

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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.denua.v2ex.R;
import cn.denua.v2ex.adapter.MainPagerAdapter;
import cn.denua.v2ex.base.BaseActivity;
import cn.denua.v2ex.fragment.TopicFragment;
import cn.denua.v2ex.utils.Config;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

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

    private List<Fragment> topicFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        ButterKnife.bind(this);
        initView();
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

        // viewPager Fragment
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), topicFragments));
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
                Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
