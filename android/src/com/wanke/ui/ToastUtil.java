package com.wanke.ui;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    public static void showToast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }
}
