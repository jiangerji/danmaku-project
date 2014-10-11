package com.wanke.network.socket;

import android.util.Log;

import com.wanke.danmaku.protocol.HeartBeatRequest;

class SocketHeartThread extends Thread {
    private final static int HEART_BEAT_INTERVAL = 20 * 1000;

    boolean isStop = false;
    boolean mIsConnectSocketSuccess = false;
    static SocketHeartThread mInstance;
    static final String TAG = "SocketHeartThread";

    public static synchronized SocketHeartThread instance() {
        if (mInstance == null) {
            mInstance = new SocketHeartThread();
        }
        return mInstance;
    }

    public SocketHeartThread() {
        SocketClient.instance();
    }

    public void stopThread() {
        isStop = true;
    }

    private boolean reConnect() {
        return SocketClient.instance().reConnect();
    }

    public void run() {
        isStop = false;
        while (!isStop) {
            boolean canConnectToServer = SocketClient.instance()
                    .canConnectToServer();
            if (canConnectToServer == false) {
                reConnect();
            }

            try {
                HeartBeatRequest request = new HeartBeatRequest();
                SocketClient.instance().sendMsg(request.getMessage());
                Log.d(TAG, "Send Heart Beat!");
            } catch (Exception e) {
                Log.d(TAG, "HeartBeat Exception:" + e.toString());
            }

            try {
                Thread.sleep(HEART_BEAT_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
