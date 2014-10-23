package com.wanke.util;

import org.json.JSONObject;

import android.text.TextUtils;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.wanke.model.AccountInfo;
import com.wanke.network.http.CommonHttpUtils;

public class AccountUtil {

    public static interface LoginCallback {
        /**
         * 登录成功
         */
        public void onLoginSuccess();

        /**
         * 登录失败的回调,
         * 
         * @param error
         *            错误代码
         * @param msg
         *            错误信息
         */
        public void onLoginFailed(int error, String msg);

        /**
         * 登录过程发生异常
         * 
         * @param msg
         *            异常信息
         */
        public void onLoginException(String msg);
    }

    /**
     * 登录服务器
     * 
     * @param username
     *            用户名
     * @param password
     *            密码
     * 
     * @return 返回发起登录请求是否成功
     */
    public static boolean login(
            final String username, final String password,
            final LoginCallback callback) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return false;
        }

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("username", username);
        params.addQueryStringParameter("password", password);

        CommonHttpUtils.get("login", params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject object = new JSONObject(responseInfo.result);
                    int errorCode = object.getInt("error");
                    if (errorCode == 0) {
                        // 登录成功
                        if (callback != null) {
                            callback.onLoginSuccess();
                        }

                        String avatar = object.getString("avatar");
                        int uid = object.getInt("uid");
                        PreferenceUtil.saveAccountInfo(username, password, ""
                                + uid, avatar);
                    } else {
                        String msg = object.getString("msg");
                        if (callback != null) {
                            callback.onLoginFailed(errorCode, msg);
                        }
                    }
                } catch (Exception e) {
                    if (callback != null) {
                        callback.onLoginException(e.toString());
                    }
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if (callback != null) {
                    callback.onLoginException(msg);
                }
            }
        });
        return true;
    }

    public static interface RegisterCallback {
        /**
         * 注册成功
         */
        public void onRegisterSuccess();

        /**
         * 注册失败的回调,
         * 
         * @param error
         *            错误代码
         * @param msg
         *            错误信息
         */
        public void onRegisterFailed(int error, String msg);

        /**
         * 注册过程发生异常
         * 
         * @param msg
         *            异常信息
         */
        public void onRegisterException(String msg);
    }

    /**
     * 注册新用户
     * 
     * @param username
     *            用户名
     * @param password
     *            用户密码
     * @param email
     *            用户Email
     * @return 是否成功发起注册请求
     */
    public static boolean register(
            String username, String password, String email,
            final RegisterCallback callback) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(email)) {
            return false;
        }

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("username", username);
        params.addQueryStringParameter("password", password);
        params.addQueryStringParameter("email", email);

        CommonHttpUtils.get("register", params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException error, String msg) {
                if (callback != null) {
                    callback.onRegisterException(msg);
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject object = new JSONObject(responseInfo.result);
                    int errorCode = object.getInt("error");
                    if (errorCode == 0) {
                        // 登录成功
                        if (callback != null) {
                            callback.onRegisterSuccess();
                        }
                    } else {
                        String msg = object.getString("msg");
                        if (callback != null) {
                            callback.onRegisterFailed(errorCode, msg);
                        }
                    }
                } catch (Exception e) {
                    if (callback != null) {
                        callback.onRegisterException(e.toString());
                    }
                }
            }
        });

        return true;
    }

    public static interface UserInfoCallback {
        /**
         * 注册成功
         */
        public void onUserInfoSuccess(AccountInfo info);

        /**
         * 注册失败的回调,
         * 
         * @param error
         *            错误代码
         * @param msg
         *            错误信息
         */
        public void onUserInfoFailed(int error, String msg);

        /**
         * 注册过程发生异常
         * 
         * @param msg
         *            异常信息
         */
        public void onUserInfoException(String msg);
    }

    /**
     * 获取某个用户的信息
     * 
     * @param uid
     * @return
     */
    public static boolean userInfo(
            String uid, final UserInfoCallback callback) {
        if (TextUtils.isEmpty(uid)) {
            return false;
        }

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", uid);

        CommonHttpUtils.get("userInfo", params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException error, String msg) {
                if (callback != null) {
                    callback.onUserInfoException(msg);
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject object = new JSONObject(responseInfo.result);
                    int errorCode = object.getInt("error");
                    if (errorCode == 0) {
                        // 登录成功
                        if (callback != null) {
                            AccountInfo info = new AccountInfo();
                            info.setUid("" + object.getInt("uid"));
                            info.setUsername(object.getString("username"));
                            info.setEmail(object.getString("email"));
                            info.setExp(object.getLong("exp"));
                            callback.onUserInfoSuccess(info);
                        }
                    } else {
                        String msg = object.getString("msg");
                        if (callback != null) {
                            callback.onUserInfoFailed(errorCode, msg);
                        }
                    }
                } catch (Exception e) {
                    if (callback != null) {
                        callback.onUserInfoException(e.toString());
                    }
                }
            }
        }
                );

        return true;
    }

}
