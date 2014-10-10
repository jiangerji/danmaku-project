package com.wanke.danmaku.protocol;

import org.apache.mina.core.buffer.IoBuffer;

public class SendChatRequest extends BaseProtocol {
    /**
     * 此消息是否匿名发送
     */
    private int anonymous = 0;

    /**
     * 发送消息到目标用户的user id
     */
    private int userId = 0;

    /**
     * 聊天内容，长度最大为128
     */
    private String content;

    /**
     * @return the anonymous
     */
    public int getAnonymous() {
        return anonymous;
    }

    /**
     * @param anonymous
     *            the anonymous to set
     */
    public void setAnonymous(int anonymous) {
        this.anonymous = anonymous;
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
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     *            the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    protected int getCommandId() {
        // TODO Auto-generated method stub
        return CMD_USER_CHAT;
    }

    @Override
    protected byte[] getBody() {
        IoBuffer b = IoBuffer.allocate(getBodyLength());

        b.putInt(anonymous)
                .putInt(userId)
                .put(fixLength(content, 128))
                .flip();

        return b.array();
    }

    @Override
    protected int getBodyLength() {
        /**
         * 0 Anonymous INT4 4 此消息是否匿名发送。
         * 1 To_User_ID UINT4 4 此消息的目标帐号ID。
         * 2 Content String[128] 128 聊天内容。
         */
        return 4 + 4 + 128;
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

        anonymous = b.getInt();
        userId = b.getInt();
        content = getStringByLength(b, 128);

        return 0;
    }

}
