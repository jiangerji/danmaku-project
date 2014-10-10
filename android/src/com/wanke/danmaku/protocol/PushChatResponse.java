package com.wanke.danmaku.protocol;

import org.apache.mina.core.buffer.IoBuffer;

public class PushChatResponse extends BaseProtocol {
    /**
     * 聊天类型
     * 1. 蜜语
     * 2. 公聊信息
     * 3. 系统消息
     * 4. 玩家操作信息
     */
    private int chatType = 0;

    /**
     * 聊天发起者是否登录
     */
    private int authenticated = -1;

    /**
     * 聊天发起者是否匿名发送
     */
    private int anonymous = 0;

    /**
     * 聊天发起者的user id
     */
    private int sendUserId = 0;

    /**
     * 聊天发起者的昵称，最大32字节
     */
    private String sendUserNickName = "";

    /**
     * 聊天内容，最大128字节
     */
    private String content = "";

    /**
     * @return the chatType
     */
    public int getChatType() {
        return chatType;
    }

    /**
     * @param chatType
     *            the chatType to set
     */
    public void setChatType(int chatType) {
        this.chatType = chatType;
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
     * @return the sendUserId
     */
    public int getSendUserId() {
        return sendUserId;
    }

    /**
     * @param sendUserId
     *            the sendUserId to set
     */
    public void setSendUserId(int sendUserId) {
        this.sendUserId = sendUserId;
    }

    /**
     * @return the sendUserNickName
     */
    public String getSendUserNickName() {
        return sendUserNickName;
    }

    /**
     * @param sendUserNickName
     *            the sendUserNickName to set
     */
    public void setSendUserNickName(String sendUserNickName) {
        this.sendUserNickName = sendUserNickName;
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
        return CMD_USER_CHAT_PUSH;
    }

    @Override
    protected byte[] getBody() {
        IoBuffer b = IoBuffer.allocate(getBodyLength());

        b.putInt(chatType)
                .putInt(authenticated)
                .putInt(anonymous)
                .putInt(sendUserId)
                .put(fixLength(sendUserNickName, 32))
                .put(fixLength(content, 128))
                .flip();

        return b.array();
    }

    @Override
    protected int getBodyLength() {
        /**
         * 0 Chat_Type INT4 4 聊天类型。
         * |- 1 - 密语；
         * |- 2 - 公聊信息；
         * |- 3 - 系统消息；
         * |- 4 - 玩家操作信息。
         * 1 Authenticated INT4 4 聊天发起者是否登录。
         * 2 Anonymous INT4 4 聊天发起者是否匿名发送。
         * 3 From_User_ID UINT4 4 聊天发起者的帐号ID。
         * 4 From_Nick_Name String[32] 32 聊天发起者的昵称。
         * 5 Content String[128] 128 聊天内容。
         */
        return 4 + 4 + 4 + 4 + 32 + 128;
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

        chatType = b.getInt();
        authenticated = b.getInt();
        anonymous = b.getInt();
        sendUserId = b.getInt();
        sendUserNickName = getStringByLength(b, 32);
        content = getStringByLength(b, 128);

        System.out.println("Chat Type:" + chatType);
        System.out.println("authenticated:" + authenticated);
        System.out.println("anonymous:" + anonymous);
        System.out.println("sendUserId:" + sendUserId);
        System.out.println("sendUserNickName:" + sendUserNickName);
        System.out.println("content:" + content);

        return 0;
    }

}
