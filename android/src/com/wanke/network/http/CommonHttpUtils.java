package com.wanke.network.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.text.TextUtils;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.cache.MD5FileNameGenerator;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wanke.WankeTVApplication;

public class CommonHttpUtils {
    private final static String TAG = "http";

    private final static String BASE_URL = "http://192.168.41.101:9257/wanketv/live/";

    public static void get(
            final String action, final RequestParams params,
            final RequestCallBack<String> callBack) {
        get(action, params, callBack, null);
    }

    /**
     * 请求网络数据
     * 
     * @param action
     *            请求方法
     * @param params
     *            请求参数
     * @param callBack
     *            请求完成回调
     * @param cacheKey
     *            如果不为空，会将数据进行缓存，当网络出现异常，会直接返回cache数据
     *            如果为空，则不会进行缓存，当网络出现异常，会返回异常给调用者
     */
    public static void get(
            final String action, final RequestParams params,
            final RequestCallBack<String> callBack, final String cacheKey) {
        final String url = BASE_URL + action;
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000 * 15);
        http.configSoTimeout(1000 * 10);
        http.configTimeout(1000 * 10);
        http.send(HttpRequest.HttpMethod.GET,
                url,
                params,
                new RequestCallBack<String>() {
                    MD5FileNameGenerator md5 = new MD5FileNameGenerator();

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

                        try {
                            if (!TextUtils.isEmpty(cacheKey)) {
                                String cacheName = md5.generate(cacheKey);

                                File file = new File(WankeTVApplication.getCurrentApplication()
                                        .getExternalCacheDir(),
                                        cacheName);
                                FileOutputStream fos = new FileOutputStream(file);
                                fos.write(responseInfo.result.getBytes());
                                fos.close();
                            }
                        } catch (Exception e) {

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
                        boolean findCache = false;
                        // 获取失败，寻找cache
                        FileInputStream fis = null;
                        String cacheContent = "";
                        try {
                            if (!TextUtils.isEmpty(cacheKey)) {
                                String cacheName = md5.generate(cacheKey);

                                File file = new File(WankeTVApplication.getCurrentApplication()
                                        .getExternalCacheDir(),
                                        cacheName);
                                fis = new FileInputStream(file);
                                byte[] buffer = new byte[10240];
                                int count = fis.read(buffer);
                                StringBuffer sb = new StringBuffer();
                                while (count > 0) {
                                    sb.append(new String(buffer, 0, count));

                                    count = fis.read(buffer);
                                }

                                findCache = true;
                                cacheContent = sb.toString();
                            }
                        } catch (Exception e) {

                        } finally {
                            if (fis != null) {
                                try {
                                    fis.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        if (!findCache) {
                            if (callBack != null) {
                                callBack.onFailure(error, msg);
                            }
                        } else {
                            if (callBack != null) {
                                callBack.onFailure(new HttpExceptionButFoundCache(error),
                                        cacheContent);
                            }
                        }

                    }
                });
    }
}
