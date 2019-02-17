package cn.denua.v2ex.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

import cn.denua.v2ex.Config;
import cn.denua.v2ex.ConfigRefEnum;
import cn.denua.v2ex.Tab;
import cn.denua.v2ex.TabEnum;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;
    private List<Tab> titles;

    public MainPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList){
        super(fragmentManager);
        this.fragmentList = fragmentList;
        this.titles = Config.getConfig(ConfigRefEnum.CONFIG_HOME_TAB);
//        fragmentManager.beginTransaction().commitAllowingStateLoss();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        Tab tabEnum = titles.get(position);
        return tabEnum.getName() != null ? tabEnum.getName() : tabEnum.getTitle();
    }

}
