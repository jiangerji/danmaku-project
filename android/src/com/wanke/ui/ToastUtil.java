package com.wanke.ui;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    public static Toast showToast(Context context, int resId) {
        Toast toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }
}
