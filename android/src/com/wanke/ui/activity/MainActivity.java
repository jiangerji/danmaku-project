package com.wanke.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.wanke.tv.R;
import com.wanke.ui.adapter.MyFragmentPagerAdapter;

public class MainActivity extends BaseActivity {
    MyFragmentPagerAdapter mMyFragmentPagerAdapter;
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private View mRecommentBtn = null;
    private View mLiveBtn = null;
    private View mGameBtn = null;
    private View mAccountBtn = null;

    private MyFragmentPagerAdapter mFragmentPagerAdapter;

    /*
     * 初始化ViewPager
     */
    public void initView() {
        mPager = (ViewPager) findViewById(R.id.viewpager);

        // 给ViewPager设置适配器
        mFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mPager.setOffscreenPageLimit(mFragmentPagerAdapter.getCount());
        mPager.setAdapter(mFragmentPagerAdapter);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        //        mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器
        //        mPager.setOnDragListener(l);

        mRecommentBtn = findViewById(R.id.recomment_btn);
        mRecommentBtn.setOnClickListener(mBtnClickListener);
        mLiveBtn = findViewById(R.id.live_btn);
        mLiveBtn.setOnClickListener(mBtnClickListener);
        mGameBtn = findViewById(R.id.game_btn);
        mGameBtn.setOnClickListener(mBtnClickListener);
        mAccountBtn = findViewById(R.id.account_btn);
        mAccountBtn.setOnClickListener(mBtnClickListener);

        setCurrentPage(0);// 设置当前显示标签页为第一页
    }

    private OnClickListener mBtnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            int pageIndex = -1;
            switch (id) {
            case R.id.recomment_btn:
                pageIndex = 0;
                break;

            case R.id.live_btn:
                pageIndex = 1;
                break;

            case R.id.game_btn:
                pageIndex = 2;
                break;

            case R.id.account_btn:
                break;

            default:
                break;
            }

            if (pageIndex >= 0 && pageIndex < mFragmentPagerAdapter.getCount()) {
                setCurrentPage(pageIndex);
            }
        }
    };

    private void setCurrentPage(int index) {
        if (index >= 0 && index < mFragmentPagerAdapter.getCount()) {
            mPager.setCurrentItem(index, true);

            mAccountBtn.setSelected(false);
            mGameBtn.setSelected(false);
            mLiveBtn.setSelected(false);
            mRecommentBtn.setSelected(false);

            switch (index) {
            case 0:
                mRecommentBtn.setSelected(true);
                break;

            case 1:
                mLiveBtn.setSelected(true);
                break;

            case 2:
                mGameBtn.setSelected(true);
                break;

            case 3:
                mAccountBtn.setSelected(true);
                break;

            default:
                break;
            }
        }
    }

    private class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int state) {
            Log.d(TAG, "onPageScrollStateChanged:" + state);
        }

        @Override
        public void onPageScrolled(
                int position, float positionOffset, int positionOffsetPixels) {
            Log.d(TAG, "onPageScrolled:" + position + " " + positionOffset
                    + " " + positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            Log.d(TAG, "onPageSelected:" + position);
            setCurrentPage(position);
        }

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
