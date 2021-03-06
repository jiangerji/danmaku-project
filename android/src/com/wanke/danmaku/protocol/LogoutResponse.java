package com.wanke.danmaku.protocol;

import org.apache.mina.core.buffer.IoBuffer;

public class LogoutResponse extends BaseProtocol {
    private int result = -1;

    /**
     * @return the result
     */
    public int getResult() {
        return result;
    }

    /**
     * @param result
     *            the result to set
     */
    public void setResult(int result) {
        this.result = result;
    }

    @Override
    protected int getCommandId() {
        return CMD_USER_LOGOUT_RES;
    }

    @Override
    protected byte[] getBody() {
        IoBuffer b = IoBuffer.allocate(getBodyLength());

        b.putInt(result).flip();

        return b.array();
    }

    @Override
    protected int getBodyLength() {
        /**
         * 0 Result_Code INT4 4 返回结果。
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

        result = b.getInt();

        return 0;
    }

}
