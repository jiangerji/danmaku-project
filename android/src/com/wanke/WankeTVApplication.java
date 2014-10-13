package com.wanke;

import android.app.Application;

public class WankeTVApplication extends Application {

    private static WankeTVApplication mCurrentApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();

        mCurrentApplication = this;
    }

    public WankeTVApplication getCurrentApplication() {
        return mCurrentApplication;
    }
}
