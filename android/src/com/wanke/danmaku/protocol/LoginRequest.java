package com.wanke.danmaku.protocol;

import org.apache.mina.core.buffer.IoBuffer;

public class LoginRequest extends BaseProtocol {

    /**
     * 用户id
     */
    private int userId;

    /**
     * 验证登录用的临时密码。
     */
    private String authToken;

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
     * @return the authToken
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * @param authToken
     *            the authToken to set
     */
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Override
    protected int getCommandId() {
        return CMD_USER_LOGIN;
    }

    @Override
    protected byte[] getBody() {
        IoBuffer b = IoBuffer.allocate(getBodyLength());

        b.putInt(userId)
                .put(fixLength(authToken, 64))
                .flip();

        return b.array();
    }

    @Override
    protected int getBodyLength() {
        /**
         * 0 User_ID UINT4 4 帐号ID。
         * 1 Auth_Token String[64] 64 验证登录用的临时密码。
         */
        return 4 + 64;
    }

    @Override
    protected int analyzeBody(byte[] reqBody) {
        if (reqBody.length != getBodyLength()) {
            return -1;
        }

        byte[] body = reqBody;
        IoBuffer b = IoBuffer.allocate(body.length);
        b.put(body);
        b.flip();

        userId = b.getInt();
        authToken = getStringByLength(b, 64);

        return 0;
    }

}
