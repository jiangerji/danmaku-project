package com.wanke;

import android.app.Application;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class WankeTVApplication extends Application {

    private static WankeTVApplication mCurrentApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();

        mCurrentApplication = this;

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).threadPriority(Thread.NORM_PRIORITY + 1)
                .threadPoolSize(5).denyCacheImageMultipleSizesInMemory()
                .memoryCache(new WeakMemoryCache())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    public static WankeTVApplication getCurrentApplication() {
        return mCurrentApplication;
    }
}
