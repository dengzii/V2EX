package cn.denua.v2ex.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;

    public MainViewPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList){
        super(fragmentManager);
        this.fragmentList = fragmentList;
        fragmentManager.beginTransaction().commitAllowingStateLoss();
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
