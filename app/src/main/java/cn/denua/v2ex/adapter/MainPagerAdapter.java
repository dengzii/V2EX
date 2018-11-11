package cn.denua.v2ex.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;

import cn.denua.v2ex.Config;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;
    private List<String> titles;

    public MainPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList){
        super(fragmentManager);
        this.fragmentList = fragmentList;
        this.titles = Config.HOME_TAB_TITLES;
//        fragmentManager.beginTransaction().commitAllowingStateLoss();
    }

//    @Override
//    public int getItemPosition(@NonNull Object object) {
//        return fragmentList.indexOf(object);
//    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

//    @Override
//    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
//        return view == object;
//    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    //    @NonNull
//    @Override
//    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//
//        return fragmentList.get(position);
//    }
//
//    @Override
//    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        fragmentList.remove(position);
//    }
}
