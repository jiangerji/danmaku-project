package com.wanke.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.wanke.tv.R;
import com.wanke.ui.adapter.MyFragmentPagerAdapter;

public class MainActivity extends BaseActivity {
    MyFragmentPagerAdapter mMyFragmentPagerAdapter;
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitViewPager();
    }

    public class txListener implements View.OnClickListener {
        private int index = 0;

        public txListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    }

    /*
     * 初始化ViewPager
     */
    public void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.viewpager);

        // 给ViewPager设置适配器
        MyFragmentPagerAdapter viewerPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mPager.setOffscreenPageLimit(viewerPagerAdapter.getCount());
        mPager.setAdapter(viewerPagerAdapter);
        mPager.setCurrentItem(0);// 设置当前显示标签页为第一页
        //        mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器
    }
    // /
    //    private int first = 0;
    //    private int second = 0;
    //    private int third = 0;
    //    private int currPosition = 0;
    //
    //    public class MyOnPageChangeListener implements OnPageChangeListener {
    //        private int one = mShiftP * 2 + mYwidth;// 两个相邻页面的偏移量
    //
    //        @Override
    //        public void onPageScrolled(int arg0, float arg1, int arg2) {
    //            // TODO Auto-generated method stub
    //        }
    //
    //        @Override
    //        public void onPageScrollStateChanged(int arg0) {
    //            // TODO Auto-generated method stub
    //        }
    //
    //        @Override
    //        public void onPageSelected(int arg0) {
    //            Log.d("onchanged", "onchanged " + arg0);
    //            TranslateAnimation ta = null;
    //            switch (arg0) {
    //            case 0:
    //                view1.setBackgroundResource(R.color.bisque);
    //                view2.setBackgroundResource(R.color.divider);
    //                if (currPosition == 1) {
    //                    view2.setBackgroundResource(R.color.divider);
    //                    ta = new TranslateAnimation(first, 0, 0, 0);
    //                }
    //                if (currPosition == 2) {
    //                    view3.setBackgroundResource(R.color.divider);
    //                    ta = new TranslateAnimation(second, 0, 0, 0);
    //                }
    //                if (currPosition == 3) {
    //                    view4.setBackgroundResource(R.color.divider);
    //                    ta = new TranslateAnimation(third, 0, 0, 0);
    //                }
    //                break;
    //
    //            case 1:
    //                view2.setBackgroundResource(R.color.bisque);
    //                view3.setBackgroundResource(R.color.divider);
    //                if (currPosition == 0) {
    //                    view1.setBackgroundResource(R.color.divider);
    //                    ta = new TranslateAnimation(0, first, 0, 0);
    //                }
    //                if (currPosition == 2) {
    //                    view3.setBackgroundResource(R.color.divider);
    //                    ta = new TranslateAnimation(second, first, 0, 0);
    //                }
    //                if (currPosition == 3) {
    //                    view4.setBackgroundResource(R.color.divider);
    //                    ta = new TranslateAnimation(third, first, 0, 0);
    //                }
    //                break;
    //
    //            case 2:
    //                view3.setBackgroundResource(R.color.bisque);
    //                view2.setBackgroundResource(R.color.divider);
    //                if (currPosition == 0) {
    //                    view1.setBackgroundResource(R.color.divider);
    //                    ta = new TranslateAnimation(0, second, 0, 0);
    //                }
    //                if (currPosition == 1) {
    //                    view2.setBackgroundResource(R.color.divider);
    //                    ta = new TranslateAnimation(first, second, 0, 0);
    //                }
    //                if (currPosition == 3) {
    //                    view4.setBackgroundResource(R.color.divider);
    //                    ta = new TranslateAnimation(third, second, 0, 0);
    //                }
    //                break;
    //
    //            case 3:
    //                view4.setBackgroundResource(R.color.bisque);
    //                view3.setBackgroundResource(R.color.divider);
    //                if (currPosition == 0) {
    //                    view1.setBackgroundResource(R.color.divider);
    //                    ta = new TranslateAnimation(0, third, 0, 0);
    //                }
    //                if (currPosition == 1) {
    //                    view2.setBackgroundResource(R.color.divider);
    //                    ta = new TranslateAnimation(first, third, 0, 0);
    //                }
    //                if (currPosition == 2) {
    //                    view3.setBackgroundResource(R.color.divider);
    //                    ta = new TranslateAnimation(second, third, 0, 0);
    //                }
    //                break;
    //
    //            }
    //            currPosition = arg0;
    //        }
    //    }

}
