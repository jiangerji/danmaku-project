package com.wanke.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> list;
    List<String> titleList;

    public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list,
            List<String> titleList) {
        super(fm);
        this.list = list;
        this.titleList = titleList;
    }

    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int arg0) {
        return list.get(arg0);
    }

}
