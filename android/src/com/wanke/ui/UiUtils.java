package com.wanke.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.wanke.WankeTVApplication;

public class UiUtils {

    private static int mScreenWidth = 0;
    private static int mScreenHeight = 0;
    private static float mDensity = 0;

    /**
     * 获取屏幕宽度，单位像素
     * 
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        if (mScreenWidth > 0) {
            return mScreenWidth;
        }

        DisplayMetrics dm = new DisplayMetrics();
        // 取得窗口属性
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(dm);

        // 窗口的宽度
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        mDensity = dm.density;

        return mScreenWidth;
    }

    /**
     * 获取屏幕宽度，单位像素
     * 
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        if (mScreenHeight > 0) {
            return mScreenHeight;
        }

        if (context == null) {
            context = WankeTVApplication.getCurrentApplication();
        }

        DisplayMetrics dm = new DisplayMetrics();
        // 取得窗口属性
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(dm);

        // 窗口的宽度
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        mDensity = dm.density;

        return mScreenHeight;
    }

    public static DisplayImageOptions getOptionsFadeIn() {
        return new DisplayImageOptions.Builder().cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
                .displayer(new FadeInBitmapDisplayer(250))
                .imageScaleType(ImageScaleType.EXACTLY).build();
    }
}
