package com.wanke.danmaku;

import android.util.Log;

import com.wanke.danmaku.protocol.BaseProtocol;
import com.wanke.danmaku.protocol.BaseProtocol.ProtocolListener;
import com.wanke.danmaku.protocol.InitConnectionRequest;
import com.wanke.danmaku.protocol.LoginRequest;
import com.wanke.danmaku.protocol.LogoutResquest;
import com.wanke.danmaku.protocol.PushChatResponse;
import com.wanke.danmaku.protocol.SendChatRequest;
import com.wanke.network.socket.SocketThreadManager;

public class DanmakuController {
    public static String TAG = "DanmakuController";

    private static DanmakuController _instance = new DanmakuController();

    public static DanmakuController getInstance() {
        return _instance;
    }

    private DanmakuController() {

    }

    public static interface DanmakuListener {
        // 连接弹幕服务状态
        public void onConnectionStatus(int status);

        // 登录弹幕服务状态
        public void onLoginStatus(int status);

        // 登出弹幕服务状态
        // public void onLogoutStatus(int status);

        // 接收到弹幕服务状态
        public void onPushChatReceive(PushChatResponse pushChatResponse);

        // 发送弹幕的状态
        public void onSendChatStatus(int status);
    }

    private DanmakuListener mDanmakuListener = null;

    private int mConnectionStatus = -1; // 弹幕服务器连接状态
    private int mLoginStatus = -1; // 弹幕服务器登录状态

    private ProtocolListener mListener = new ProtocolListener() {

        @Override
        /**
         * status
         *  0: 表示发送成功
         *  -1: 表示发送失败
         *  1: 表示不允许发送，需要登录
         */
        public void onSendChatStatus(int status) {

        }

        @Override
        public void onPushChatReceive(PushChatResponse pushChatResponse) {
            if (mDanmakuListener != null) {
                mDanmakuListener.onPushChatReceive(pushChatResponse);
            }
        }

        @Override
        public void onLogoutStatus(int status) {
            Log.d(TAG, "Logout Status:" + status);
            SocketThreadManager.sharedInstance().stopThreads();
        }

        @Override
        public void onLoginStatus(int status) {
            Log.d(TAG, "Login Status:" + status);
            if (mDanmakuListener != null) {
                mDanmakuListener.onLoginStatus(status);
            }

            mLoginStatus = status;
        }

        @Override
        public void onInitConnectionStatus(int status) {
            Log.d(TAG, "Connection Status:" + status);
            if (status != -1) {
                login();
            } else {
                // 连接失败
            }

            if (mDanmakuListener != null) {
                mDanmakuListener.onConnectionStatus(status);
            }

            mConnectionStatus = status;
        }
    };

    /**
     * 连接到弹幕服务
     */
    public void connect(DanmakuListener listener) {
        mDanmakuListener = listener;

        BaseProtocol.addProtocolListener(mListener);

        SocketThreadManager manager = SocketThreadManager.sharedInstance();
        manager.startThreads();

        initConnection();
    }

    public void disconnect() {
        logout();

        BaseProtocol.removeProtocolListener(mListener);
        SocketThreadManager.sharedInstance().stopThreads();
        mDanmakuListener = null;
    }

    static int userId = 123654;
    static int roomId = 1001;
    static int authenticated = 1;
    static String authToken = "erasdfaerasdfawerfasdfawerfasd";
    static String mac = "0123456789";

    /**
     * 初始化到弹幕服务器的连接
     */
    void initConnection() {
        InitConnectionRequest initConnectionRequest = new InitConnectionRequest();
        initConnectionRequest.setRoomId(userId);
        initConnectionRequest.setUserId(roomId);
        initConnectionRequest.setAuthenticated(authenticated);
        initConnectionRequest.setAuthToken(authToken);
        initConnectionRequest.setMac(mac);

        SocketThreadManager.sharedInstance()
                .sendMsg(initConnectionRequest.getMessage());
        Log.d(TAG, "Send Init Connection Request!");
    }

    /**
     * 登录到弹幕服务器，这样才能发送弹幕
     */
    void login() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserId(userId);
        loginRequest.setAuthToken(authToken);

        SocketThreadManager.sharedInstance()
                .sendMsg(loginRequest.getMessage());
        Log.d(TAG, "Send Login Connection Request!");
    }

    /**
     * 退出登录到弹幕服务器
     */
    void logout() {
        LogoutResquest logoutResquest = new LogoutResquest();
        logoutResquest.setUserId(userId);

        SocketThreadManager.sharedInstance()
                .sendMsg(logoutResquest.getMessage());
    }

    /**
     * 发送弹幕聊天信息
     */
    public void sendChat(String content) {
        if (mLoginStatus < 0) {
            // 未登录的话
            if (mDanmakuListener != null) {
                mDanmakuListener.onLoginStatus(-1);
            }
        } else {
            SendChatRequest request = new SendChatRequest();
            request.setAnonymous(1);
            request.setUserId(userId);
            request.setContent(content);

            SocketThreadManager.sharedInstance()
                    .sendMsg(request.getMessage());
        }
    }
}
