package com.wanke.network.http;

import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class CommonHttpUtils {
    private final static String TAG = "http";

    public static void get(
            final String url, final RequestCallBack<String> callBack) {
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,
                url,
                new RequestCallBack<String>() {
                    @Override
                    public void onLoading(
                            long total, long current, boolean isUploading) {
                        if (callBack != null) {
                            callBack.onLoading(total, current, isUploading);
                        }
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.d(TAG, "Get " + url);
                        Log.d(TAG, "onSuccess:" + responseInfo.result);
                        if (callBack != null) {
                            callBack.onSuccess(responseInfo);
                        }
                    }

                    @Override
                    public void onStart() {
                        if (callBack != null) {
                            callBack.onStart();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Log.d(TAG, "Get " + url + " failed:" + msg);
                        if (callBack != null) {
                            callBack.onFailure(error, msg);
                        }
                    }
                });
    }
}
