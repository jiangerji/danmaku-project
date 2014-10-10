package com.wanke.danmaku.protocol;

public class HeartBeatRequest extends BaseProtocol {

    @Override
    protected int getCommandId() {
        return CMD_HEARTBEAT;
    }

    @Override
    protected byte[] getBody() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected int getBodyLength() {
        return 0;
    }

    @Override
    protected int analyzeBody(byte[] reqBody) {
        return 0;
    }

}
