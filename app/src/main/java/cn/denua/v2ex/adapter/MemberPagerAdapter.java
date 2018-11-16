/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/*
 *
 *
 * @author denua
 * @date 2018/11/16 16
 */
public class MemberPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    private List<String> pageTitles;

    public MemberPagerAdapter(List<String> pageTitles,FragmentManager fragmentManager, List<Fragment> fragments){
        super(fragmentManager);
        this.pageTitles = pageTitles;
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

}
