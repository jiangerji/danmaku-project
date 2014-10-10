package com.wanke.danmaku.protocol;

import org.apache.mina.core.buffer.IoBuffer;

public class InitConnectionRequest extends BaseProtocol {

    /**
     * 房间号
     */
    private int roomId = 0;

    /**
     * 用户ID
     */
    private int userId = 0;

    /**
     * 此连接是否登录过
     */
    private int authenticated = 0;

    /**
     * 验证登录用的临时密码，长度64位
     */
    private String authToken = null;

    /**
     * 此连接的MAC地址，长度16位
     */
    private String mac = null;

    private final int bodyLength = 4 + 4 + 4 + 64 + 16;

    /**
     * @return the roomId
     */
    public int getRoomId() {
        return roomId;
    }

    /**
     * @param roomId
     *            the roomId to set
     */
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    /**
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return the authenticated
     */
    public int getAuthenticated() {
        return authenticated;
    }

    /**
     * @param authenticated
     *            the authenticated to set
     */
    public void setAuthenticated(int authenticated) {
        this.authenticated = authenticated;
    }

    /**
     * @return the authToken
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * @param authToken
     *            the authToken to set, 长度必须小于64字节
     */
    public void setAuthToken(String authToken) {
        if (authToken.length() > 64) {
            System.err.println("authToken length more than 64!");
        }
        this.authToken = authToken;
    }

    /**
     * @return the mac
     */
    public String getMac() {
        return mac;
    }

    /**
     * @param mac
     *            the mac to set
     */
    public void setMac(String mac) {
        if (mac.length() > 64) {
            System.err.println("mac length more than 64!");
        }
        this.mac = mac;
    }

    @Override
    protected int getBodyLength() {
        return bodyLength;
    }

    @Override
    public byte[] getBody() {
        IoBuffer b = IoBuffer.allocate(getBodyLength());

        b.putInt(roomId)
                .putInt(userId)
                .putInt(authenticated)
                .put(fixLength(authToken, 64))
                .put(fixLength(mac, 16))
                .flip();

        return b.array();
    }

    @Override
    public int analyzeBody(byte[] recvBody) {
        if (recvBody.length != getBodyLength()) {
            return -1;
        }

        byte[] body = recvBody;
        IoBuffer b = IoBuffer.allocate(body.length);
        b.put(body);
        b.flip();

        roomId = b.getInt();
        userId = b.getInt();
        authenticated = b.getInt();
        authToken = getStringByLength(b, 64);
        mac = getStringByLength(b, 16);

        return 0;
    }

    @Override
    public int getCommandId() {
        return CMD_INIT;
    }

}
