package com.wanke.danmaku.protocol;

import org.apache.mina.core.buffer.IoBuffer;

public class LogoutResquest extends BaseProtocol {
    private int userId = 0;

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

    @Override
    protected int getCommandId() {
        return CMD_USER_LOGOUT;
    }

    @Override
    protected byte[] getBody() {
        IoBuffer b = IoBuffer.allocate(getBodyLength());

        b.putInt(userId).flip();

        return b.array();
    }

    @Override
    protected int getBodyLength() {
        /**
         * 0 User_ID UINT4 4 帐号ID；
         */
        return 4;
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

        return 0;
    }

}
