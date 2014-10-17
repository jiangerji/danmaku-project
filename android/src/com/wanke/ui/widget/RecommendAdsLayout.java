package com.wanke.ui.widget;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wanke.network.http.CommonHttpUtils;
import com.wanke.network.http.Constants;
import com.wanke.tv.R;
import com.wanke.ui.UiUtils;

public class RecommendAdsLayout {
    private final static String TAG = "recommend";

    private ViewPager mAdViewPager = null;
    private Context mContext = null;
    private View mRootView;
    private DisplayImageOptions mOptions;

    public RecommendAdsLayout(Context context) {
        mContext = context;
        initView(mContext);
    }

    private AdvAdapter mAdapter;

    private BottomIndicator mIndicator;

    private TextView mTitle;
    private ArrayList<String> mAdTitles = new ArrayList<String>();

    private void initView(Context context) {
        mOptions = UiUtils.getOptionsFadeIn();

        mRootView = View.inflate(context, R.layout.recommend_top_ad, null);
        mAdViewPager = (ViewPager) mRootView.findViewById(R.id.adv_pager);
        mTitle = (TextView) mRootView.findViewById(R.id.ad_title);

        int height = UiUtils.getScreenWidth(mContext) * 480
                / 854;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                height);
        mAdViewPager.setLayoutParams(layoutParams);

        mAdapter = new AdvAdapter();
        mAdViewPager.setAdapter(mAdapter);
        mAdViewPager.setOnPageChangeListener(new AdPageChangeListener());
        initAdapter();

        mIndicator = (BottomIndicator) mRootView.findViewById(R.id.indicator);
        mIndicator.setNumber(mAdapter.getCount());
        mIndicator.setSelection(0);
    }

    private Runnable mScrollRunnable = new Runnable() {

        @Override
        public void run() {
            mAdViewPager.setCurrentItem((mAdViewPager.getCurrentItem() + 1)
                    % mAdapter.getCount(), true);

            mHandler.postDelayed(mScrollRunnable, 5000);
        }
    };

    public View getView() {
        return mRootView;
    }

    private void initAdapter() {
        CommonHttpUtils.get("ads",
                new RequestCallBack<String>() {
                    @Override
                    public void onLoading(
                            long total, long current, boolean isUploading) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String content = responseInfo.result;
                        parseContent(content);
                    }

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                    }
                });
    }

    private void parseContent(String content) {
        try {
            JSONObject object = new JSONObject(content);
            JSONArray ads = object.getJSONArray("data");

            for (int i = 0; i < ads.length(); i++) {
                JSONObject ad = (JSONObject) ads.get(i);

                String cover = ad.getString("cover");
                mAdapter.addAdv(i, Constants.buildImageUrl(cover));
                mAdTitles.add(ad.getString("title"));
            }
            Log.d(TAG, "Refresh ad");
            mHandler.sendEmptyMessage(GET_AD_FINISH);
        } catch (Exception e) {
        }
    }

    private void setAdTitle(int position) {
        if (position < mAdTitles.size()) {
            mTitle.setText(mAdTitles.get(position));
        }
    }

    private final static int GET_AD_FINISH = 1;
    private Handler mHandler = new Handler(new Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
            case GET_AD_FINISH:
                mHandler.postDelayed(mScrollRunnable, 2500);
                mAdapter.update(mAdViewPager.getCurrentItem());
                setAdTitle(mAdViewPager.getCurrentItem());
                mAdapter.notifyDataSetChanged();
                break;

            default:
                break;
            }
            return true;
        }
    });

    private final class AdPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            if (mAdTitles.size() > position) {
                mTitle.setText(mAdTitles.get(position));
            }

            mIndicator.setSelection(position);
        }
    }

    private final class AdvAdapter extends PagerAdapter {
        private ArrayList<ImageView> images = new ArrayList<ImageView>();
        private ArrayList<String> mAdvCovers = new ArrayList<String>();

        private AdvAdapter() {
            for (int i = 0; i < 4; i++) {
                ImageView imageView = new ImageView(mContext);
                imageView.setScaleType(ScaleType.FIT_XY);
                images.add(imageView);
                mAdvCovers.add(null);
            }
        }

        public void addAdv(int index, String url) {
            mAdvCovers.set(index, url);
        }

        // 获取要滑动的控件的数量，在这里我们以滑动的广告栏为例，那么这里就应该是展示的广告图片的ImageView数量
        @Override
        public int getCount() {
            return 4;
        }

        // 来判断显示的是否是同一张图片，这里我们将两个参数相比较返回即可
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        // PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            view.removeView(images.get(position));
        }

        // 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            ImageView imageView = images.get(position);
            if (mAdvCovers.get(position) != null) {
                Log.d(TAG, "show " + position + " " + mAdvCovers.get(position));
                ImageLoader.getInstance()
                        .displayImage(mAdvCovers.get(position),
                                imageView, mOptions);
            }

            view.addView(imageView);
            return images.get(position);
        }

        public void update(int position) {
            ImageView imageView = images.get(position);
            if (mAdvCovers.get(position) != null) {
                Log.d(TAG, "show " + position + " " + mAdvCovers.get(position));
                ImageLoader.getInstance()
                        .displayImage(mAdvCovers.get(position),
                                imageView, mOptions);
            }
        }
    }
}
