package com.wanke.ui;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {

    public static Toast showToast(Context context, int resId) {
        Toast toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }

    public static Toast showToast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }

    public static Toast showToastInCenter(Context context, int resId) {
        Toast toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        return toast;
    }
}
