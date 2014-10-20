package com.wanke.ui.adapter;

import java.util.Hashtable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.wanke.ui.fragment.FragmentGame;
import com.wanke.ui.fragment.FragmentLive;
import com.wanke.ui.fragment.FragmentRecommend;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private Hashtable<Integer, Fragment> mFragmentTable = new Hashtable<Integer, Fragment>();

    private Class<?> mFragmentClass[] = { FragmentRecommend.class,
            FragmentLive.class, FragmentGame.class };

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public CharSequence getPageTitle(int position) {
        return "";
    }

    @Override
    public int getCount() {
        return mFragmentClass.length;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = mFragmentTable.get(position);
        if (fragment == null) {
            Class<?> fragmentClass = mFragmentClass[position];
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                Log.d("excetpion", "MyFragmentPagerAdapter:" + e.toString());
            }
        }
        return fragment;
    }

}
