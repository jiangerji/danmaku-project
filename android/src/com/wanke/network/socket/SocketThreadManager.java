package com.wanke.network.socket;

import android.util.Log;

public class SocketThreadManager {
    private final static String TAG = "SocketThreadManager";

    private static SocketThreadManager s_SocketManager = null;

    private SocketInputThread mInputThread = null;
    private SocketOutputThread mOutThread = null;
    private SocketHeartThread mHeartThread = null;

    private final static Object mMutex = new Object();

    // 获取单例
    public static SocketThreadManager sharedInstance() {
        synchronized (mMutex) {
            if (s_SocketManager == null) {
                s_SocketManager = new SocketThreadManager();
            }
        }
        return s_SocketManager;
    }

    // 单例，不允许在外部构建对象
    private SocketThreadManager() {
        //        mHeartThread = new SocketHeartThread();
        //        mInputThread = new SocketInputThread();
        //        mOutThread = new SocketOutputThread();
    }

    private Thread mStartThread = null;

    /**
     * 启动线程
     */
    public void startThreads() {
        // 首先开启SocketClient
        mStartThread = new Thread(new Runnable() {

            @Override
            public void run() {
                SocketClient client = SocketClient.instance();
                if (client.isConnect()) {
                    mInputThread = new SocketInputThread();
                    mInputThread.setStart(true);
                    mInputThread.start();

                    mOutThread = new SocketOutputThread();
                    mOutThread.start();

                    mHeartThread = new SocketHeartThread();
                    mHeartThread.start();
                } else {
                    Log.d(TAG, "Socket Client is not connected!");
                }

                mStartThread = null;
            }
        });

        mStartThread.start();
    }

    /**
     * stop线程
     */
    public void stopThreads() {
        if (mStartThread != null) {
            mStartThread.interrupt();
        }

        if (mInputThread != null) {
            mInputThread.setStart(false);
        }

        if (mOutThread != null) {
            mOutThread.setStart(false);
        }

        if (mHeartThread != null) {
            mHeartThread.stopThread();
        }
    }

    public static void releaseInstance() {
        if (s_SocketManager != null) {
            s_SocketManager.stopThreads();
            s_SocketManager = null;
        }
    }

    public void sendMsg(byte[] buffer) {
        if (mOutThread != null) {
            mOutThread.addMsgToSendList(buffer);
        }
    }

    public static void main(String[] args) throws Exception {
        SocketThreadManager socketThreadManager = SocketThreadManager.sharedInstance();
        socketThreadManager.startThreads();
    }
}
