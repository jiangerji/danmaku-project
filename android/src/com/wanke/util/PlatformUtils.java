package com.wanke.util;

import android.os.Environment;

public class PlatformUtils {

    /**
     * 检查是否存在SDCard
     * 
     * @return
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
}
