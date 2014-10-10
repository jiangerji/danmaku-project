package com.wanke;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.mina.core.buffer.IoBuffer;

import com.wanke.danmaku.protocol.HeartBeatRequest;
import com.wanke.danmaku.protocol.InitConnectionRequest;
import com.wanke.danmaku.protocol.InitConnectionResponse;
import com.wanke.danmaku.protocol.LoginRequest;
import com.wanke.danmaku.protocol.LoginResponse;
import com.wanke.danmaku.protocol.LogoutResponse;
import com.wanke.danmaku.protocol.LogoutResquest;
import com.wanke.danmaku.protocol.PushChatResponse;
import com.wanke.danmaku.protocol.SendChatRequest;
import com.wanke.danmaku.protocol.SendChatResponse;

public class ProtocolTest {
    static Socket socket = null;
    static OutputStream out = null;
    static InputStream in = null;

    static int userId = 123654;
    static int roomId = 1001;
    static int authenticated = 1;
    static String authToken = "erasdfaerasdfawerfasdfawerfasd";
    static String mac = "0123456789";

    /**
     * 测试连接初始�?
     */
    static void testInitConnection() {
        InitConnectionRequest initConnectionRequest = new InitConnectionRequest();
        initConnectionRequest.setRoomId(userId);
        initConnectionRequest.setUserId(roomId);
        initConnectionRequest.setAuthenticated(authenticated);
        initConnectionRequest.setAuthToken(authToken);
        initConnectionRequest.setMac(mac);

        try {
            out.write(initConnectionRequest.getMessage());
            out.flush();

            byte[] charBuf = new byte[4096];
            int size = 0;
            size = in.read(charBuf, 0, 4096);
            IoBuffer ResBuf = IoBuffer.allocate(size);
            ResBuf.put(charBuf, 0, size);
            ResBuf.flip();

            InitConnectionResponse response = new InitConnectionResponse();
            response.analyzeMessage(ResBuf);
            System.out.println("Init Connection Result:" + response.getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试heart beat
     */
    static void testHeartBeat() {
        HeartBeatRequest heartBeatRequest = new HeartBeatRequest();

        try {
            out.write(heartBeatRequest.getMessage());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void testLogin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserId(userId);
        loginRequest.setAuthToken(authToken);

        try {
            out.write(loginRequest.getMessage());
            out.flush();

            byte[] charBuf = new byte[4096];
            int size = 0;
            size = in.read(charBuf, 0, 4096);
            IoBuffer ResBuf = IoBuffer.allocate(size);
            ResBuf.put(charBuf, 0, size);
            ResBuf.flip();

            LoginResponse response = new LoginResponse();
            response.analyzeMessage(ResBuf);
            System.out.println("Test Login Result:" + response.getResult());
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    static void testLogout() {
        LogoutResquest logoutResquest = new LogoutResquest();
        logoutResquest.setUserId(userId);

        try {
            out.write(logoutResquest.getMessage());
            out.flush();

            byte[] charBuf = new byte[4096];
            int size = 0;
            size = in.read(charBuf, 0, 4096);
            IoBuffer ResBuf = IoBuffer.allocate(size);
            ResBuf.put(charBuf, 0, size);
            ResBuf.flip();

            LogoutResponse response = new LogoutResponse();
            response.analyzeMessage(ResBuf);
            System.out.println("Test Logout Result:" + response.getResult());
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    static void testSendChat() {
        SendChatRequest request = new SendChatRequest();
        request.setAnonymous(1);
        request.setUserId(userId);
        request.setContent("hello world!");

        try {
            out.write(request.getMessage());
            out.flush();

            byte[] charBuf = new byte[4096];
            int size = 0;
            size = in.read(charBuf, 0, 4096);
            IoBuffer ResBuf = IoBuffer.allocate(size);
            ResBuf.put(charBuf, 0, size);
            ResBuf.flip();

            PushChatResponse response = new PushChatResponse();
            response.analyzeMessage(ResBuf);
            //            System.out.println("Send Chat Result:" + response.getResult());

            size = in.read(charBuf, 0, 4096);
            ResBuf = IoBuffer.allocate(size);
            ResBuf.put(charBuf, 0, size);
            ResBuf.flip();
            SendChatResponse sendChatResponse = new SendChatResponse();
            sendChatResponse.analyzeMessage(ResBuf);
            System.out.println("Send Chat Result:"
                    + sendChatResponse.getResult());

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void main(String[] args) {
        InetSocketAddress endpoint = new InetSocketAddress("192.168.41.204",
                9898);
        try {
            socket = new Socket();
            socket.setSoLinger(true, 2);
            socket.setSendBufferSize(32 * 1024);
            socket.setReceiveBufferSize(32 * 1024);
            socket.setTcpNoDelay(true);
            socket.connect(endpoint);
            out = socket.getOutputStream();
            in = socket.getInputStream();
        } catch (Exception e) {
        }

        System.out.println("\n=====start init connection");
        testInitConnection();

        System.out.println("\n=====start heart beat");
        testHeartBeat();

        System.out.println("\n=====start login");
        testLogin();

        System.out.println("\n=====start send chat");
        testSendChat();

        //        System.out.println("\n=====start logout");
        //        testLogout();

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
