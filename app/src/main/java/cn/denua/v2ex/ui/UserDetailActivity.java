/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.denua.v2ex.R;
import cn.denua.v2ex.adapter.MemberPagerAdapter;
import cn.denua.v2ex.base.BaseNetworkActivity;
import cn.denua.v2ex.fragment.MemberTopicFragment;
import cn.denua.v2ex.model.Member;
import cn.denua.v2ex.utils.ImageLoader;

/*
 * User detail page
 *
 * @author denua
 * @date 2018/11/04 20
 */
public class UserDetailActivity extends BaseNetworkActivity {

    private static final String EXTRA_MEMBER = "MEMBER";

    @BindView(R.id.iv_user_pic)
    ImageView mIVUserPic;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tab_pager)
    TabLayout mTabLayout;
    @BindView(R.id.vp_content)
    ViewPager mViewPager;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.tv_number_created)
    TextView mNumberCreated;


    private Member mMember;
    private MemberPagerAdapter mPagerAdapter;
    private List<Fragment> mMemberTopicFragments;

    public static void start(Context context, Member member){
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra(EXTRA_MEMBER, member);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThemeNoActionBar();
        setContentView(R.layout.act_user_detail);

        this.mMember = getIntent().getParcelableExtra(EXTRA_MEMBER);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void initView() {

        mMemberTopicFragments = new ArrayList<>();
        mToolbar.setTitle(mMember.getUsername());
        mTabLayout.setupWithViewPager(mViewPager);
        mNumberCreated.setText(mMember.getNumber());

        List<String> tabs = new ArrayList<String>(){{
            add("Post");
            add("Reply");
            add("Other");
        }};

        TabLayout.Tab tab = mTabLayout.newTab();
        mTabLayout.addTab(tab);

        mMemberTopicFragments.add(MemberTopicFragment.create(mMember));
        mMemberTopicFragments.add(MemberTopicFragment.create(mMember));
        mMemberTopicFragments.add(MemberTopicFragment.create(mMember));

        mPagerAdapter = new MemberPagerAdapter(tabs, getSupportFragmentManager(), mMemberTopicFragments);
        mViewPager.setAdapter(mPagerAdapter);

        ImageLoader.load(mMember.getAvatar_large(), mIVUserPic, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_user_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void onRefresh(){

        mPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCompleteRequest() {
        ToastUtils.showShort("onCompleteRequest");
    }

    @Override
    public void onStartRequest() {
        ToastUtils.showShort("onStartRequest");
    }
}














