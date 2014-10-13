package com.wanke.network.socket;

public class SocketThreadManager {

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

    /**
     * 启动线程
     */
    public void startThreads() {
        //        mInputThread = new SocketInputThread();
        //        mInputThread.setStart(true);
        //        mInputThread.start();

        mOutThread = new SocketOutputThread();
        mOutThread.start();

        mHeartThread = new SocketHeartThread();
        mHeartThread.start();
    }

    /**
     * stop线程
     */
    public void stopThreads() {
        //        mHeartThread.stopThread();
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
