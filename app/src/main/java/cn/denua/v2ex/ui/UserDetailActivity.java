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
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.orhanobut.logger.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.denua.v2ex.Config;
import cn.denua.v2ex.ConfigRefEnum;
import cn.denua.v2ex.R;
import cn.denua.v2ex.adapter.MemberPagerAdapter;
import cn.denua.v2ex.base.BaseNetworkActivity;
import cn.denua.v2ex.fragment.MemberTopicFragment;
import cn.denua.v2ex.interfaces.IResponsibleView;
import cn.denua.v2ex.interfaces.ResponseListener;
import cn.denua.v2ex.model.Member;
import cn.denua.v2ex.service.MemberService;
import cn.denua.v2ex.utils.ImageLoader;
import cn.denua.v2ex.utils.StringUtil;

/*
 * User detail page
 *
 * @author denua
 * @date 2018/11/04 20
 */
public class UserDetailActivity extends BaseNetworkActivity implements ResponseListener<Member> {

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
    private List<Fragment> mMemberTopicFragments = new ArrayList<>();

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

        if (mMember.getCreated() == 0) {
            new MemberService (this, this)
                .getMemberDetail(mMember.getUsername());
        }else {
            mNumberCreated.setText(StringUtil.timestampToStr(mMember.getCreated()));
        }
    }

    @Override
    protected void initView() {

        mToolbar.setTitle(mMember.getUsername());
        mToolbar.inflateMenu(R.menu.menu_user_detail);

        mTabLayout.setupWithViewPager(mViewPager);

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
        getMenuInflater().inflate(R.menu.menu_user_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCompleteRequest() {

        ToastUtils.showShort("onCompleteRequest");
    }

    @Override
    public int getContextStatus() {
        return IResponsibleView.VIEW_STATUS_ACTIVATED;
    }

    @Override
    public void onStartRequest() {

        ToastUtils.showShort("onStartRequest");
    }

    @Override
    public void onComplete(Member result) {

        Logger.e("Result", result);
        this.mNumberCreated.setText(StringUtil.timestampToStr(result.getCreated()));
    }

    @Override
    public void onFailed(String msg) {
        ToastUtils.showShort(msg);
    }
}














