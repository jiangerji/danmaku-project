package com.wanke.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.wanke.WankeTVApplication;
import com.wanke.ui.widget.CircleBitmapDisplayer;
import com.wanke.ui.widget.WaitingProgressDialog;

public class UiUtils {

    private static int mScreenWidth = 0;
    private static int mScreenHeight = 0;
    private static float mDensity = 0;

    public static float getDensity(Context context) {
        if (mDensity > 0) {
            return mDensity;
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

        return mDensity;
    }

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
        return getOptionsFadeIn(250);
    }

    public static DisplayImageOptions getOptionsFadeIn(int millseconds) {
        return new DisplayImageOptions.Builder().cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
                .displayer(new FadeInBitmapDisplayer(millseconds))
                .imageScaleType(ImageScaleType.EXACTLY).build();
    }

    public static DisplayImageOptions getOptionsRound(int size) {
        return new DisplayImageOptions.Builder().cacheOnDisk(true)
                .cacheInMemory(true)
                .displayer(new RoundedBitmapDisplayer((int) (size)))
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
    }

    public static DisplayImageOptions getOptionCircle() {
        return new DisplayImageOptions.Builder().cacheOnDisk(true)
                .cacheInMemory(true)
                .displayer(new CircleBitmapDisplayer())
                .build();
    }

    public static Dialog showWaitingDialog(Context context) {
        WaitingProgressDialog progressDialog = new WaitingProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        return progressDialog;
    }

    public static void dismissWaitingDialog(Dialog dialog) {
        try {
            dialog.dismiss();
        } catch (Exception e) {

        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
                .getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }
}
